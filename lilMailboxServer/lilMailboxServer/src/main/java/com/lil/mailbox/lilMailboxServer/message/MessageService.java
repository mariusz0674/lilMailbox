package com.lil.mailbox.lilMailboxServer.message;

import com.lil.mailbox.lilMailboxServer.datasource.models.MessageFolder;

import java.util.List;
import java.util.UUID;

public interface MessageService {

    Message getMessageFolderById(UUID id);

    List<MessageFolder> getUserAllInboxMessages(UUID userId);

    List<MessageFolder> getUserAllSentMessages(UUID userId);

    void sendMessage(Message message);

    Message getMessage(UUID messageId);

}


