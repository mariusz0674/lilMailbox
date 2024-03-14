package com.lil.mailbox.lilMailboxServer.datasource.postgres;

import com.lil.mailbox.lilMailboxServer.datasource.models.MessageFolder;
import org.apache.ibatis.annotations.*;

import java.util.UUID;

public interface MessageMapper {
    @Select("SELECT * FROM MESSAGES WHERE id = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "fromUser", column = "from_user"),
            @Result(property = "toUser", column = "to_user"),
            @Result(property = "s3Key", column = "s3_key"),
            @Result(property = "title", column = "title")

    })
    MessageFolder getMessageById(@Param("id") UUID id);

    @Insert("INSERT INTO public.messages (id, from_user, to_user, s3_key, title) " +
            "VALUES (#{id}, #{fromUser}, #{toUser}, #{s3Key}, #{title})")
    void insertMessage(MessageFolder message);
}
