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

    void insertUser(UUID id, String userName){
        userMapper.insertUser(id, userName);
    }

    User getUserById(UUID id){
        return userMapper.getUserById(id);
    }

    User getUserByUserName(String username){
        return userMapper.getUserByUserName(username);
    }

    List<User> getAllUsers(){
        return userMapper.getAllUsers();
    }


}
