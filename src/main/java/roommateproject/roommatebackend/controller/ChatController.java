package roommateproject.roommatebackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import roommateproject.roommatebackend.dto.ChatDto;
import roommateproject.roommatebackend.service.ChatService;

@RequiredArgsConstructor
@Controller
@CrossOrigin
public class ChatController {

    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatService chatService;

    @MessageMapping("/chat/message")
    public void message(ChatDto message) {
//        if (MessageType.ENTER.equals(message.getType()))
//            message.setMessage(message.getSender() + "님이 입장하셨습니다.");

        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
        // 메시지 db에 저장
        chatService.saveMessage(message);
    }

    @PostMapping("/api/chat/message")
    @ResponseBody
    public void messageTeset(@RequestBody ChatDto message) {
        System.out.println("message = " + message.toString());
        chatService.saveMessage(message);
    }
}