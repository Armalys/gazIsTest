package ru.gazis.exception;

public class ExistStorageException extends StorageException {

    public ExistStorageException(Exception e) {
        super("User already exist", e);
    }
}
