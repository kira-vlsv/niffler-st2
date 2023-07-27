package guru.qa.niffler.tests;

import com.codeborne.selenide.Selenide;
import com.fasterxml.jackson.databind.ObjectMapper;
import guru.qa.niffler.jupiter.annotation.ClassPathUser;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginTest extends BaseWebTest {

    @ValueSource(strings = {
            "testdata/user1.json",
            "testdata/user2.json"
    })
    @AllureId("103")
    @ParameterizedTest
    void loginTest(@ClassPathUser UserJson user) throws IOException {

        Selenide.open("http://localhost:3000");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(user.getUsername());
        $("input[name='password']").setValue(user.getPassword());
        $("button[type='submit']").click();

        $(".header").should(visible).shouldHave(text("Niffler. The coin keeper."));
    }
}
