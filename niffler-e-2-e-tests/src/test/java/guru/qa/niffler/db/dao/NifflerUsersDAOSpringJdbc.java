package guru.qa.niffler.db.dao;

import guru.qa.niffler.db.DataSourceProvider;
import guru.qa.niffler.db.ServiceDB;
import guru.qa.niffler.db.entity.UserEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

public class NifflerUsersDAOSpringJdbc implements NifflerUsersDAO {

    private final TransactionTemplate transactionTemplate;
    private final JdbcTemplate jdbcTemplate;

    public NifflerUsersDAOSpringJdbc() {
        DataSourceTransactionManager transactionManager = new JdbcTransactionManager(
                DataSourceProvider.INSTANCE.getDataSource(ServiceDB.NIFFLER_AUTH));
        this.transactionTemplate = new TransactionTemplate(transactionManager);
        this.jdbcTemplate = new JdbcTemplate(transactionManager.getDataSource());
    }

    @Override
    public int createUser(UserEntity user) {
        return 0;
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
