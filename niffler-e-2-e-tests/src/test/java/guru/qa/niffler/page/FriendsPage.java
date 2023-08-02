package guru.qa.niffler.page;

import guru.qa.niffler.page.component.Header;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class FriendsPage extends BasePage<FriendsPage> {

    private final Header header = new Header();

    @Override
    public FriendsPage verifyPageLoaded() {
        $(".table abstract-table").shouldBe(visible);
        return this;
    }

    public Header getHeader() {
        return header;
    }

}
