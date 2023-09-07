package guru.qa.niffler.tests.dbtests;

import guru.qa.niffler.db.dao.NifflerUsersDAO;
import guru.qa.niffler.db.dao.NifflerUsersDAOHibernate;
import guru.qa.niffler.db.entity.Authority;
import guru.qa.niffler.db.entity.AuthorityEntity;
import guru.qa.niffler.db.entity.UserEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static guru.qa.niffler.utils.DataUtils.getRandomPassword;
import static guru.qa.niffler.utils.DataUtils.getRandomUsername;

/*
 * This class uses Hibernate DAO to create user direct in DB before test
 * and delete it after using DB connection
 */

class CreateUserHibernateTest {

    private final String userPassword = getRandomPassword();
    private final NifflerUsersDAO usersDAO = new NifflerUsersDAOHibernate();
    private UserEntity userEntity;

    @BeforeEach
    void createUserForTest() {
        userEntity = new UserEntity();
        userEntity.setUsername(getRandomUsername());
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
