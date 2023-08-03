package guru.qa.niffler.tests.uitests;

import com.codeborne.selenide.Configuration;
import guru.qa.niffler.jupiter.annotation.WebTest;

@WebTest
public class BaseWebTest {

    static {
        Configuration.browserSize = "1920x1080";
    }
}
