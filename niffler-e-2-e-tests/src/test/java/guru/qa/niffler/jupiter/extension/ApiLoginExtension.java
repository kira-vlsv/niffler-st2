package guru.qa.niffler.jupiter.extension;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import guru.qa.niffler.api.AuthClient;
import guru.qa.niffler.api.context.CookieContext;
import guru.qa.niffler.api.context.SessionContext;
import guru.qa.niffler.api.restclient.AuthRestClient;
import guru.qa.niffler.api.util.OauthUtils;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.Cookie;

import java.util.Objects;

public class ApiLoginExtension implements BeforeEachCallback, AfterTestExecutionCallback {

    protected static final Config CFG = Config.getConfig();
    private static final AuthClient authClient = new AuthRestClient();
    private static final String JSESSIONID = "JSESSIONID";
    private static final GenerateUserService generateUserService = new GenerateUserService();

    @Override
    public void beforeEach(ExtensionContext context) {
        ApiLogin apiLogin = context.getRequiredTestMethod().getAnnotation(ApiLogin.class);
        if (apiLogin != null) {
            String username, password;
            if ("".equals(apiLogin.username()) || "".equals(apiLogin.password())) {
                if (Objects.nonNull(apiLogin.user())) {
                    UserJson generatedUser = generateUserService.generateUser(apiLogin.user()[0]);
                    username = generatedUser.getUsername();
                    password = generatedUser.getPassword();
                    context.getStore(GenerateUserAPIExtension.GENERATED_USER_NAMESPACE).put(getTestId(context), generatedUser);
                } else {
                    throw new IllegalStateException();
                }
            } else {
                username = apiLogin.username();
                password = apiLogin.password();
            }
            doLogin(username, password);
        }
    }

    private void doLogin(String username, String password) {
        final SessionContext sessionContext = SessionContext.getInstance();
        final String codeVerifier = OauthUtils.generateCodeVerifier();
        final String codeChallenge = OauthUtils.generateCodeChallange(codeVerifier);

        sessionContext.setCodeVerifier(codeVerifier);
        sessionContext.setCodeChallenge(codeChallenge);

        authClient.authorizePreRequest();
        authClient.login(username, password);
        final String token = authClient.getToken();
        setUpBrowser(token);
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
        SessionContext.getInstance().release();
        CookieContext.getInstance().release();
    }

    private void setUpBrowser(String token) {
        SessionContext sessionContext = SessionContext.getInstance();
        CookieContext cookieContext = CookieContext.getInstance();
        Selenide.open(CFG.getFrontUrl());
        Selenide.sessionStorage().setItem("id_token", token);
        Selenide.sessionStorage().setItem("codeChallenge", sessionContext.getCodeChallenge());
        Selenide.sessionStorage().setItem("codeVerifier", sessionContext.getCodeVerifier());
        Cookie jssesionIdCookie = new Cookie(JSESSIONID, cookieContext.getCookie(JSESSIONID));
        WebDriverRunner.getWebDriver().manage().addCookie(jssesionIdCookie);
    }

    private String getTestId(ExtensionContext context) {
        return Objects
                .requireNonNull(context.getRequiredTestMethod().getAnnotation(AllureId.class))
                .value();
    }
}