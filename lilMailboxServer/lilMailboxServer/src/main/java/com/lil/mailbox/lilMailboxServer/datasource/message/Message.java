package com.lil.mailbox.lilMailboxServer.datasource.message;

import lombok.Data;

@Data
public class Message {
    private Long id;
    private String title;
    private String author;
}
