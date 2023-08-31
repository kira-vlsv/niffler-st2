package guru.qa.niffler.tests.uitests;

import com.codeborne.selenide.Configuration;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.WebTest;

@WebTest
public class BaseWebTest {

    protected static final Config CFG = Config.getConfig();

}
