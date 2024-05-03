package com.lil.mailbox.lilMailboxServer.datasource.redis;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserUnreadCounterRepository extends CrudRepository<UserUnreadCounter, String> {
}
