package com.rafaelhosaka.shareme.chat;

import com.rafaelhosaka.shareme.message.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ChatRepository extends MongoRepository<Chat, String> {
    @Query("{'owner.id': ?0}")
    List<Chat> getChatByUserId(String userId);

    @Query("{'$and':[ {'owner.id': ?0}, {'friend.id': ?1} ] }")
    Chat getChatByIds(String ownerId, String friendId);
}
