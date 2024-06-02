package com.lil.mailbox.lilMailboxServer;


import com.lil.mailbox.lilMailboxServer.counters.UserUnreadCounterService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class StartupRunner implements CommandLineRunner {

    private final UserUnreadCounterService userUnreadCounterService;

    @Override
    public void run(String... args) throws Exception {
        this.userUnreadCounterService.cacheMessageCounts();
    }

}
