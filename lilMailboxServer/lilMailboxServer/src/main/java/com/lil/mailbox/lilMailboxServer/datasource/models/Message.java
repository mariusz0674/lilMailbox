package com.lil.mailbox.lilMailboxServer.datasource.models;

import lombok.Data;

import java.util.UUID;

@Data
public class Message {
    private UUID id;
    private String title;
    private String author;
}
