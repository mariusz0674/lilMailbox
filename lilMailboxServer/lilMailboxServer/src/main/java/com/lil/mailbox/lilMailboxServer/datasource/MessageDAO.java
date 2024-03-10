package com.lil.mailbox.lilMailboxServer.datasource;

import com.lil.mailbox.lilMailboxServer.datasource.models.Message;
import com.lil.mailbox.lilMailboxServer.datasource.postgres.MessageMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class MessageDAO {

    private final MessageMapper messageMapper;

    public Message getMessageById(UUID id){
     return messageMapper.getMessageById(id);
    }


}
