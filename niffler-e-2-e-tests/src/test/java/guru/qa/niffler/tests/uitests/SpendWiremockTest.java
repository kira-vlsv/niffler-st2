package guru.qa.niffler.tests.uitests;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Selenide;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.github.tomakehurst.wiremock.client.WireMock.*;

class SpendWiremockTest extends BaseWebTest {

    private static ObjectMapper om = new ObjectMapper();

    @RegisterExtension
    static WireMockExtension wireMockSpendExtension = WireMockExtension.newInstance()
            .options(
                    WireMockConfiguration.options().port(8093)
            )
            .configureStaticDsl(true).build();

    @AllureId("107")
    @Test
    void loginTest() {

        stubFor(get(urlEqualTo("/spends?username=user1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(getMockSpendJson())
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
        $(".spendings-table tbody").shouldBe(visible)
                .scrollTo()
                .$$("tr")
                .shouldHave(CollectionCondition.size(1));

    }

    private static String getMockSpendJson() {
        SpendJson mockSpendJson = new SpendJson();
        mockSpendJson.setUsername("user1");
        mockSpendJson.setId(UUID.randomUUID());
        mockSpendJson.setSpendDate(new Date());
        mockSpendJson.setCategory("mock category");
        mockSpendJson.setCurrency(CurrencyValues.EUR);
        mockSpendJson.setAmount(8767.09);
        mockSpendJson.setDescription("mock spend description");

        List<SpendJson> jsonList = new ArrayList<>();
        jsonList.add(mockSpendJson);
        try {
            return om.writeValueAsString(jsonList);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
