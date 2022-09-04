package com.rafaelhosaka.shareme.notification;

import com.rafaelhosaka.shareme.exception.NotificationNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/notification")
public class NotificationController {
    private NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService){
        this.notificationService = notificationService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<Notification>> getNotificationsByUserId(@PathVariable("id")String id){
        return ResponseEntity.ok().body(notificationService.getNotificationsByUserId(id));
    }

    @PutMapping("/markAsRead")
    public ResponseEntity<Notification> markAsRead(@RequestBody Object notificationId){
        try {
            return ResponseEntity.ok().body(notificationService.markAsRead((String)notificationId));
        } catch (NotificationNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
