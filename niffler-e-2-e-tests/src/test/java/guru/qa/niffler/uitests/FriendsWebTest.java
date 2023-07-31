package guru.qa.niffler.uitests;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extension.UserQueueExtension;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Selenide.$;
import static guru.qa.niffler.jupiter.annotation.User.UserType.INVITATION_SENT;
import static guru.qa.niffler.jupiter.annotation.User.UserType.WITH_FRIENDS;

@ExtendWith(UserQueueExtension.class)
public class FriendsWebTest extends BaseWebTest {

    @AllureId("102")
    @Test
    void friendsShouldBeVisible(@User(userType = WITH_FRIENDS) UserJson user1,
                                @User(userType = INVITATION_SENT) UserJson user2,
                                @User(userType = INVITATION_SENT) UserJson user3) {
        Selenide.open("http://127.0.0.1:3000");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(user1.getUsername());
        $("input[name='password']").setValue(user1.getPassword());

        Selenide.open("http://127.0.0.1:3000");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(user2.getUsername());
        $("input[name='password']").setValue(user2.getPassword());

        Selenide.open("http://127.0.0.1:3000");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(user3.getUsername());
        $("input[name='password']").setValue(user3.getPassword());

//        $("a[href*='friends']").click();
//        $$(".table tbody tr").shouldHave(sizeGreaterThan(0));
    }

    @AllureId("103")
    @Test
    void friendsShouldBeVisible01(@User(userType = WITH_FRIENDS) UserJson user1,
                                @User(userType = INVITATION_SENT) UserJson user2,
                                  @User(userType = WITH_FRIENDS) UserJson user3) {
        Selenide.open("http://127.0.0.1:3000");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(user1.getUsername());
        $("input[name='password']").setValue(user1.getPassword());

        Selenide.open("http://127.0.0.1:3000");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(user2.getUsername());
        $("input[name='password']").setValue(user2.getPassword());

        Selenide.open("http://127.0.0.1:3000");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(user3.getUsername());
        $("input[name='password']").setValue(user3.getPassword());

//        $("a[href*='friends']").click();
//        $$(".table tbody tr").shouldHave(sizeGreaterThan(0));
    }

    @AllureId("104")
    @Test
    void friendsShouldBeVisible02(@User(userType = WITH_FRIENDS) UserJson user1,
                                @User(userType = INVITATION_SENT) UserJson user2) {
        Selenide.open("http://127.0.0.1:3000");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(user1.getUsername());
        $("input[name='password']").setValue(user1.getPassword());

        Selenide.open("http://127.0.0.1:3000");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(user2.getUsername());
        $("input[name='password']").setValue(user2.getPassword());

//        $("a[href*='friends']").click();
//        $$(".table tbody tr").shouldHave(sizeGreaterThan(0));
    }
}
