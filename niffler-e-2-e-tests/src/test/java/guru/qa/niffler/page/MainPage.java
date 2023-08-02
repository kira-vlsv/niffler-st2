package guru.qa.niffler.page;

import guru.qa.niffler.page.component.Header;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class MainPage extends BasePage {

    private final Header header = new Header();

    @Override
    public MainPage verifyPageLoaded() {
        $(".header__title").shouldHave(text("Niffler. The coin keeper."));
        return this;
    }

    public Header getHeader() {
        return header;
    }
}
