package guru.qa.niffler.tests.dbtests;

import guru.qa.niffler.db.entity.UserEntity;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.jupiter.extension.GenerateUserExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({GenerateUserExtension.class})
public class CreateUserJDBCTest {

    @Test
    void simpleTest(@GenerateUser() UserEntity user1,
                   @GenerateUser() UserEntity user2) {
        System.out.println(user1.getUsername());
        System.out.println(user1.getPassword());
        System.out.println(user2.getUsername());
        System.out.println(user2.getPassword());
    }
}
