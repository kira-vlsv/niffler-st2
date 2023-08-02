package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.FriendsPage;
import guru.qa.niffler.page.MainPage;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class Header extends BaseComponent<Header> {

    public Header() {
        super($("header"));
    }

    private final SelenideElement friendPageBtn = $("a[href*='friends']");
    private final SelenideElement mainPageBtn = $("a[href*='main']");

    @Override
    public Header verifyComponentDisplayed() {
        self.$(".header__title").shouldHave(text("Niffler. The coin keeper."));
        return this;
    }

    public FriendsPage goToFriendsPage() {
        friendPageBtn.click();
        return new FriendsPage();
    }

    public MainPage goToMainPage() {
        mainPageBtn.click();
        return new MainPage();
    }
}
