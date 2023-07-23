package guru.qa.niffler.tests;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.GenerateCategory;
import guru.qa.niffler.jupiter.GenerateCategoryExtension;
import guru.qa.niffler.jupiter.GenerateSpend;
import guru.qa.niffler.jupiter.GenerateSpendExtension;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@ExtendWith({GenerateSpendExtension.class, GenerateCategoryExtension.class})
public class SpendsWebTest {

    static  {
        Configuration.browserSize = "1920x1080";
    }

    @BeforeEach
    void doLogin() {
        Selenide.open("http://localhost:3000/main");
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
