package com.rafaelhosaka.shareme.message;

import com.rafaelhosaka.shareme.post.BasePost;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface MessageRepository extends MongoRepository<Message,String> {
    @Query("{ 'sender.id' : ?0 , 'receiver.id' : ?1}")
    List<Message> getMessages(String senderId, String receiverId);
}
