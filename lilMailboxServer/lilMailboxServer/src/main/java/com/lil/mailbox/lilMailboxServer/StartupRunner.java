package com.lil.mailbox.lilMailboxServer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lil.mailbox.lilMailboxServer.counters.UserUnreadCounterService;
import com.lil.mailbox.lilMailboxServer.message.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.bson.Document;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Component
public class StartupRunner implements CommandLineRunner {

    private final UserUnreadCounterService userUnreadCounterService;
    private final MessageService messageService;

    @Override
    public void run(String... args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<Map<String, Object>>> typeReference = new TypeReference<>() {};
        InputStream inputStream = new ClassPathResource("mongo-data.json").getInputStream();
        List<Map<String, Object>> messages = mapper.readValue(inputStream, typeReference);

        for (Map<String, Object> messageData : messages) {
            Document message = new Document(messageData);
            messageService.sendMessage(message);
        }

        this.userUnreadCounterService.cacheMessageCounts();
    }

}
