package roommateproject.roommatebackend.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import roommateproject.roommatebackend.dto.ChatListDto;
import roommateproject.roommatebackend.dto.ChatRoomDto;
import roommateproject.roommatebackend.entity.ChatRoom;
import roommateproject.roommatebackend.response.ResponseMessage;
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
    public List<ChatRoomDto> getRoomList(@PathVariable Long userId) {
        return chatService.findAllRoomById(userId);
    }

    // 채팅방 생성
    @PostMapping("/room")
    @ResponseBody
    public ChatRoom createRoom(@RequestBody Map<String, Long> request) {
        return chatService.createRoom(request);
    }

    // 특정 채팅방 조회
    @GetMapping("/room/{userId}/{roomId}")
    @ResponseBody
    public ChatListDto getRoomChatList(@PathVariable Long userId, @PathVariable Long roomId) {
        return chatService.getRoomChatList(userId,roomId);
    }

    // 채팅방 나가기
//    @DeleteMapping("/room/{roomId}")
//    public ResponseMessage deleteRoom(@PathVariable Long roomId) {
//        chatService.deleteRoom(roomId);
//        return new ResponseMessage(HttpStatus.OK.value(), true, "채팅방 삭제 완료", new Date());
//    }

}