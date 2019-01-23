package ru.gazis.storage;

import org.junit.Before;
import org.junit.Test;
import ru.gazis.Config;
import ru.gazis.exception.NotExistStorageException;
import ru.gazis.model.User;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SqlStorageTest {
    private static final User USER_1;
    private static final User USER_2;
    private static final User USER_3;
    private static final User USER_DUMMY;

    private static final String NAME_1 = "name1";
    private static final String NAME_2 = "name2";
    private static final String NAME_3 = "name3";
    private static final String NAME_DUMMY = "nameDummy";

    private static final String LAST_NAME_1 = "fullName1";
    private static final String LAST_NAME_2 = "fullName2";
    private static final String LAST_NAME_3 = "fullName3";
    private static final String LAST_NAME_DUMMY = "fullNameDummy";


    private SqlStorage storage;

    public SqlStorageTest() {
        this.storage = Config.get().getStorage();
    }

    static {
        USER_1 = new User(NAME_1, LAST_NAME_1);
        USER_2 = new User(NAME_2, LAST_NAME_2);
        USER_3 = new User(NAME_3, LAST_NAME_3);
        USER_DUMMY = new User(NAME_DUMMY, LAST_NAME_DUMMY);
    }

    @Before
    public void setUp() {
        storage.clear();
        storage.save(USER_1);
        storage.save(USER_2);
        storage.save(USER_3);
    }


    @Test
    public void get() {
        User name1 = storage.get(NAME_1);
        User name2 = storage.get(NAME_2);
        User name3 = storage.get(NAME_3);
        assertEquals(name1, USER_1);
        assertEquals(name2, USER_2);
        assertEquals(name3, USER_3);
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() {
        storage.get(NAME_DUMMY);
    }

    @Test
    public void update() {
        User newUser = new User(NAME_1, "newLastName");
        storage.update(newUser);
        assertEquals(newUser, storage.get(NAME_1));
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() {
        storage.update(USER_DUMMY);
    }

    @Test
    public void getAllSorted() {
        List<User> storageExpected = Arrays.asList(USER_1, USER_2, USER_3);
        List<User> storageActual = storage.getAllSorted();
        assertEquals(storageExpected.size(), storageActual.size());
        assertEquals(storageExpected, storageActual);
    }

}