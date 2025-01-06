package com.lil.mailbox.lilMailboxServer.user;

import com.lil.mailbox.lilMailboxServer.datasource.models.User;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/add")
    public void addUser(@RequestBody String userName) {
        userService.addUser(userName);
    }

}
