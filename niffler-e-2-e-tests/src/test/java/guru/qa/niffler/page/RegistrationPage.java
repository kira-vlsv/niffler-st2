package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class RegistrationPage extends BasePage {

    public static final String URL = Config.getConfig().getAuthUrl() + "/register";
    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement passwordInput = $("#password");
    private final SelenideElement passwordSubmitInput = $("#passwordSubmit");
    private final SelenideElement signUpBtn = $(".form__submit");

    @Override
    public RegistrationPage verifyPageLoaded() {
        $(".form__paragraph").shouldHave(text("Registration form"));
        return this;
    }

    public RegistrationPage doRegister(String username, String password, String passwordSubmit) {
        usernameInput.val(username);
        passwordInput.val(password);
        passwordSubmitInput.val(passwordSubmit);
        signUpBtn.click();
        return this;
    }

    public RegistrationPage verifyErrorMessage(String errorMessage) {
        $(".form__error").shouldHave(text(errorMessage));
        return this;
    }
}
