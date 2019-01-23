package ru.gazis.exception;

public class StorageException extends RuntimeException {
    private final String name;

    public StorageException(Exception e) {
        this(e.getMessage(),e);
    }

    public StorageException(String message, String name) {
        super(message);
        this.name = name;
    }

    public StorageException(String message, Exception e) {
        this(message, null, e);
    }

    public StorageException(String message, String name, Exception e) {
        super(message, e);
        this.name = name;

    }
}
