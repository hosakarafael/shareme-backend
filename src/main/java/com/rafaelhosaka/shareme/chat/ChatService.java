package com.rafaelhosaka.shareme.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ChatService {
    private ChatRepository chatRepository;

    @Autowired
    public ChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    public List<Chat> getChatByUserId(String id) {
        List<Chat> chats =  chatRepository.getChatByUserId(id);
        if(chats != null) {
            Collections.sort(chats, Collections.reverseOrder());
        }
        return chats;
    }

    public int markAsRead(String ownerId, String friendId) {
        Chat chat = chatRepository.getChatByIds(ownerId, friendId);
        if(chat != null) {
            chat.setRead(true);
            chatRepository.save(chat);
        }
        return unreadCount(ownerId);

    }

    public int unreadCount(String userId) {
        List<Chat> chats = chatRepository.getChatByUserId(userId);
        int unreadCount = 0;
        for (Chat chat : chats) {
            if(!chat.isRead()){
                unreadCount++;
            }
        }
        return unreadCount;
    }
}
