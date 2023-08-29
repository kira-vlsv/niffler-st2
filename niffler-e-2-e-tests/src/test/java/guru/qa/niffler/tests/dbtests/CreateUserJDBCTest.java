package guru.qa.niffler.tests.dbtests;

import guru.qa.niffler.db.entity.UserEntity;
import guru.qa.niffler.jupiter.annotation.GenerateUserDB;
import guru.qa.niffler.jupiter.extension.GenerateUserDBExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/*
    This class uses @GenerateUserDB annotation to create user direct in DB before test and delete it after
    using JDBC
    Annotation is resolved by GenerateUserDBExtension
 */

@ExtendWith({GenerateUserDBExtension.class})
class CreateUserJDBCTest {

    @Test
    void simpleTest(@GenerateUserDB() UserEntity user1,
                   @GenerateUserDB() UserEntity user2) {
        System.out.println(user1.getUsername());
        System.out.println(user1.getPassword());
        System.out.println(user2.getUsername());
        System.out.println(user2.getPassword());
    }
}
