package com.lil.mailbox.lilMailboxServer;

import com.lil.mailbox.lilMailboxServer.datasource.message.Message;
import com.lil.mailbox.lilMailboxServer.datasource.message.MessageMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JustTest {

    @Autowired
    private MessageMapper messageMapper;

    @Test
    void simpleSelectTest(){

        Long articleId = 1L;
        Message article = messageMapper.getArticle(articleId);
        System.out.println(article);

        Long val = 1L;
        assertThat(val).isEqualTo(1L);

    }

}
