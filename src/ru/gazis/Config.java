package ru.gazis;

import ru.gazis.storage.SqlStorage;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
//    private static final File PROPS = new File(getHomeDir(), "config/users.properties");
    private static final File PROPS = new File("F:\\rep\\gazIsTest\\config\\users.properties");
    private static final Config INSTANCE = new Config();

    private final SqlStorage storage;

    public static Config get() {
        return INSTANCE;
    }

    private Config() {
        try (InputStream is = new FileInputStream(PROPS)) {
            Properties props = new Properties();
            props.load(is);
            storage = new SqlStorage(props.getProperty("db.url"), props.getProperty("db.user"), props.getProperty("db.password"));
        } catch (IOException e) {
            throw new IllegalStateException("Invalid config file " + PROPS.getAbsolutePath());
        }
    }

    public SqlStorage getStorage() {
        return storage;
    }

    private static File getHomeDir() {
        String prop = System.getProperty("homeDir");
        File homeDir = new File(prop == null ? "." : prop);
        if (!homeDir.isDirectory()) {
            throw new IllegalStateException(prop + " is not directory");
        }
        return homeDir;
    }
}
