package com.lil.mailbox.lilMailboxServer.message;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RequestMapping("/api/v1/message")
@RestController
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/userInbox")
    public List<Message> getUserAllInboxMessages(@RequestBody String userId) {
        return messageService.getUserAllInboxMessages(UUID.fromString(userId));
    }

    @PostMapping("/userSent")
    public List<Message> getAllUserMessages(@RequestBody String userId) {
        return messageService.getUserAllSentMessages(UUID.fromString(userId));
    }

    @PostMapping("/send")
    public void sendMessage(@RequestBody Message message) {
        messageService.sendMessage(message);
    }
}
