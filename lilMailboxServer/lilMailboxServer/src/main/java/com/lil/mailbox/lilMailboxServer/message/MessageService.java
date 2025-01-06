package com.lil.mailbox.lilMailboxServer.message;

import org.bson.Document;

import java.util.List;

public interface MessageService {

    Document getMessageById(String id);

    List<Document> getUserAllInboxMessages(String userId);

    List<Document> getUserAllSentMessages(String userId);

    void sendMessage(Document message);

    List<Document> searchMessages(String searchQuery);

    void replyMessage(String messageId, Document message);

    void deleteMessage(String messageId);
}


