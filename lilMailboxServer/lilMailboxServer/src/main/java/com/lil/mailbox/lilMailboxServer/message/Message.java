package com.lil.mailbox.lilMailboxServer.message;

import lombok.Data;

import java.util.UUID;

@Data
public class Message {
    private UUID fromUser;
    private UUID toUser;
    private String content;
    private String title;
}
