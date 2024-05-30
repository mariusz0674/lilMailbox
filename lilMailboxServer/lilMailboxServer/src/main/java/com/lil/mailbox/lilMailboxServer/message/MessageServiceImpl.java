package com.lil.mailbox.lilMailboxServer.message;

import com.lil.mailbox.lilMailboxServer.counters.UserUnreadCounterService;
import com.lil.mailbox.lilMailboxServer.datasource.MessageDAO;
import com.lil.mailbox.lilMailboxServer.datasource.minio.MinioService;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.bson.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final UserUnreadCounterService userUnreadCounterService;
    private final MongoTemplate mongoTemplate;

    @Override
    public Document getMessageById(String id) {
        Query query = new Query(Criteria.where("messageId").is(id));
        Document message = mongoTemplate.findOne(query, Document.class, "messages");

        if (message != null && message.containsKey("read") && !message.getBoolean("read")) {
            userUnreadCounterService.decrementUnreadCount(UUID.fromString(message.getString("toUser")));
            Update update = new Update().set("read", true);
            mongoTemplate.updateFirst(query, update, "messages");
        }
        return message;
    }

    @Override
    public List<Document> getUserAllInboxMessages(String userId) {
        Query query = new Query(Criteria.where("toUser").is(userId));
        return mongoTemplate.find(query, Document.class, "messages");
    }


    @Override
    public List<Document> getUserAllSentMessages(String userId) {
        Query query = new Query(Criteria.where("fromUser").is(userId));
        return mongoTemplate.find(query, Document.class, "messages");
    }

    @Override
    public void sendMessage(Document message) {

        userUnreadCounterService.incrementUnreadCount(UUID.fromString(message.getString("toUser")));

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
            reply.put("date", new Date());
            replies.add(reply);
            Update update = new Update().set("replies", replies);
            mongoTemplate.updateFirst(query, update, "messages");
        }
    }

}
