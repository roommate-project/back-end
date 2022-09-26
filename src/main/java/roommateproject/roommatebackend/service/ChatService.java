package roommateproject.roommatebackend.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roommateproject.roommatebackend.dto.ChatDto;
import roommateproject.roommatebackend.dto.ChatRoomDto;
import roommateproject.roommatebackend.entity.Chat;
import roommateproject.roommatebackend.entity.ChatRoom;
import roommateproject.roommatebackend.entity.Participant;
import roommateproject.roommatebackend.entity.User;
import roommateproject.roommatebackend.repository.ChatRepository;
import roommateproject.roommatebackend.repository.ChatRoomRepository;
import roommateproject.roommatebackend.repository.ImageRepository;
import roommateproject.roommatebackend.repository.ParticipantRepository;
import roommateproject.roommatebackend.repository.UserRepository;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final ParticipantRepository participantRepository;
    private final ImageRepository imageRepository;

    public List<ChatRoomDto> findAllRoomById(Long user_id) {
        User user = userRepository.find(user_id);
        List<ChatRoom> chatRooms = participantRepository.findAllChatRoom(user);
        List<ChatRoomDto> chatRoomDtoList = new ArrayList<>();
        for (ChatRoom room : chatRooms) {
            Chat chat = chatRepository.findLastMessage(room);
            ChatRoomDto chatRoomDto =
                    ChatRoomDto.builder()
                            .roomId(room.getRoomId())
                            .sender(participantRepository.findReceiver(user, room)
                                    .getName())
                            .lastMessage(chat.getMessage())
                            .sendTime(chat.getSendTime())
                            .representImageId(
                                    imageRepository.getRepresentImage(userRepository.find(user_id))
                                            .getId())
                            .build();
            chatRoomDtoList.add(chatRoomDto);
        }
        return chatRoomDtoList;
    }

    public List<Chat> findChatList(Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findRoom(roomId);
        return chatRepository.findChatListById(chatRoom);
    }

    @Transactional
    public ChatRoom createRoom(Map<String, Long> request) {
        User sender = userRepository.find(request.get("sender"));
        User receiver = userRepository.find(request.get("receiver"));
        ChatRoom chatRoom = new ChatRoom();
        chatRoomRepository.store(chatRoom);
        participantRepository.store(new Participant(sender, chatRoom));
        participantRepository.store(new Participant(receiver, chatRoom));

        return chatRoom;
    }

    @Transactional
    public void saveMessage(ChatDto message) {
        ChatRoom chatRoom = chatRoomRepository.findRoom(message.getRoomId());
        Chat chat = Chat.builder()
                .chatRoom(chatRoom)
                .message(message.getMessage())
                .sendTime(new Date())
                .fileCheck(false)
                .sender(message.getSender())
                .build();
        chatRepository.store(chat);
    }

}
