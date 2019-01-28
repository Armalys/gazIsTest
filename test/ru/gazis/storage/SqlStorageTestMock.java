package ru.gazis.storage;

import org.junit.Test;
import ru.gazis.exception.ExistStorageException;
import ru.gazis.exception.NotExistStorageException;
import ru.gazis.model.User;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class SqlStorageTestMock {

    private static final SqlStorage STORAGE;
    private static final User USER;
    private static final String NAME;

    static {
        STORAGE = mock(SqlStorage.class);
        USER = mock(User.class);
        NAME = USER.getName();      // not sure about this field
    }

    @Test
    public void save() {
        STORAGE.save(USER);
        verify(STORAGE).save(USER);
    }

    @Test(expected = ExistStorageException.class)
    public void saveExist() {
        doThrow(new ExistStorageException()).when(STORAGE).save(USER);
        STORAGE.save(USER);
        verify(STORAGE).save(USER);
    }

    @Test
    public void get() {
        STORAGE.get(NAME);
        verify(STORAGE).get(NAME);
        when(STORAGE.get(NAME)).thenReturn(USER);
        assertEquals(USER, STORAGE.get(NAME));
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() {
        doThrow(new NotExistStorageException()).when(STORAGE).get("dummy");
        STORAGE.get("dummy");
        verify(STORAGE).get("dummy");
    }

    @Test
    public void updateLastName() {
        String newLastName = "newLastName";
        STORAGE.updateLastName(NAME, newLastName);
        verify(STORAGE).updateLastName(NAME, newLastName);
    }

    @Test(expected = NotExistStorageException.class)
    public void updateLastNameNotExist() {
        String newLastName = "newLastName";
        String newName = "newName";
        doThrow(new NotExistStorageException()).when(STORAGE).updateLastName(newName, newLastName);
        STORAGE.updateLastName(newName, newLastName);
        verify(STORAGE).updateLastName(newName, newLastName);
    }
}