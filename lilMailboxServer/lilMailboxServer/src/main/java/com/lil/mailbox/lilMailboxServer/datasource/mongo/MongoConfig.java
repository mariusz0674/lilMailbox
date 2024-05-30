package com.lil.mailbox.lilMailboxServer.datasource.mongo;

import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;

@Configuration
public class MongoConfig {

    @Bean
    public MongoTemplate mongoTemplate() {
        MongoTemplate mongoTemplate = new MongoTemplate(MongoClients.create("mongodb://localhost:27017"), "mailbox");
        mongoTemplate.indexOps("messages").ensureIndex(new Index().on("messageId", Sort.Direction.ASC).unique());
        mongoTemplate.indexOps("messages").ensureIndex(new Index().on("fromUser", Sort.Direction.ASC));
        mongoTemplate.indexOps("messages").ensureIndex(new Index().on("toUser", Sort.Direction.ASC));
        return mongoTemplate;    }
}
