package com.rafaelhosaka.shareme.message;

import com.rafaelhosaka.shareme.exception.UserProfileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/message")
public class MessageController {
    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService){
        this.messageService = messageService;
    }

    @PostMapping("/get")
    public ResponseEntity<List<Message>> getMessages(@RequestPart("senderId") String senderId, @RequestPart("receiverId")String receiverId){
            return ResponseEntity.ok().body(messageService.getMessages(senderId, receiverId));
    }

    @PostMapping("/send")
    public ResponseEntity<Message> sendMessage(@RequestPart("senderId") String senderId, @RequestPart("receiverId")String receiverId, @RequestPart("content")String content){
        try {
            return ResponseEntity.ok().body(messageService.sendMessage(senderId, receiverId, content));
        } catch (UserProfileNotFoundException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
