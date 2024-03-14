package com.lil.mailbox.lilMailboxServer.datasource.postgres;

import com.lil.mailbox.lilMailboxServer.datasource.models.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.UUID;

public interface UserMapper {
    @Insert("INSERT INTO public.user_data (id, username) VALUES (#{id}, #{userName})")
    void insertUser(@Param("id") UUID id, @Param("userName") String userName);

    @Select("SELECT * FROM public.user_data")
    List<User> getAllUsers();

    @Select("SELECT * FROM public.user_data WHERE id = #{id}")
    User getUserById(@Param("id") UUID id);

    @Select("SELECT * FROM public.user_data WHERE username = #{username}")
    User getUserByUserName(@Param("username") String username);

}
