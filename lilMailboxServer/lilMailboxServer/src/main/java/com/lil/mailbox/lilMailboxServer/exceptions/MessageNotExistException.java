package com.lil.mailbox.lilMailboxServer.exceptions;

public class MessageNotExistException extends RuntimeException {
    public MessageNotExistException(String message) {
        super(message);
    }
}
