package guru.qa.niffler.api.client;

import guru.qa.niffler.api.UserService;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.UserJson;
import org.junit.jupiter.api.Assertions;

import javax.annotation.Nonnull;
import java.io.IOException;

public class UserRestClient extends BaseRestClient {

    private final UserService userService = retrofit.create(UserService.class);

    public UserRestClient() {
        super(Config.getConfig().getUserServiceBaseUrl());
    }

    public @Nonnull UserJson updateUserInfo(UserJson user) {
        try {
            return userService.updateUserInfo(user).execute().body();
        } catch (IOException e) {
            Assertions.fail("Can't execute api call to niffler-userdata. " + e.getMessage());
            return null;
        }
    }
}
