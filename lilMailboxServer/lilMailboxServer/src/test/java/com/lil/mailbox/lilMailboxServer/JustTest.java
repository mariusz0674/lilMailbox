package com.lil.mailbox.lilMailboxServer;

import com.lil.mailbox.lilMailboxServer.datasource.models.MessageFolder;
import com.lil.mailbox.lilMailboxServer.message.MessageServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
public class JustTest {

    @Autowired
    private MessageServiceImpl messageService;

    @Test
    void simpleSelectTest(){

        UUID messageId = new UUID(0,1);
        log.info(messageId.toString());
        MessageFolder article = messageService.getMessageFolderById(messageId);
        System.out.println(article);

        Long val = 1L;
        assertThat(val).isEqualTo(1L);

    }

}
