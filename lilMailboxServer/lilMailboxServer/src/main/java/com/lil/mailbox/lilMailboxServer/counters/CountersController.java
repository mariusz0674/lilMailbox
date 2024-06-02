package com.lil.mailbox.lilMailboxServer.counters;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@RequestMapping("/api/v1/counters")
@RestController
public class CountersController {

    private final UserUnreadCounterService userUnreadCounterService;

    @GetMapping("/user")
    public Map<String, String> getUserCounter(@RequestParam String userId) {
        return userUnreadCounterService.getUserUnreadCounters(UUID.fromString(userId));
    }

}
