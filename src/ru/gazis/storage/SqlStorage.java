package ru.gazis.storage;

import ru.gazis.exception.NotExistStorageException;
import ru.gazis.model.User;
import ru.gazis.sql.SqlHelper;

import java.sql.*;

public class SqlStorage {
    private SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
        sqlHelper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    protected void save(User user) {
        sqlHelper.transactionalExecute(connection -> {
                    try (PreparedStatement ps = connection.prepareStatement("INSERT INTO \"user\" (last_name, name) VALUES (?,?)")) {
                        ps.setString(1, user.getLastName());
                        ps.setString(2, user.getName());
                        ps.executeUpdate();
                    }
                }
        );
    }

    protected User get(String name) {
        return sqlHelper.execute("" +
                        "     SELECT * FROM \"user\" u " +
                        "     WHERE u.name =? ",
                ps -> {
                    ps.setString(1, name);
                    ResultSet rs = ps.executeQuery();
                    if (!rs.next()) {
                        throw new NotExistStorageException(name);
                    }
                    return new User(rs.getString("name"), rs.getString("last_name"));
                });
    }

    protected void updateLastName(String currentName, String newLastName) {
        sqlHelper.transactionalExecute(connection -> {
            try (PreparedStatement ps = connection.prepareStatement("UPDATE \"user\" SET last_name = ? WHERE name =?")) {
                ps.setString(1, newLastName);
                ps.setString(2, currentName);
                int i = ps.executeUpdate();
                if (i == 0) {
                    throw new NotExistStorageException(currentName);
                }
            }
        });
    }
}
