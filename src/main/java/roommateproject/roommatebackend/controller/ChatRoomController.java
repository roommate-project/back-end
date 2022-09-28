package roommateproject.roommatebackend.controller;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import roommateproject.roommatebackend.dto.ChatRoomDto;
import roommateproject.roommatebackend.entity.Chat;
import roommateproject.roommatebackend.entity.ChatRoom;
import roommateproject.roommatebackend.service.ChatService;

@RequiredArgsConstructor
@Controller
@RequestMapping("/api/chat")
@CrossOrigin
public class ChatRoomController {

    private final ChatService chatService;

    // 모든 채팅방 목록 반환
    @GetMapping("/rooms/{userId}")
    @ResponseBody
    public List<ChatRoomDto> room(@PathVariable Long userId) {
        return chatService.findAllRoomById(userId);
    }

    // 채팅방 생성
    @PostMapping("/room")
    @ResponseBody
    public ChatRoom createRoom(@RequestBody Map<String, Long> request) {
        return chatService.createRoom(request);
    }

    // 특정 채팅방 조회
    @GetMapping("/room/{roomId}")
    @ResponseBody
    public List<Chat> roomInfo(@PathVariable Long roomId) {
        return chatService.findChatList(roomId);
    }

    //TODO: 채팅방 나기기

}