package guru.qa.niffler.tests.uitests;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.jupiter.annotation.GenerateUserAPI;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static guru.qa.niffler.condition.SpendCondition.spends;

public class SpendsWebTest extends BaseWebTest {
//    @BeforeEach
//    void doLogin() {
//        Selenide.open("http://127.0.0.1:3000");
//        $("a[href*='redirect']").click();
//        $("input[name='username']").setValue("user1");
//        $("input[name='password']").setValue("1234");
//        $("button[type='submit']").click();
//    }

    @GenerateCategory(
            username = "user1",
            category = "NewCategory"
    )
    @GenerateSpend(
            username = "user1",
            description = "Test 1",
            currency = CurrencyValues.RUB,
            amount = 673948.90,
            category = "NewCategory")
    @ApiLogin(username = "user1", password = "1234")
    @AllureId("101")
    @Test
    void spendShouldBeDeletedByActionInTable(SpendJson spend) {
        Selenide.open(CFG.getFrontUrl() + "/main");

        $(".spendings-table tbody").shouldBe(visible)
                .$$("tr")
                .find(text(spend.getDescription()))
                .$("td").scrollTo().click();

        $$(".button_type_small").find(text("Delete selected"))
                .click();

        $(".spendings-table tbody")
                .$$("tr")
                .shouldHave(CollectionCondition.size(0));
    }

    @GenerateUserAPI(username = "user04", password = "1234")
    @GenerateCategory(
            username = "user04",
            category = "NewCategory"
    )
    @GenerateSpend(
            username = "user04",
            description = "Test 3",
            currency = CurrencyValues.KZT,
            amount = 6739.90,
            category = "NewCategory")
    @ApiLogin(username = "user04", password = "1234")
    @AllureId("101")
    @Test
    void spendShouldBeDisplayedInTable(SpendJson spend) {
        Selenide.open(CFG.getFrontUrl() + "/main");

        $(".spendings-table tbody").shouldBe(visible)
                .scrollTo()
                .$$("tr")
                .last(1)
                .shouldHave(spends(spend));
    }
}
