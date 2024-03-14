package com.lil.mailbox.lilMailboxServer.datasource;

import com.lil.mailbox.lilMailboxServer.datasource.models.User;
import com.lil.mailbox.lilMailboxServer.datasource.postgres.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserDAO {
    private final UserMapper userMapper;

    public void insertUser(UUID id, String userName){
        userMapper.insertUser(id, userName);
    }

    public List<User> getAllUsers(){
        return userMapper.getAllUsers();
    }

    public User getUserById(UUID id){
        return userMapper.getUserById(id);
    }

    public User getUserByUserName(String username){
        return userMapper.getUserByUserName(username);
    }

}
