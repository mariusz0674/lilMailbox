package com.lil.mailbox.lilMailboxServer.user;

import com.lil.mailbox.lilMailboxServer.datasource.UserDAO;
import com.lil.mailbox.lilMailboxServer.datasource.models.User;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;

    @Override
    public void addUser(String userName) {
        UUID userId = UUID.randomUUID();
        this.userDAO.insertUser(userId, userName);
    }

    @Override
    public List<User> getAllUsers() {
        return this.userDAO.getAllUsers();
    }

    @Override
    @Cacheable(value = "users", key = "#id")
    public User getUserById(UUID id) {
        return this.userDAO.getUserById(id);
    }

    @Override
    public User getUserByUserName(String username) {
        return this.userDAO.getUserByUserName(username);
    }
}
