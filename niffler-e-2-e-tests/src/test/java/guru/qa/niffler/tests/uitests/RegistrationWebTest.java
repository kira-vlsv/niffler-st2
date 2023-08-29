package guru.qa.niffler.tests.uitests;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.page.RegistrationPage;
import org.junit.jupiter.api.Test;

class RegistrationWebTest extends BaseWebTest {

    @Test
    void errorMessageDisplayedWhenPasswordsAreNotEqual() {
        Selenide.open(RegistrationPage.URL, RegistrationPage.class)
                .verifyPageLoaded()
                .doRegister("user", "1234", "3345")
                .verifyErrorMessage("Passwords should be equal");
     }
}
