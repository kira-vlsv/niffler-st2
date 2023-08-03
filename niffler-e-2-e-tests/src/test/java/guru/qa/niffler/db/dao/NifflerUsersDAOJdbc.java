package guru.qa.niffler.db.dao;

import guru.qa.niffler.db.DataSourceProvider;
import guru.qa.niffler.db.ServiceDB;
import guru.qa.niffler.db.entity.AuthorityEntity;
import guru.qa.niffler.db.entity.UserEntity;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

public class NifflerUsersDAOJdbc implements NifflerUsersDAO {

    public static final DataSource ds = DataSourceProvider.INSTANCE.getDataSource(ServiceDB.NIFFLER_AUTH);

    @Override
    public int createUser(UserEntity user) {

        int executeUpdate;

        try (Connection connection = ds.getConnection()) {

            connection.setAutoCommit(false); // turn off transactions commit one by one

            try (PreparedStatement userStatement = connection.prepareStatement("INSERT INTO users " +
                    "(username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) " +
                    "VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement authorityStatement = connection.prepareStatement(
                         "INSERT INTO authorities (user_id, authority) VALUES (?, ?)")) {
                userStatement.setString(1, user.getUsername());
                userStatement.setString(2, passwordEncoder.encode(user.getPassword()));
                userStatement.setBoolean(3, user.getEnabled());
                userStatement.setBoolean(4, user.getAccountNonExpired());
                userStatement.setBoolean(5, user.getAccountNonLocked());
                userStatement.setBoolean(6, user.getCredentialsNonExpired());
                executeUpdate = userStatement.executeUpdate();

                final UUID userId;

                try (ResultSet generatedKeys = userStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        userId = UUID.fromString(generatedKeys.getString(1));
                        user.setId(userId);
                    } else {
                        throw new SQLException("User has not been created, no ID present");
                    }
                }

                for (AuthorityEntity authority : user.getAuthorities()) {
                    authorityStatement.setObject(1, userId);
                    authorityStatement.setString(2, authority.getAuthority().name());
                    authorityStatement.addBatch();
                    authorityStatement.clearParameters();
                }
                authorityStatement.executeBatch();
            } catch (SQLException e) {
                connection.rollback();
                connection.setAutoCommit(true);
                throw new RuntimeException(e);
            }

            connection.commit();
            connection.setAutoCommit(true);

        } catch (SQLException e) {
            throw new RuntimeException(e);
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
    public int deleteUser(UserEntity user) {
        String deleteSql = "WITH deleted_user AS (DELETE FROM users WHERE id = ? RETURNING id) " +
                "DELETE FROM authorities WHERE user_id = ?";
        try (Connection conn = ds.getConnection();
             PreparedStatement statement = conn.prepareStatement(deleteSql)) {
            statement.setObject(1, user.getId());
            statement.setObject(2, user.getId());
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
