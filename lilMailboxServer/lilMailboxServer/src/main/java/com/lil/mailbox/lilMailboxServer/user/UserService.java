package com.lil.mailbox.lilMailboxServer.user;

import com.lil.mailbox.lilMailboxServer.datasource.models.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

    void addUser(String userName);

    List<User> getAllUsers();

    User getUserById(UUID id);

    User getUserByUserName(String username);
}
