package com.lil.mailbox.lilMailboxServer.datasource.postgres;

import com.lil.mailbox.lilMailboxServer.datasource.models.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.UUID;

public interface UserMapper {
    @Insert("INSERT INTO User (id, name) VALUES (#{id}, #{name})")
    void insertUser(@Param("id") UUID id, @Param("name") String name);

    @Select("SELECT * FROM User WHERE id = #{id}")
    User getUserById(@Param("id") UUID id);

    @Select("SELECT * FROM User WHERE username = #{username}")
    User getUserByUserName(@Param("username") String username);

    @Select("SELECT * FROM User")
    List<User> getAllUsers();
}
