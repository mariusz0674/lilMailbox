package com.lil.mailbox.lilMailboxServer.datasource.models;

import lombok.Data;

import java.util.UUID;

@Data
public class User {
    UUID id;
    String username;
}
