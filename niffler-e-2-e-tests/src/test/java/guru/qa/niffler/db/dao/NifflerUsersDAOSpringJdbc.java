package guru.qa.niffler.db.dao;

import guru.qa.niffler.db.DataSourceProvider;
import guru.qa.niffler.db.ServiceDB;
import guru.qa.niffler.db.entity.AuthorityEntity;
import guru.qa.niffler.db.entity.UserEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class NifflerUsersDAOSpringJdbc implements NifflerUsersDAO {

    private final TransactionTemplate transactionTemplate;
    private final JdbcTemplate jdbcTemplate;

    public NifflerUsersDAOSpringJdbc() {
        DataSourceTransactionManager transactionManager = new JdbcTransactionManager(
                DataSourceProvider.INSTANCE.getDataSource(ServiceDB.NIFFLER_AUTH));
        this.transactionTemplate = new TransactionTemplate(transactionManager);
        this.jdbcTemplate = new JdbcTemplate(Objects.requireNonNull(transactionManager.getDataSource()));
    }

    @Override
    public int createUser(UserEntity user) {
        return transactionTemplate.execute(status -> {
            SimpleJdbcInsert userInsert = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName("users")
                    .usingGeneratedKeyColumns("id");

            Map<String, Object> userParams = new HashMap<>();
            userParams.put("username", user.getUsername());
            userParams.put("password", passwordEncoder.encode(user.getPassword()));
            userParams.put("enabled", user.getEnabled());
            userParams.put("account_non_expired", user.getAccountNonExpired());
            userParams.put("account_non_locked", user.getAccountNonLocked());
            userParams.put("credentials_non_expired", user.getCredentialsNonExpired());

            Map<String, Object> generatedKeys = userInsert.executeAndReturnKeyHolder(new MapSqlParameterSource(userParams))
                    .getKeys();
            UUID userId = (UUID) Objects.requireNonNull(generatedKeys).get("id");
            user.setId(userId);

            for (AuthorityEntity authority : user.getAuthorities()) {
                jdbcTemplate.update("INSERT INTO authorities (user_id, authority) VALUES (?, ?)", userId,
                        authority.getAuthority().name());
            }

            return 0;
        });
    }

    @Override
    public UserEntity getUser(String username) {
        return null;
    }

    @Override
    public int updateUser(UserEntity user) {
        return 0;
    }

    @Override
    public int deleteUser(UserEntity user) {
        return transactionTemplate.execute(status -> {
            jdbcTemplate.update("DELETE FROM authorities WHERE user_id = ?", user.getId());
            return jdbcTemplate.update("DELETE FROM users WHERE id = ?", user.getId());
        });
    }

    @Override
    public String getUserId(String userName) {
        return jdbcTemplate.query("SELECT * FROM users WHERE username = ?", // запрос для выполнения
                rs -> {return rs.getString(1);}, // обработка результата
                userName); // аргументы для подстановки в запрос
    }
}
