package guru.qa.niffler.tests;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extension.UserQueueExtension;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static guru.qa.niffler.jupiter.annotation.User.UserType.WITH_FRIENDS;

@ExtendWith(UserQueueExtension.class)
public class FriendsWebTest extends BaseWebTest {

    @AllureId("102")
    @Test
    void friendsShouldBeVisible(@User(userType = WITH_FRIENDS) UserJson user) {
        Selenide.open("http://localhost:3000");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(user.getUsername());
        $("input[name='password']").setValue(user.getPassword());
        $("button[type='submit']").click();

        $("a[href*='friends']").click();
        $$(".table tbody tr").shouldHave(sizeGreaterThan(0));
    }
}
