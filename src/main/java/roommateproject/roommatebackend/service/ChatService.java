package roommateproject.roommatebackend.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.sql.rowset.serial.SerialBlob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roommateproject.roommatebackend.dto.ChatDto;
import roommateproject.roommatebackend.dto.Message;
import roommateproject.roommatebackend.dto.ChatListDto;
import roommateproject.roommatebackend.dto.ChatRoomDto;
import roommateproject.roommatebackend.dto.UserInfo;
import roommateproject.roommatebackend.entity.Chat;
import roommateproject.roommatebackend.entity.ChatRoom;
import roommateproject.roommatebackend.entity.Participant;
import roommateproject.roommatebackend.entity.User;
import roommateproject.roommatebackend.repository.ChatRepository;
import roommateproject.roommatebackend.repository.ChatRoomRepository;
import roommateproject.roommatebackend.repository.HomeRepository;
import roommateproject.roommatebackend.repository.ParticipantRepository;
import roommateproject.roommatebackend.repository.UserRepository;
import roommateproject.roommatebackend.response.ResponseMessage;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final ParticipantRepository participantRepository;
    private final HomeRepository homeRepository;

    public UserInfo getUserInfo(User user) {
        return new UserInfo(user.getNickName(), user.getId(), user.getAge(),
                homeRepository.getLocation(user));
    }

    /**
     * 채팅방 리스트 조회
     */
    @Transactional
    public List<ChatRoomDto> findAllRoomById(Long userId) {
        User sender = userRepository.find(userId);
        List<ChatRoom> chatRooms = participantRepository.findAllChatRoom(sender);
        List<ChatRoomDto> chatRoomDtoList = new ArrayList<>();

        for (ChatRoom room : chatRooms) {
            ChatRoomDto chatRoomDto = new ChatRoomDto();

            Chat chat = chatRepository.findLastMessage(room);
            if (chat == null) {
                chatRoomRepository.remove(room);
                continue;
            }
            else {
                chatRoomDto.setLastMessage(chat.getMessage());
                chatRoomDto.setSendTime(chat.getSendTime());
            }
            chatRoomDto.setRoomId(room.getRoomId());
            User receiver = participantRepository.findReceiver(sender, room);
            chatRoomDto.setUserInfo(getUserInfo(receiver));

            chatRoomDtoList.add(chatRoomDto);
        }
        return chatRoomDtoList;
    }

    /**
     * 채팅방 생성
     */
    @Transactional
    public ChatRoom createRoom(Map<String, Long> request) {
        User sender = userRepository.find(request.get("sender"));
        User receiver = userRepository.find(request.get("receiver"));
        List<ChatRoom> senderRoomList = participantRepository.findAllChatRoom(sender);
        List<ChatRoom> receiverRoomList = participantRepository.findAllChatRoom(receiver);

        // sender와 receiver의 방이 이미 있으면 그 방을 리턴
        for (ChatRoom room : senderRoomList) {
            if (receiverRoomList.contains(room)) {
                return room;
            }
        }

        // sender와 receiver의 방이 없으면 새로 생성해서 리턴
        ChatRoom chatRoom = new ChatRoom();
        chatRoomRepository.store(chatRoom);
        participantRepository.store(new Participant(sender, chatRoom));
        participantRepository.store(new Participant(receiver, chatRoom));

        return chatRoom;
    }

    /**
     * 채팅방 채팅 리스트 조회
     */
    public ChatListDto getRoomChatList(Long userId, Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findRoom(roomId);
        User sender = userRepository.find(userId);
        User receiver = participantRepository.findReceiver(sender, chatRoom);
        UserInfo userInfo = getUserInfo(receiver);

        List<Chat> chats = chatRepository.findChatListById(chatRoom);
        List<ChatDto> chatDtos = new ArrayList<>();

        chats.forEach(chat -> {
            ChatDto chatDto = new ChatDto();
            chatDto.setMessage(chat.getMessage());
            chatDto.setSendTime(chat.getSendTime());
            if (sender.getId().equals(chat.getSenderId())) {
                chatDto.setIsMe(true);
            } else {
                chatDto.setIsMe(false);
            }
            if (chat.getIsImage()) {
                chatDto.setIsImage(true);
                try {
                    chatDto.setImage(Base64.getEncoder().encodeToString(
                            chat.getImage().getBytes(1, (int) chat.getImage().length())
                    )); // Blob 이미지 -> byte 배열 -> Base64 인코딩
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                chatDto.setIsImage(false);
            }

            chatDtos.add(chatDto);
        });

        return new ChatListDto(userInfo, chatDtos);
    }


    /**
     * 텍스트 메시지 저장
     */
    @Transactional
    public void saveTextMessage(Message message) {
        ChatRoom chatRoom = chatRoomRepository.findRoom(message.getRoomId());
        Chat chat = Chat.builder()
                .chatRoom(chatRoom)
                .message(message.getMessage())
                .sendTime(new Date())
                .senderId(message.getSenderId())
                .isImage(false)
                .build();
        chatRepository.store(chat);
    }


    /**
     * 이미지 메시지 저장
     */
    @Transactional
    public void saveImageMessage(Message message) {
        ChatRoom chatRoom = chatRoomRepository.findRoom(message.getRoomId());

        Chat chat = null;
        try {
            chat = Chat.builder()
                    .chatRoom(chatRoom)
                    .sendTime(new Date())
                    .senderId(message.getSenderId())
                    .isImage(true)
                    .image(new SerialBlob( Base64.getDecoder().decode(
                            message.getImage())
                    )) // Base64 인코딩 -> byte 배열 -> Blob 이미지
                    .build();
            chatRepository.store(chat);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 채팅방 나가기
    @Transactional
    public void deleteRoom(Long roomId) {
        ChatRoom room = chatRoomRepository.findRoom(roomId);
        chatRoomRepository.remove(room);
    }

}
