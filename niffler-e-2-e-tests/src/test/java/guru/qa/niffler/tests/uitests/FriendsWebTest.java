package guru.qa.niffler.tests.uitests;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extension.UserQueueExtension;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.jupiter.annotation.User.UserType.INVITATION_SENT;
import static guru.qa.niffler.jupiter.annotation.User.UserType.WITH_FRIENDS;

/*
    This class uses @User annotation extended with UserQueueExtension
    which get user from queue and put it back after test execution
    so other test can use it
 */
@ExtendWith(UserQueueExtension.class)
class FriendsWebTest extends BaseWebTest {

    @AllureId("103")
    @Test
    void friendsShouldBeVisible(@User(userType = WITH_FRIENDS) UserJson user1,
                                @User(userType = INVITATION_SENT) UserJson user2,
                                @User(userType = INVITATION_SENT) UserJson user3) {
        System.out.println(user1.getUsername());
        System.out.println(user1.getPassword());

        System.out.println(user2.getUsername());
        System.out.println(user2.getPassword());

        System.out.println(user3.getUsername());
        System.out.println(user3.getPassword());
    }

    @AllureId("104")
    @Test
    void friendsShouldBeVisible01(@User(userType = WITH_FRIENDS) UserJson user1,
                                  @User(userType = INVITATION_SENT) UserJson user2,
                                  @User(userType = WITH_FRIENDS) UserJson user3) {

        System.out.println(user1.getUsername());
        System.out.println(user1.getPassword());

        System.out.println(user2.getUsername());
        System.out.println(user2.getPassword());

        System.out.println(user3.getUsername());
        System.out.println(user3.getPassword());
    }

    @AllureId("105")
    @Test
    void friendsShouldBeVisible02(@User(userType = WITH_FRIENDS) UserJson user1,
                                @User(userType = INVITATION_SENT) UserJson user2) {
        System.out.println(user1.getUsername());
        System.out.println(user1.getPassword());

        System.out.println(user2.getUsername());
        System.out.println(user2.getPassword());
    }
}
