package guru.qa.niffler.apitests;

import guru.qa.niffler.api.UserService;
import guru.qa.niffler.jupiter.annotation.ClassPathUser;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.AllureId;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UpdateProfileTest {

    public final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://127.0.0.1:8089")
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private final UserService userService = retrofit.create(UserService.class);


    @ValueSource(strings = {
            "testdata/user3Update.json"
    })
    @AllureId("104")
    @ParameterizedTest
    public void updateUser(@ClassPathUser UserJson userJson) {

        step("Update user " + userJson.getUsername(), () -> {
            Response<UserJson> response = userService.updateUserInfo(userJson).execute();
            UserJson updatedUser = response.body();
            assertTrue(response.isSuccessful());
            assertEquals(userJson.getCurrency(), updatedUser.getCurrency());
            assertEquals(userJson.getFirstname(), updatedUser.getFirstname());
        });
    }
}
