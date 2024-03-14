package com.lil.mailbox.lilMailboxServer.datasource.models;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class MessageFolder {
    private UUID id;
    private UUID fromUser;
    private UUID toUser;
    private String s3Key;
    private String title;
}
