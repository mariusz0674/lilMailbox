package com.lil.mailbox.lilMailboxServer.message;

import com.lil.mailbox.lilMailboxServer.datasource.MessageDAO;
import com.lil.mailbox.lilMailboxServer.datasource.models.MessageFolder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageDAO messageDao;

    @Override
    public MessageFolder getMessageFolderById(UUID id) {
        return this.messageDao.getMessageById(id);
    }

    @Override
    public void sendMessage(Message message) {

        insertMessageFolder(message);
    }

    private void insertMessageFolder(Message message) {
        MessageFolder messageFolder = MessageFolder.builder()
                .id(UUID.randomUUID())
                .fromUser(message.getFromUser())
                .toUser(message.getToUser())
                .s3Key(message.getContent())
                .title(message.getTitle())
                .build();
        this.messageDao.insertMessage(messageFolder);
    }

}
