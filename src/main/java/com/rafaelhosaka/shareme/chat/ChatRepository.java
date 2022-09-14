package com.rafaelhosaka.shareme.chat;

import com.rafaelhosaka.shareme.message.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ChatRepository extends MongoRepository<Chat, String> {
    @Query("{'$or':[ {'firstUser.id': ?0}, {'secondUser.id': ?0} ] }")
    List<Chat> getChatByUserId(String userId);

    @Query("{'$and':[ {'firstUser.id': ?0}, {'secondUser.id': ?1} ] }")
    Chat getChatByIds(String id1, String id2);
}
