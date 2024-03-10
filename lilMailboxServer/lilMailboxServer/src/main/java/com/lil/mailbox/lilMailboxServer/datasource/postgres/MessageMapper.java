package com.lil.mailbox.lilMailboxServer.datasource.postgres;

import com.lil.mailbox.lilMailboxServer.datasource.models.Message;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.UUID;

public interface MessageMapper {
    @Select("SELECT * FROM MESSAGES WHERE id = #{id}")
    Message getMessageById(@Param("id") UUID id);
}
