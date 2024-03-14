package com.lil.mailbox.lilMailboxServer.message;

import com.lil.mailbox.lilMailboxServer.datasource.models.MessageFolder;

import java.util.UUID;

public interface MessageService {

    MessageFolder getMessageFolderById(UUID id);

    void sendMessage(Message message);
}
