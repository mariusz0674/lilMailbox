package com.lil.mailbox.lilMailboxServer.user;

import com.lil.mailbox.lilMailboxServer.datasource.UserDAO;
import com.lil.mailbox.lilMailboxServer.datasource.models.User;
import com.lil.mailbox.lilMailboxServer.datasource.redis.UserUnreadCounter;
import com.lil.mailbox.lilMailboxServer.datasource.redis.UserUnreadCounterRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;
    private final UserUnreadCounterRepository userUnreadCounterRepository;

    @Override
    public void addUser(String userName) {
        UUID userId = UUID.randomUUID();
        this.userDAO.insertUser(userId, userName);
        userUnreadCounterRepository.save(UserUnreadCounter.builder()
                .userId(userId)
                .unreadCount(0)
                .build());
    }

    @Override
    public List<User> getAllUsers() {
        return this.userDAO.getAllUsers();
    }

    @Override
    public User getUserById(UUID id) {
        return this.userDAO.getUserById(id);
    }

    @Override
    public User getUserByUserName(String username) {
        return this.userDAO.getUserByUserName(username);
    }
}
