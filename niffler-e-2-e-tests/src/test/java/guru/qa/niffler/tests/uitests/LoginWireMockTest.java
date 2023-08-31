package guru.qa.niffler.tests.uitests;

import com.codeborne.selenide.Selenide;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.github.tomakehurst.wiremock.client.WireMock.*;

@WireMockTest(httpPort = 8089)
class LoginWireMockTest extends BaseWebTest {

    @AllureId("106")
    @Test
    void loginTest() {

        stubFor(get(urlEqualTo("/currentUser?username=user1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\": \"d689102a-be62-4b1e-894a-c42bcdc83dcf\"," +
                                "\"username\": \"user04\"," +
                                "\"firstname\": null," +
                                "\"surname\": null, " +
                                "\"currency\": \"RUB\"," +
                                "\"photo\": null}")
                ));

        UserJson userJson = new UserJson();
        userJson.setUsername("user1");
        userJson.setPassword("1234");
        Selenide.open(CFG.getFrontUrl() + "/main");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(userJson.getUsername());
        $("input[name='password']").setValue(userJson.getPassword());
        $("button[type='submit']").click();

        $(".header").should(visible).shouldHave(text("Niffler. The coin keeper."));
    }
}
