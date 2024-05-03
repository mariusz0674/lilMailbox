package com.lil.mailbox.lilMailboxServer.message;

import com.lil.mailbox.lilMailboxServer.datasource.MessageDAO;
import com.lil.mailbox.lilMailboxServer.datasource.models.MessageFolder;
import com.lil.mailbox.lilMailboxServer.minio.MinioService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageDAO messageDao;
    private final MinioService minioService;

    @Override
    public Message getMessageFolderById(UUID id) {
        MessageFolder messageFolder = this.messageDao.getMessageById(id);
        return Message.builder()
                .fromUser(messageFolder.getFromUser())
                .toUser(messageFolder.getToUser())
                .title(messageFolder.getTitle())
                .content(minioService.getContent(messageFolder.getS3Key()))
                .build();
    }

    @Override
    public List<Message> getUserAllInboxMessages(UUID userId) {
        return this.messageDao.getAllMessagesByFromUserId(userId).stream().map(messageFolder -> Message.builder()
                .fromUser(messageFolder.getFromUser())
                .toUser(messageFolder.getToUser())
                .title(messageFolder.getTitle())
                .content(minioService.getContent(messageFolder.getS3Key()))
                .build()).toList();
    }

    @Override
    public List<Message> getUserAllSentMessages(UUID userId) {
        return this.messageDao.getAllMessagesByTomUserId(userId).stream().map(messageFolder -> Message.builder()
                .fromUser(messageFolder.getFromUser())
                .toUser(messageFolder.getToUser())
                .title(messageFolder.getTitle())
                .content(minioService.getContent(messageFolder.getS3Key()))
                .build()).toList();
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
                .s3Key(minioService.putContent(message.getContent()))
                .title(message.getTitle())
                .build();
        this.messageDao.insertMessage(messageFolder);
    }

}
