package ru.gazis.exception;

public class NotExistStorageException extends StorageException {

    public NotExistStorageException() {
        this(null);
    }

    public NotExistStorageException(String name) {
        super("User " + name + " not exist", name);
    }
}
