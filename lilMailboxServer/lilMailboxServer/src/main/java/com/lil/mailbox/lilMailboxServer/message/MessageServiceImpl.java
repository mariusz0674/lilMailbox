package com.lil.mailbox.lilMailboxServer.message;

import com.lil.mailbox.lilMailboxServer.counters.UserUnreadCounterService;
import com.lil.mailbox.lilMailboxServer.user.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.bson.Document;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final UserUnreadCounterService userUnreadCounterService;
    private final MongoTemplate mongoTemplate;
    private final UserServiceImpl userService;

    @Override
    public Document getMessageById(String id) {
        Query query = new Query(Criteria.where("messageId").is(id));
        Document message = mongoTemplate.findOne(query, Document.class, "messages");

        if (message != null && message.containsKey("read") && !message.getBoolean("read")) {
            userUnreadCounterService.decrementUnreadOnRead(UUID.fromString(message.getString("fromUser")), UUID.fromString(message.getString("toUser")));
            Update update = new Update().set("read", true);
            mongoTemplate.updateFirst(query, update, "messages");
        }
        return message;
    }

    @Override
    public List<Document> getUserAllInboxMessages(String userId) {
        Query query = new Query(Criteria.where("toUser").is(userId));
        List<Document> messages = mongoTemplate.find(query, Document.class, "messages");
        for (Document message : messages) {
            message.put(
                    "fromUserName",
                    this.userService.getUserById(
                            UUID.fromString(message.getString("fromUser"))
                    ).getUsername()
            );
        }
        return messages;
    }


    @Override
    public List<Document> getUserAllSentMessages(String userId) {
        Query query = new Query(Criteria.where("fromUser").is(userId));
        List<Document> messages = mongoTemplate.find(query, Document.class, "messages");
        for (Document message : messages) {
            message.put(
                    "toUserName",
                    this.userService.getUserById(
                            UUID.fromString(message.getString("toUser"))
                    ).getUsername()
            );
        }
        return messages;
    }

    @Override
    public void sendMessage(Document message) {

        userUnreadCounterService.incrementUnreadOnSend(UUID.fromString(message.getString("fromUser")), UUID.fromString(message.getString("toUser")));

        UUID messageId = UUID.randomUUID();
        message.put("messageId", messageId.toString());
        message.put("read", false);
        mongoTemplate.insert(message, "messages");
    }

    @Override
    public void replyMessage(String messageId, Document reply) {
        Query query = new Query(Criteria.where("messageId").is(messageId));
        Document message = mongoTemplate.findOne(query, Document.class, "messages");

        if (message != null) {
            List<Document> replies = (List<Document>) message.get("replies");
            if (replies == null) {
                replies = new ArrayList<>();
            }
            reply.put("date", new Date());
            replies.add(reply);
            Update update = new Update().set("replies", replies);
            mongoTemplate.updateFirst(query, update, "messages");
        }
    }

    @Override
    public List<Document> searchMessages(String searchQuery) {
        TextCriteria textCriteria = TextCriteria.forDefaultLanguage().matchingPhrase(searchQuery);
        Query query = TextQuery.queryText(textCriteria).sortByScore();
        return mongoTemplate.find(query, Document.class, "messages");
    }


}
