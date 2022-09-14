package com.rafaelhosaka.shareme.message;

import com.rafaelhosaka.shareme.chat.Chat;
import com.rafaelhosaka.shareme.chat.ChatRepository;
import com.rafaelhosaka.shareme.exception.UserProfileNotFoundException;
import com.rafaelhosaka.shareme.user.UserProfile;
import com.rafaelhosaka.shareme.user.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final UserProfileService userService;
    private final ChatRepository chatRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, UserProfileService userService, ChatRepository chatRepository){
        this.messageRepository = messageRepository;
        this.userService = userService;
        this.chatRepository = chatRepository;
    }

    public Message sendMessage(String senderId, String receiverId, String content) throws UserProfileNotFoundException {
        UserProfile sender = userService.getUserProfileById(senderId);
        UserProfile receiver = userService.getUserProfileById(receiverId);
        Message message = new Message();
        message.setDateSent(LocalDateTime.now());
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(content);
        message = messageRepository.save(message);

        Chat chat = chatRepository.getChatByIds(senderId, receiverId);
        if(chat == null) {
            chat = chatRepository.getChatByIds(receiverId, senderId);

            if(chat == null) {
                chat = new Chat();
                chat.setFirstUser(sender);
                chat.setSecondUser(receiver);
            }
        }
        chat.setLastMessage(message);
        chatRepository.save(chat);
        return message;
    }

    public List<Message> getMessages(String senderId, String receiverId) {
        List<Message> messages = new ArrayList<>();
        List<Message> sentMessages = messageRepository.getMessages(senderId, receiverId);
        List<Message> receivedMessages = messageRepository.getMessages(receiverId, senderId);
        messages.addAll(sentMessages);
        messages.addAll(receivedMessages);
        Collections.sort(messages);
        return messages;
    }
}
