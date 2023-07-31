package guru.qa.niffler.uitests;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.jupiter.extension.GenerateCategoryExtension;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@ExtendWith(GenerateCategoryExtension.class)
public class SpendsWebTest extends BaseWebTest {

    static  {
        Configuration.browserSize = "1920x1080";
    }

    @BeforeEach
    void doLogin() {
        Selenide.open("http://127.0.0.1:3000");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue("user1");
        $("input[name='password']").setValue("1234");
        $("button[type='submit']").click();
    }

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
    @AllureId("101")
    @Test
    void spendShouldBeDeletedByActionInTable(SpendJson spend) {
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
}
