package com.rafaelhosaka.shareme.websocket;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ChatStatus {
    private String id;
    private boolean online;
    private boolean connected;
}
