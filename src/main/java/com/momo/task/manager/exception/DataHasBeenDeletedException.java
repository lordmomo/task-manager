package com.momo.task.manager.exception;

public class DataHasBeenDeletedException extends RuntimeException {
    public DataHasBeenDeletedException(String message) {
        super(message);
    }
}
