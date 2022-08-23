package com.rafaelhosaka.shareme.chat;

import com.rafaelhosaka.shareme.exception.UserProfileNotFoundException;
import com.rafaelhosaka.shareme.message.Message;
import com.rafaelhosaka.shareme.user.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ChatController {


    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private UserProfileService userProfileService;

    @MessageMapping("/change-status")
    @SendTo("/chat/status")
    public ChatStatus receiveMessage(@Payload ChatStatus newStatus){
        try {
            userProfileService.changeStatusById(newStatus.getId(), newStatus.isOnline());
        } catch (UserProfileNotFoundException e) {
            e.printStackTrace();
        }
        return newStatus;
    }

    @MessageMapping("/message")
    public Message recMessage(@Payload Message message){
        simpMessagingTemplate.convertAndSendToUser(message.getReceiver().getId(),"/private",message);
        return message;
    }
}
