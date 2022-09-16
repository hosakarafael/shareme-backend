package com.rafaelhosaka.shareme.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/chat")
public class ChatController {
    private ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService){
        this.chatService = chatService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<Chat>> getChatByUserId(@PathVariable("id")String id){
        return ResponseEntity.ok().body(chatService.getChatByUserId(id));
    }

    @PutMapping("/markAsRead")
    public ResponseEntity<Integer> markAsRead(@RequestPart("ownerId") String ownerId, @RequestPart("friendId")String friendId ){
        return ResponseEntity.ok().body(chatService.markAsRead(ownerId, friendId));
    }

    @GetMapping("/{userId}/unreadCount")
    public int unreadCount(@PathVariable("userId")String userId){
        return chatService.unreadCount(userId);
    }

}
