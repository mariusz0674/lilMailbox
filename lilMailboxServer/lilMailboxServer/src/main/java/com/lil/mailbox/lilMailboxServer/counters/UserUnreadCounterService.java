package com.lil.mailbox.lilMailboxServer.counters;

import lombok.AllArgsConstructor;
import org.bson.Document;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserUnreadCounterService {

    private final StringRedisTemplate stringRedisTemplate;
    private final RedisTemplate<String, Object> redisTemplate;
    private final MongoTemplate mongoTemplate;

    public void decrementUnreadOnRead(UUID fromUser, UUID toUser) {
        String fromUserKey = "UserUnreadCounter:" + fromUser.toString();
        String toUserKey = "UserUnreadCounter:" + toUser.toString();

        // Use SessionCallback to execute operations in a transaction
        redisTemplate.execute(new SessionCallback<Void>() {
            @Override
            public Void execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                HashOperations<String, String, String> hashOps = operations.opsForHash();
                hashOps.increment(toUserKey, "unreadInbox", -1);
                hashOps.increment(fromUserKey, "unreadSent", -1);
                operations.exec();
                return null;
            }
        });
    }

    public void incrementUnreadOnSend(UUID fromUser, UUID toUser) {
        String fromUserKey = "UserUnreadCounter:" + fromUser.toString();
        String toUserKey = "UserUnreadCounter:" + toUser.toString();

        redisTemplate.execute(new SessionCallback<Void>() {
            @Override
            public Void execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                HashOperations<String, String, String> hashOps = operations.opsForHash();
                hashOps.increment(toUserKey, "unreadInbox", 1);
                hashOps.increment(fromUserKey, "unreadSent", 1);
                operations.exec();
                return null;
            }
        });
    }

    public Map<String, Integer> getAggregateUnreadInboxByUser() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("read").is(false)),
                Aggregation.group("toUser").count().as("unreadInbox"),
                Aggregation.project("unreadInbox").and("_id").as("toUser")
        );

        AggregationResults<Document> results = mongoTemplate.aggregate(aggregation, "messages", Document.class);
        List<Document> mappedResults = results.getMappedResults();

        return mappedResults.stream().collect(
                Collectors.toMap(
                        doc -> doc.getString("toUser"),
                        doc -> doc.getInteger("unreadInbox")
                )
        );
    }

    public Map<String, Integer> getAggregateUnreadSentByUser() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("read").is(false)),
                Aggregation.group("fromUser").count().as("unreadSent"),
                Aggregation.project("unreadSent").and("_id").as("fromUser")
        );

        AggregationResults<Document> results = mongoTemplate.aggregate(aggregation, "messages", Document.class);
        List<Document> mappedResults = results.getMappedResults();

        return mappedResults.stream().collect(
                Collectors.toMap(
                        doc -> doc.getString("fromUser"),
                        doc -> doc.getInteger("unreadSent")
                )
        );
    }

    public void cacheMessageCounts() {
        Map<String, Integer> unreadInboxData = getAggregateUnreadInboxByUser();
        Map<String, Integer> unreadSentData = getAggregateUnreadSentByUser();

        for (Map.Entry<String, Integer> entry : unreadInboxData.entrySet()) {
            String toUser = entry.getKey();
            Integer unreadInbox = entry.getValue();
            redisTemplate.opsForHash().put("UserUnreadCounter:" + toUser, "unreadInbox", unreadInbox.toString());
        }

        for (Map.Entry<String, Integer> entry : unreadSentData.entrySet()) {
            String fromUser = entry.getKey();
            Integer unreadSent = entry.getValue();
            redisTemplate.opsForHash().put("UserUnreadCounter:" + fromUser, "unreadSent", unreadSent.toString());
        }
    }

    public Map<String, String> getUserUnreadCounters(UUID userId) {
        HashOperations<String, String, String> hashOps = stringRedisTemplate.opsForHash();
        String key = "UserUnreadCounter:" + userId.toString();
        return hashOps.entries(key);
    }
}
