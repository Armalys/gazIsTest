package ru.gazis.storage;

import ru.gazis.exception.NotExistStorageException;
import ru.gazis.model.User;
import ru.gazis.sql.SqlHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    public void clear() {
        sqlHelper.execute("DELETE FROM \"user\"");
    }

    public void save(User user) {
        sqlHelper.<Void>transactionalExecute(connection -> {
                    insertUser(user, connection);
                    return null;
                }
        );
    }

    public User get(String name) {
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

    public void update(User user) {
        sqlHelper.transactionalExecute(connection -> {
            try (PreparedStatement ps = connection.prepareStatement("UPDATE \"user\" SET last_name = ? WHERE name =?")) {
                ps.setString(1, user.getLastName());
                ps.setString(2, user.getName());
                int i = ps.executeUpdate();
                if (i == 0) {
                    throw new NotExistStorageException(user.getName());
                }
                return i;
            }
        });
    }

    public List<User> getAllSorted() {
        Map<String, User> resumeMap = new LinkedHashMap<>();
        sqlHelper.transactionalExecute(connection -> {
            try (PreparedStatement psResume = connection.prepareStatement("SELECT * FROM \"user\" ORDER BY name")) {
                ResultSet rsResume = psResume.executeQuery();
                while (rsResume.next()) {
                    String name = rsResume.getString("name");
                    String lastName = rsResume.getString("last_name");
                    resumeMap.put(name, new User(name, lastName));
                }
            }
            return null;
        });
        return new ArrayList<>(resumeMap.values());
    }

    private void insertUser(User user, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO \"user\" (last_name, name) VALUES (?,?)")) {
            ps.setString(1, user.getLastName());
            ps.setString(2, user.getName());
            ps.executeUpdate();
        }
    }

}
