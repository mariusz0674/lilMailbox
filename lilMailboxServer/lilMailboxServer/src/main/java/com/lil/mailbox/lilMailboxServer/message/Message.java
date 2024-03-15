package com.lil.mailbox.lilMailboxServer.message;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class Message {
    private UUID fromUser;
    private UUID toUser;
    private String content;
    private String title;
}
