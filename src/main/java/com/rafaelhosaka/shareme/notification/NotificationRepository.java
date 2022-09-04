package com.rafaelhosaka.shareme.notification;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface NotificationRepository extends MongoRepository<Notification, String> {

    @Query("{ 'ownerUserId' : ?0}")
    List<Notification> getByUserId(String id);
}
