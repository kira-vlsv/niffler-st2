package guru.qa.niffler.db.dao;

import guru.qa.niffler.db.DataSourceProvider;
import guru.qa.niffler.db.ServiceDB;
import guru.qa.niffler.db.entity.UserEntity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class NifflerUsersDAOJdbc implements NifflerUsersDAO {

    public static final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    public static final DataSource ds = DataSourceProvider.INSTANCE.getDataSource(ServiceDB.NIFFLER_AUTH);

    @Override
    public int createUser(UserEntity user) {

        int executeUpdate;

        try (Connection connection = ds.getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO users " +
                     "(username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) " +
                     "VALUES (?, ?, ?, ?, ?, ?)")) {
            statement.setString(1, user.getUsername());
            statement.setString(2, passwordEncoder.encode(user.getPassword()));
            statement.setBoolean(3, user.getEnabled());
            statement.setBoolean(4, user.getAccountNonExpired());
            statement.setBoolean(5, user.getAccountNonLocked());
            statement.setBoolean(6, user.getCredentialsNonExpired());
            executeUpdate = statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String insertAuthoritiesSql = "INSERT INTO authorities (user_id, authority) VALUES ('%s', '%s')";

        final String finalUserId = getUserId(user.getUsername());
        List<String> sqls = user.getAuthorities()
                .stream()
                .map(ae -> ae.getAuthority().name())
                .map(a -> String.format(insertAuthoritiesSql, finalUserId, a))
                .toList();

        for (String sql : sqls) {
            try (Connection connection = ds.getConnection();
                 Statement statement = connection.createStatement()) {
                statement.executeUpdate(sql);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return executeUpdate;
    }

    @Override
    public UserEntity getUser(String username) {
        String getSql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = ds.getConnection();
             PreparedStatement statement = conn.prepareStatement(getSql)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                UserEntity user = new UserEntity();
                user.setUsername(resultSet.getString(1));
                user.setPassword(resultSet.getString(2));
                user.setEnabled(resultSet.getBoolean(3));
                user.setAccountNonExpired(resultSet.getBoolean(4));
                user.setAccountNonLocked(resultSet.getBoolean(5));
                user.setCredentialsNonExpired(resultSet.getBoolean(6));
                return user;
            } else {
                throw new IllegalArgumentException("Can't find user by given username: " + username);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int updateUser(UserEntity user) {
        String updateSql = "UPDATE users SET username = ?, password = ?, enabled = ?, " +
                "account_non_expired = ?, account_non_locked = ?, credentials_non_expired = ?" +
                " WHERE username = ?";

        try (Connection conn = ds.getConnection();
             PreparedStatement statement = conn.prepareStatement(updateSql)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, passwordEncoder.encode(user.getPassword()));
            statement.setBoolean(3, user.getEnabled());
            statement.setBoolean(4, user.getAccountNonExpired());
            statement.setBoolean(5, user.getAccountNonLocked());
            statement.setBoolean(6, user.getCredentialsNonExpired());
            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int deleteUser(String username) {
        String deleteSql = "WITH deleted_user AS (DELETE FROM users WHERE username = ? RETURNING id) " +
                "DELETE FROM authorities WHERE user_id IN (SELECT id FROM deleted_user)";
        try (Connection conn = ds.getConnection();
             PreparedStatement statement = conn.prepareStatement(deleteSql)) {
            statement.setString(1, username);
            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getUserId(String userName) {
        try (Connection conn = ds.getConnection();
             PreparedStatement statement = conn.prepareStatement("SELECT * FROM users WHERE username = ?")) {
            statement.setString(1, userName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString(1);
            } else {
                throw new IllegalArgumentException("Can't find user by given username: " + userName);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
