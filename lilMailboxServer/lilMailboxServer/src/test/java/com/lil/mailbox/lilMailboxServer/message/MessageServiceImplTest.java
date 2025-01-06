package com.lil.mailbox.lilMailboxServer.message;

import com.lil.mailbox.lilMailboxServer.counters.UserUnreadCounterService;
import com.lil.mailbox.lilMailboxServer.datasource.models.User;
import com.lil.mailbox.lilMailboxServer.exceptions.MessageNotExistException;
import com.lil.mailbox.lilMailboxServer.exceptions.UserNotExistException;
import com.lil.mailbox.lilMailboxServer.user.UserServiceImpl;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MessageServiceImplTest {

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private UserUnreadCounterService userUnreadCounterService;

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private MessageServiceImpl messageService;

    public MessageServiceImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getMessageById_shouldMarkMessageAsReadAndDecrementUnreadCounter() {
        // given
        String messageId = "12345";
        String fromUser = UUID.randomUUID().toString();
        String toUser = UUID.randomUUID().toString();

        Document mockMessage = new Document("messageId", messageId)
                .append("read", false)
                .append("fromUser", fromUser)
                .append("toUser", toUser);

        when(mongoTemplate.findOne(any(Query.class), eq(Document.class), eq("messages"))).thenReturn(mockMessage);

        // when
        Document result = messageService.getMessageById(messageId);

        // then
        assertNotNull(result);
        assertEquals(messageId, result.getString("messageId"));

        verify(userUnreadCounterService).decrementUnreadOnRead(UUID.fromString(fromUser), UUID.fromString(toUser));

        ArgumentCaptor<Update> updateCaptor = ArgumentCaptor.forClass(Update.class);
        verify(mongoTemplate).updateFirst(any(Query.class), updateCaptor.capture(), eq("messages"));
        assertTrue(updateCaptor.getValue().getUpdateObject().containsKey("$set"));
        assertEquals(true, updateCaptor.getValue().getUpdateObject().get("$set", Document.class).get("read"));
    }

    @Test
    void getMessageById_shouldNotUpdateOrDecrementWhenMessageIsAlreadyRead() {
        // given
        String messageId = "12345";
        Document mockMessage = new Document("messageId", messageId)
                .append("read", true);

        when(mongoTemplate.findOne(any(Query.class), eq(Document.class), eq("messages"))).thenReturn(mockMessage);

        // when
        Document result = messageService.getMessageById(messageId);

        // then
        assertNotNull(result);
        assertEquals(messageId, result.getString("messageId"));

        verify(userUnreadCounterService, never()).decrementUnreadOnRead(any(UUID.class), any(UUID.class));

        verify(mongoTemplate, never()).updateFirst(any(Query.class), any(Update.class), eq("messages"));
    }

    @Test
    void getMessageById_shouldThrowExceptionWhenMessageNotFound() {
        // given
        String messageId = "12345";
        // when
        when(mongoTemplate.findOne(any(Query.class), eq(Document.class), eq("messages"))).thenReturn(null);

        // then
        MessageNotExistException exception = assertThrows(MessageNotExistException.class, () -> {
            messageService.getMessageById(messageId);
        });

        assertEquals("Message not exist: " + messageId, exception.getMessage());
        verifyNoInteractions(userUnreadCounterService);
        verify(mongoTemplate, never()).updateFirst(any(Query.class), any(Update.class), eq("messages"));
    }



    @Test
    void getUserAllInboxMessages_shouldReturnMessagesWithFromUserNames() {
        // given
        String userId = UUID.randomUUID().toString();
        UUID fromUserUUID = UUID.randomUUID();
        List<Document> mockMessages = List.of(
                new Document("messageId", "1").append("fromUser", fromUserUUID.toString())
        );

        when(userService.getUserById(eq(UUID.fromString(userId)))).thenReturn(new User(UUID.fromString(userId), "inboxUser"));
        when(mongoTemplate.find(any(Query.class), eq(Document.class), eq("messages"))).thenReturn(mockMessages);
        when(userService.getUserById(eq(fromUserUUID))).thenReturn(new User(fromUserUUID, "testUser"));

        // when
        List<Document> result = messageService.getUserAllInboxMessages(userId);

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("testUser", result.get(0).getString("fromUserName"));
    }

    @Test
    void getUserAllInboxMessages_shouldThrowExceptionWhenUserDoesNotExist() {
        // given
        String userId = UUID.randomUUID().toString();
        when(userService.getUserById(UUID.fromString(userId))).thenReturn(null);

        // when / then
        UserNotExistException exception = assertThrows(UserNotExistException.class, () -> {
            messageService.getUserAllInboxMessages(userId);
        });

        assertEquals("User not exist: " + userId, exception.getMessage());
    }

    @Test
    void getUserAllSentMessages_shouldReturnMessagesWithToUserNames() {
        // given
        String userId = UUID.randomUUID().toString();
        UUID toUserUUID = UUID.randomUUID();
        List<Document> mockMessages = List.of(
                new Document("messageId", "1").append("toUser", toUserUUID.toString())
        );

        when(mongoTemplate.find(any(Query.class), eq(Document.class), eq("messages"))).thenReturn(mockMessages);
        when(userService.getUserById(eq(toUserUUID))).thenReturn(new User(toUserUUID, "testUser"));
        when(userService.getUserById(eq(UUID.fromString(userId)))).thenReturn(new User(UUID.fromString(userId), "user"));

        // when
        List<Document> result = messageService.getUserAllSentMessages(userId);

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("testUser", result.get(0).getString("toUserName"));
    }

    @Test
    void sendMessage_shouldIncrementUnreadCountersAndSaveMessage() {
        // given
        String fromUser = UUID.randomUUID().toString();
        String toUser = UUID.randomUUID().toString();
        Document message = new Document("fromUser", fromUser).append("toUser", toUser);

        ArgumentCaptor<Document> messageCaptor = ArgumentCaptor.forClass(Document.class);
        when(userService.getUserById(any())).thenReturn(new User(UUID.fromString(toUser), "testUser"));

        // when
        messageService.sendMessage(message);

        // then
        verify(userUnreadCounterService).incrementUnreadOnSend(UUID.fromString(fromUser), UUID.fromString(toUser));
        verify(mongoTemplate).insert(messageCaptor.capture(), eq("messages"));

        Document capturedMessage = messageCaptor.getValue();
        assertNotNull(capturedMessage.getString("messageId"));
        assertFalse(capturedMessage.getBoolean("read"));
    }


    @Test
    void replyMessage_shouldAddReplyToExistingMessage() {
        // given
        String messageId = "12345";
        Document existingMessage = new Document("messageId", messageId).append("replies", new ArrayList<>());
        Document reply = new Document("content", "test reply");

        when(mongoTemplate.findOne(any(Query.class), eq(Document.class), eq("messages"))).thenReturn(existingMessage);

        // when
        messageService.replyMessage(messageId, reply);

        // then
        ArgumentCaptor<Update> updateCaptor = ArgumentCaptor.forClass(Update.class);
        verify(mongoTemplate).updateFirst(any(Query.class), updateCaptor.capture(), eq("messages"));

        Update capturedUpdate = updateCaptor.getValue();
        assertNotNull(capturedUpdate.getUpdateObject().get("$set", Document.class).get("replies"));
    }

    @Test
    void searchMessages_shouldReturnMatchingMessages() {
        // given
        String searchQuery = "test";
        List<Document> mockMessages = List.of(new Document("messageId", "1"));

        when(mongoTemplate.find(any(Query.class), eq(Document.class), eq("messages"))).thenReturn(mockMessages);

        // when
        List<Document> result = messageService.searchMessages(searchQuery);

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("1", result.get(0).getString("messageId"));
    }

    @Test
    void deleteMessage_shouldThrowExceptionWhenMessageDoesNotExist() {
        // given
        String messageId = "12345";

        when(mongoTemplate.findOne(any(Query.class), eq(Document.class), eq("messages"))).thenReturn(null);

        // when / then
        MessageNotExistException exception = assertThrows(MessageNotExistException.class, () -> {
            messageService.deleteMessage(messageId);
        });

        assertEquals("Message not exist: " + messageId, exception.getMessage());
    }

}