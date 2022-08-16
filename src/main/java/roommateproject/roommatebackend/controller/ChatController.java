package roommateproject.roommatebackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roommateproject.roommatebackend.entity.ChatRoom;
import roommateproject.roommatebackend.service.ChatService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/api/chat")
    public ChatRoom createRoom(@RequestBody String name) {
        return chatService.createRoom(name);
    }

    @GetMapping("/api/chat")
    public List<ChatRoom> findAllRoom() {
        return chatService.findAllRoom();
    }
}
