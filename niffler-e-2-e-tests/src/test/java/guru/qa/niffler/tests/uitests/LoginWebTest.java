package guru.qa.niffler.tests.uitests;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.ClassPathUser;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.AllureId;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

class LoginWebTest extends BaseWebTest {

    @ValueSource(strings = {
            "testdata/user1.json",
            "testdata/user2.json"
    })
    @AllureId("103")
    @ParameterizedTest
    void loginTest(@ClassPathUser UserJson user) {

        Selenide.open(CFG.getFrontUrl());
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(user.getUsername());
        $("input[name='password']").setValue(user.getPassword());
        $("button[type='submit']").click();

        $(".header").should(visible).shouldHave(text("Niffler. The coin keeper."));
    }
}