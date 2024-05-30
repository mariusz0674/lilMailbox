package com.lil.mailbox.lilMailboxServer.message;

import org.bson.Document;

import java.util.List;

public interface MessageService {

    Document getMessageById(String id);

    List<Document> getUserAllInboxMessages(String userId);

    List<Document> getUserAllSentMessages(String userId);

    void sendMessage(Document message);

    void replyMessage(String messageId, Document message);

}


