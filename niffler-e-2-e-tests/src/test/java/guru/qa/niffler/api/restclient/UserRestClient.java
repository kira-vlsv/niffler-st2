package guru.qa.niffler.api.restclient;

import guru.qa.niffler.api.UserService;
import guru.qa.niffler.model.UserJson;
import org.junit.jupiter.api.Assertions;

import javax.annotation.Nonnull;
import java.io.IOException;

public class UserRestClient extends BaseRestClient {

    private final UserService userService = retrofit.create(UserService.class);

    public UserRestClient() {
        super(CFG.getUserServiceBaseUrl());
    }

    public @Nonnull UserJson updateUserInfo(UserJson user) {
        try {
            return userService.updateUserInfo(user).execute().body();
        } catch (IOException e) {
            Assertions.fail("Can't execute api call to niffler-userdata. " + e.getMessage());
            return null;
        }
    }

    public @Nonnull UserJson getCurrentUser(String username) {
        try {
            return userService.getCurrentUser(username).execute().body();
        } catch (IOException e) {
            Assertions.fail("Can't execute api call to niffler-userdata. " + e.getMessage());
            return null;
        }
    }
}
