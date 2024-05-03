package com.lil.mailbox.lilMailboxServer.datasource.postgres;

import com.lil.mailbox.lilMailboxServer.datasource.models.MessageFolder;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.UUID;

public interface MessageMapper {
    @Select("SELECT * FROM MESSAGES WHERE id = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "fromUser", column = "from_user"),
            @Result(property = "toUser", column = "to_user"),
            @Result(property = "s3Key", column = "s3_key"),
            @Result(property = "title", column = "title"),
            @Result(property = "read", column = "read")
    })
    MessageFolder getMessageById(@Param("id") UUID id);

    @Select("SELECT * FROM MESSAGES WHERE from_user = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "fromUser", column = "from_user"),
            @Result(property = "toUser", column = "to_user"),
            @Result(property = "s3Key", column = "s3_key"),
            @Result(property = "title", column = "title"),
            @Result(property = "read", column = "read")
    })
    List<MessageFolder> getMessagesByFromUserId(@Param("id") UUID id);

    @Select("SELECT * FROM MESSAGES WHERE to_user = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "fromUser", column = "from_user"),
            @Result(property = "toUser", column = "to_user"),
            @Result(property = "s3Key", column = "s3_key"),
            @Result(property = "title", column = "title"),
            @Result(property = "read", column = "read")
    })
    List<MessageFolder> getMessagesByToUserId(@Param("id") UUID id);

    @Select("SELECT COUNT(*) FROM public.messages WHERE to_user = #{userId} AND read = false")
    int countUnreadForUser(UUID userId);

    @Insert("INSERT INTO public.messages (id, from_user, to_user, s3_key, title, read) " +
            "VALUES (#{id}, #{fromUser}, #{toUser}, #{s3Key}, #{title}, #{read})")
    void insertMessage(MessageFolder message);

    @Update("UPDATE public.messages SET read = true WHERE id = #{messageId}")
    void markAsRead(UUID messageId);
}
