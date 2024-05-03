package com.lil.mailbox.lilMailboxServer;


import com.lil.mailbox.lilMailboxServer.datasource.UserDAO;
import com.lil.mailbox.lilMailboxServer.datasource.models.User;
import com.lil.mailbox.lilMailboxServer.datasource.postgres.MessageMapper;
import com.lil.mailbox.lilMailboxServer.datasource.redis.UserUnreadCounter;
import com.lil.mailbox.lilMailboxServer.datasource.redis.UserUnreadCounterRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
public class StartupRunner implements CommandLineRunner {

    private final UserUnreadCounterRepository userUnreadCounterRepository;
    private final UserDAO userDAO;
    private final MessageMapper messageMapper;


    @Override
    public void run(String... args) throws Exception {
        List<User> allUsers = this.userDAO.getAllUsers();

        allUsers.forEach(user -> {
            userUnreadCounterRepository.save(UserUnreadCounter.builder()
                    .userId(user.getId())
                    .unreadCount(messageMapper.countUnreadForUser(user.getId()))
                    .build());
        });

    }


}
