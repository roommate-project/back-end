package roommateproject.roommatebackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import roommateproject.roommatebackend.dto.Message;
import roommateproject.roommatebackend.dto.Message.MessageType;
import roommateproject.roommatebackend.service.ChatService;

@RequiredArgsConstructor
@Controller
@CrossOrigin
public class ChatController {

    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatService chatService;

    @MessageMapping("/chat/message")
    public void message(Message message) {
        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
        // 메시지 db에 저장
        if (MessageType.CHAT.equals(message.getType())) { // 텍스트 메시지
            chatService.saveTextMessage(message);
        } else if (MessageType.IMAGE.equals(message.getType())) { // 이미지 메시지
            chatService.saveImageMessage(message);
        }
    }

    @PostMapping("/api/chat/message")
    @ResponseBody
    public void messageTeset(@RequestBody Message message) {
        System.out.println("message = " + message.toString());
        chatService.saveTextMessage(message);
    }

    @PostMapping("/api/chat/image")
    @ResponseBody
    public void messageImageTeset(@RequestBody Message message) {
        System.out.println("message = " + message.toString());
        chatService.saveImageMessage(message);
    }
}