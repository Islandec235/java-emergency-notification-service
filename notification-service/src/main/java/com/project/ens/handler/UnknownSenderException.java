package com.project.ens.handler;

public class UnknownSenderException extends RuntimeException {
    public UnknownSenderException(String message) {
        super(message);
    }
}
