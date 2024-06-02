package com.lil.mailbox.lilMailboxServer.datasource.mongo;

import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.TextIndexDefinition;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class MongoConfig {

    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    @Bean
    public MongoTemplate mongoTemplate() {
        MongoTemplate mongoTemplate = new MongoTemplate(MongoClients.create(mongoUri), "mailbox");
        mongoTemplate.indexOps("messages").ensureIndex(new Index().on("messageId", Sort.Direction.ASC).unique());
        mongoTemplate.indexOps("messages").ensureIndex(new Index().on("fromUser", Sort.Direction.ASC));
        mongoTemplate.indexOps("messages").ensureIndex(new Index().on("toUser", Sort.Direction.ASC));

        TextIndexDefinition textIndex = new TextIndexDefinition.TextIndexDefinitionBuilder()
                .onField("content")
                .onField("title")
                .build();
        mongoTemplate.indexOps("messages").ensureIndex(textIndex);

        return mongoTemplate;
    }
}
