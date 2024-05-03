package com.lil.mailbox.lilMailboxServer.datasource.redis;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.UUID;

@Data
@Builder
@RedisHash("UserUnreadCounter")
public class UserUnreadCounter {

    @Id
    UUID userId;
    private int unreadCount;
}
