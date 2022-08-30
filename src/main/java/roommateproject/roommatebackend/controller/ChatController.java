package roommateproject.roommatebackend.controller;

import org.springframework.web.bind.annotation.*;
import roommateproject.roommatebackend.entity.ChatRoom;
import roommateproject.roommatebackend.service.ChatService;

import java.util.List;

@RestController
@CrossOrigin
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/api/chat")
    public ChatRoom createRoom(@RequestBody String name) {
        return chatService.createRoom(name);
    }

    @GetMapping("/api/chat")
    public List<ChatRoom> findAllRoom() {
        return chatService.findAllRoom();
    }
}
