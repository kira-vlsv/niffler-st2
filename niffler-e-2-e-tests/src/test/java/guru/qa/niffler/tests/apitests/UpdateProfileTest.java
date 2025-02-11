package guru.qa.niffler.tests.apitests;

import guru.qa.niffler.api.restclient.UserRestClient;
import guru.qa.niffler.jupiter.annotation.ClassPathUser;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.AllureId;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;

/*
 * This class uses @ClassPathUser annotation to read values from file.
 * Annotation is resolved by ClassPathUserConverter
 */

public class UpdateProfileTest {

    private final UserRestClient userRestClient = new UserRestClient();

    @ValueSource(strings = {
            "testdata/user3Update.json"
    })
    @AllureId("107")
    @ParameterizedTest
    public void updateUser(@ClassPathUser UserJson userJson) {
        step("Update user " + userJson.getUsername(), () -> {
            UserJson user = userRestClient.updateUserInfo(userJson);
            assertEquals(userJson.getCurrency(), user.getCurrency());
            assertEquals(userJson.getFirstname(), user.getFirstname());
        });
    }
}
