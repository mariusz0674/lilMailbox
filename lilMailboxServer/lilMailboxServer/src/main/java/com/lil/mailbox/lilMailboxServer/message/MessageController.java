package com.lil.mailbox.lilMailboxServer.message;

import lombok.AllArgsConstructor;
import org.bson.Document;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RequestMapping("/api/v1/message")
@RestController
public class MessageController {

    private final MessageService messageService;

    @GetMapping("/userInbox")
    public List<Document> getUserAllInboxMessages(@RequestParam String userId) {
        return messageService.getUserAllInboxMessages(userId);
    }


    @GetMapping("/userSent")
    public List<Document> getAllUserMessages(@RequestParam String userId) {
        return messageService.getUserAllSentMessages(userId);
    }

    @PostMapping(value = "/send", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void sendMessage(@RequestBody Document message) {
        messageService.sendMessage(message);
    }

    @GetMapping("/message")
    public Document getMessage(@RequestParam String messageId) {
        return messageService.getMessageById(messageId);
    }

//    @PostMapping("/replyToMessage")
//    public reply(@RequestParam String messageId, @RequestBody Document message) {
//        return messageService.replyMessage(messageId, message);
//    }


}
