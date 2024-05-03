package com.lil.mailbox.lilMailboxServer.datasource;

import com.lil.mailbox.lilMailboxServer.datasource.models.MessageFolder;
import com.lil.mailbox.lilMailboxServer.datasource.postgres.MessageMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class MessageDAO {

    private final MessageMapper messageMapper;

    public MessageFolder getMessageById(UUID id) {
        return messageMapper.getMessageById(id);
    }

    public List<MessageFolder> getAllMessagesByFromUserId(UUID id) {
        return messageMapper.getMessagesByFromUserId(id);
    }

    public List<MessageFolder> getAllMessagesByTomUserId(UUID id) {
        return messageMapper.getMessagesByToUserId(id);
    }

    public void insertMessage(MessageFolder message) {
        messageMapper.insertMessage(message);
    }

    public void makAsRead(UUID messageId) {
        messageMapper.markAsRead(messageId);
    }

}
