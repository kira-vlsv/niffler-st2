package guru.qa.niffler.tests.uitests;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.jupiter.annotation.GenerateUserAPI;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static guru.qa.niffler.condition.SpendCondition.spends;

/*
 * In this class nested annotations are used to generate user with category and spend
 * Login with credentials and verify that user has spending from setup
 */
class SpendsNestedAnnotationsWebTest extends BaseWebTest {

    @ApiLogin(user = @GenerateUserAPI(
            categories = @GenerateCategory(
                    category = "Обучение"),
            spends = @GenerateSpend(
                    description = "QA GURU ADVANCED VOL 2",
                    currency = CurrencyValues.RUB,
                    amount = 52000.00
            )))
    @AllureId("108")
    @Test
    void spendShouldBeDisplayedInTable(UserJson user) {
        final SpendJson spend = user.getSpends().get(0);

        Selenide.open(CFG.getFrontUrl() + "/main");

        $(".spendings-table tbody").shouldBe(visible)
                .scrollTo()
                .$$("tr")
                .last(1)
                .shouldHave(spends(spend));
    }
}
