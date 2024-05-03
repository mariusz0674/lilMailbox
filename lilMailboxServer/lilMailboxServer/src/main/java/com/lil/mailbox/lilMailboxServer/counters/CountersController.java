package com.lil.mailbox.lilMailboxServer.counters;

import com.lil.mailbox.lilMailboxServer.datasource.redis.UserUnreadCounter;
import com.lil.mailbox.lilMailboxServer.datasource.redis.UserUnreadCounterRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@AllArgsConstructor
@RequestMapping("/api/v1/counters")
@RestController
public class CountersController {

    private final UserUnreadCounterRepository userUnreadCounterRepository;

    @GetMapping("/all")
    public List<UserUnreadCounter> getAllCounters() {
        Iterable<UserUnreadCounter> allCounters = userUnreadCounterRepository.findAll();
        return StreamSupport.stream(allCounters.spliterator(), false)
                .collect(Collectors.toList());
    }

    @GetMapping("/user")
    public ResponseEntity<UserUnreadCounter> getUserCounter(@RequestParam String userId) {
        Optional<UserUnreadCounter> userCounter = userUnreadCounterRepository.findById(userId);
        return userCounter
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
