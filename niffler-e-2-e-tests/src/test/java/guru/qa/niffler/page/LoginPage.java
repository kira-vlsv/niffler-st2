package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage extends BasePage {

    private final SelenideElement usernameInput = $(byName("username"));
    private final SelenideElement passwordInput = $(byName("password"));
    private final SelenideElement signInBtn = $(".form__submit");

    @Override
    public LoginPage verifyPageLoaded() {
        $(".form__paragraph").shouldHave(text("Please sign in"));
        return this;
    }

    public MainPage doLogin(String username, String password) {
        usernameInput.val(username);
        passwordInput.val(password);
        signInBtn.click();
        return new MainPage();
    }

}
