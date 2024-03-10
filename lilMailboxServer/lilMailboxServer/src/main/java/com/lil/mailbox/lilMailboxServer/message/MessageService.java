package com.lil.mailbox.lilMailboxServer.message;

import com.lil.mailbox.lilMailboxServer.datasource.models.Message;

import java.util.UUID;

public interface MessageService {

    Message getMessageById(UUID id);
}
