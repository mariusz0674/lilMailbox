package com.lil.mailbox.lilMailboxServer.message;

import com.lil.mailbox.lilMailboxServer.counters.UserUnreadCounterService;
import com.lil.mailbox.lilMailboxServer.datasource.MessageDAO;
import com.lil.mailbox.lilMailboxServer.datasource.models.MessageFolder;
import com.lil.mailbox.lilMailboxServer.datasource.minio.MinioService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageDAO messageDao;
    private final MinioService minioService;
    private final UserUnreadCounterService userUnreadCounterService;

    @Override
    public Message getMessageFolderById(UUID id) {
        MessageFolder messageFolder = this.messageDao.getMessageById(id);
        return Message.builder()
                .fromUser(messageFolder.getFromUser())
                .toUser(messageFolder.getToUser())
                .title(messageFolder.getTitle())
                .content(minioService.getContent(messageFolder.getS3Key()))
                .read(messageFolder.isRead())
                .build();
    }

    @Override
    public List<MessageFolder> getUserAllInboxMessages(UUID userId) {
        return this.messageDao.getAllMessagesByTomUserId(userId);
    }

    @Override
    public List<MessageFolder> getUserAllSentMessages(UUID userId) {
        return this.messageDao.getAllMessagesByFromUserId(userId);
    }

    @Override
    public void sendMessage(Message message) {
        insertMessageFolder(message);
        userUnreadCounterService.incrementUnreadCount(message.getToUser());
    }

    @Override
    public Message getMessage(UUID messageId) {
        messageDao.makAsRead(messageId);
        MessageFolder messageF = messageDao.getMessageById(messageId);
        userUnreadCounterService.decrementUnreadCount(messageF.getToUser());
        return Message.builder()
                .fromUser(messageF.getFromUser())
                .toUser(messageF.getToUser())
                .title(messageF.getTitle())
                .content(minioService.getContent(messageF.getS3Key()))
                .read(messageF.isRead())
                .build();
    }

    private void insertMessageFolder(Message message) {
        MessageFolder messageFolder = MessageFolder.builder()
                .id(UUID.randomUUID())
                .fromUser(message.getFromUser())
                .toUser(message.getToUser())
                .s3Key(minioService.putContent(message.getContent()))
                .title(message.getTitle())
                .read(false)
                .build();
        this.messageDao.insertMessage(messageFolder);
    }

}
