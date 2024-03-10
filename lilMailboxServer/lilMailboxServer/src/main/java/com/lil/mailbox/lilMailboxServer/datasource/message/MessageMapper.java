package com.lil.mailbox.lilMailboxServer.datasource.message;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface MessageMapper {
    @Select("SELECT * FROM MESSAGES WHERE id = #{id}")
    Message getArticle(@Param("id") Long id);
}
