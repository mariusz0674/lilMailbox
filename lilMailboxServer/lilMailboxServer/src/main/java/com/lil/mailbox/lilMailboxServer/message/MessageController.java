package com.lil.mailbox.lilMailboxServer.message;

import com.lil.mailbox.lilMailboxServer.datasource.models.MessageFolder;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RequestMapping("/api/v1/message")
@RestController
public class MessageController {

    private final MessageService messageService;

    @GetMapping("/userInbox")
    public List<MessageFolder> getUserAllInboxMessages(@RequestParam String userId) {
        return messageService.getUserAllInboxMessages(UUID.fromString(userId));
    }

    @GetMapping("/userSent")
    public List<MessageFolder> getAllUserMessages(@RequestParam String userId) {
        return messageService.getUserAllSentMessages(UUID.fromString(userId));
    }

    @PostMapping("/send")
    public void sendMessage(@RequestBody Message message) {
        messageService.sendMessage(message);
    }

    @GetMapping("/message")
    public Message getMessage(@RequestParam String messageId) {
        return messageService.getMessage(UUID.fromString(messageId));
    }


}
