package guru.qa.niffler.tests.dbtests;

import com.github.javafaker.Faker;
import guru.qa.niffler.db.dao.NifflerUsersDAO;
import guru.qa.niffler.db.dao.NifflerUsersDAOHibernate;
import guru.qa.niffler.db.entity.Authority;
import guru.qa.niffler.db.entity.AuthorityEntity;
import guru.qa.niffler.db.entity.UserEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class CreateUserHibernateTest {

    private NifflerUsersDAO usersDAO = new NifflerUsersDAOHibernate();
    private UserEntity userEntity;
    private final Faker faker = new Faker();

    private final String userPassword = faker.internet().password();

    @BeforeEach
    void createUserForTest() {
        userEntity = new UserEntity();
        userEntity.setUsername(faker.name().username());
        userEntity.setPassword(userPassword);
        userEntity.setAccountNonExpired(true);
        userEntity.setEnabled(true);
        userEntity.setCredentialsNonExpired(true);
        userEntity.setAccountNonLocked(true);
        userEntity.setAuthorities(Arrays.stream(Authority.values()).map(
                a -> {
                    AuthorityEntity ae = new AuthorityEntity();
                    ae.setAuthority(a);
                    ae.setUser(userEntity);
                    return ae;
                }
        ).toList());
        usersDAO.createUser(userEntity);
    }

    @AfterEach
    void cleanup() {
        usersDAO.deleteUser(userEntity);
    }

    @Test
    void simpleTest() {
        System.out.println(userEntity.getUsername());
        System.out.println(userEntity.getPassword());
    }
}
