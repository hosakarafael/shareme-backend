package com.rafaelhosaka.shareme.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {
    private ChatRepository chatRepository;

    @Autowired
    public ChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    public List<Chat> getChatByUserId(String id) {
        return chatRepository.getChatByUserId(id);
    }

    public Chat getChatByIds(String id1, String id2){
        Chat chat = chatRepository.getChatByIds(id1, id2);
        if(chat == null){
            chat = chatRepository.getChatByIds(id2,id1);
        }
        return chat;
    }
}
