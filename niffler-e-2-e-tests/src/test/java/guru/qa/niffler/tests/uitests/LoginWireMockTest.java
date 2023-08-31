package guru.qa.niffler.tests.uitests;

import com.codeborne.selenide.Selenide;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.MainPage;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.github.tomakehurst.wiremock.client.WireMock.*;

@WireMockTest(httpPort = 8089)
class LoginWireMockTest extends BaseWebTest {

    private static ObjectMapper om = new ObjectMapper();
    @Test
    void registrationAndLoginTest() {
        WireMock.stubFor(WireMock.get(urlPathEqualTo("/currentUser?username=user1")).willReturn(
                WireMock.aResponse()
                        .withStatus(200)
                        .withHeader("Content-type", "application/json")
                        .withBody(getMockUserJson())
        ));

        Selenide.open(CFG.getFrontUrl() + "/main");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue("user1");
        $("input[name='password']").setValue("1234");
        $("button[type='submit']").click();

        $(".header").should(visible).shouldHave(text("Niffler. The coin keeper."));

    }

    private String getMockUserJson() {
        UserJson mockUserJson = new UserJson();
        mockUserJson.setUsername("Привет из мока");
        mockUserJson.setId(UUID.randomUUID());
        mockUserJson.setFirstname("Привет из мока");
        mockUserJson.setSurname("Привет из мока");
        mockUserJson.setCurrency(CurrencyValues.EUR);
        mockUserJson.setPhoto(null);
        try {
            return om.writeValueAsString(mockUserJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

//    @AllureId("109")
//    @Test
//    void loginTest() {
//
//        stubFor(WireMock.get("/currentUser?username=user04")
//                .willReturn(aResponse()
//                        .withStatus(200)
//                        .withHeader("Content-type", "application/json")
//                        .withBody("{\"id\": \"d689102a-be62-4b1e-894a-c42bcdc83dcf\"," +
//                                "\"username\": \"user04\"," +
//                                "\"firstname\": null" +
//                                "\"surname\": null, " +
//                                "\"currency\": \"RUB\"," +
//                                "\"photo\": null}")
//        ));
//
//        UserJson userJson = new UserJson();
//        userJson.setUsername("user1");
//        userJson.setPassword("1234");
//        Selenide.open(CFG.getFrontUrl() + "/main");
//        $("a[href*='redirect']").click();
//        $("input[name='username']").setValue(userJson.getUsername());
//        $("input[name='password']").setValue(userJson.getPassword());
//        $("button[type='submit']").click();
//
//        $(".header").should(visible).shouldHave(text("Niffler. The coin keeper."));
//    }
}
