package com.lil.mailbox.lilMailboxServer.counters;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@AllArgsConstructor
@Service
public class UserUnreadCounterService {

    private final StringRedisTemplate stringRedisTemplate;

    public void incrementUnreadCount(UUID userId) {
        HashOperations<String, Object, Object> hashOps = stringRedisTemplate.opsForHash();
        String key = "UserUnreadCounter:" + userId.toString();
        hashOps.increment(key, "unreadCount", 1);
    }

    public void decrementUnreadCount(UUID userId) {
        HashOperations<String, Object, Object> hashOps = stringRedisTemplate.opsForHash();
        String key = "UserUnreadCounter:" + userId.toString();
        hashOps.increment(key, "unreadCount", -1);
    }

}
