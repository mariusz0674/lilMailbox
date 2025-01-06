package com.lil.mailbox.lilMailboxServer.datasource.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
@Data
public class User {
    UUID id;
    String username;
}
