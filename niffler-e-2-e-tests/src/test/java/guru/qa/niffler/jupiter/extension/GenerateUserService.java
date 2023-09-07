package guru.qa.niffler.jupiter.extension;

import com.google.common.base.Stopwatch;
import guru.qa.niffler.api.restclient.AuthRestClient;
import guru.qa.niffler.api.restclient.SpendRestClient;
import guru.qa.niffler.api.restclient.UserRestClient;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.jupiter.annotation.GenerateUserAPI;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import org.springframework.lang.NonNull;

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static guru.qa.niffler.utils.DataUtils.getRandomPassword;
import static guru.qa.niffler.utils.DataUtils.getRandomUsername;

public class GenerateUserService {

    private final AuthRestClient authRestClient = new AuthRestClient();
    private final UserRestClient userRestClient = new UserRestClient();
    private final SpendRestClient spendRestClient = new SpendRestClient();

    public UserJson generateUser(@NonNull GenerateUserAPI annotation) {
        UserJson user = createUser();
        createCategoryIfPresent(user, annotation.categories());
        createSpendIfPresent(user, annotation.spends());
        return user;
    }

    private UserJson createUser() {
        final String username = getRandomUsername();
        final String password = getRandomPassword();
        authRestClient.register(username, password);
        UserJson user = waitWhileUserToBeConsumed(username, 5000L);
        user.setPassword(password);
        return user;
    }

    private void createCategoryIfPresent(UserJson user, GenerateCategory[] categories) {
        if(Objects.nonNull(categories)) {
            for(GenerateCategory annotation : categories) {
                CategoryJson category = new CategoryJson();
                category.setCategory(annotation.category());
                category.setUsername(user.getUsername());
                CategoryJson created = spendRestClient.addCategory(category);
                user.getCategories().add(created);
            }
        }
    }

    private void createSpendIfPresent(UserJson user, GenerateSpend[] spends) {
        if(Objects.nonNull(spends)) {
            for(GenerateSpend annotation : spends) {
                SpendJson spend = new SpendJson();
                spend.setUsername(user.getUsername());
                spend.setDescription(annotation.description());
                spend.setCategory("".equals(annotation.category()) ? user.getCategories().get(0).getCategory() : annotation.category());
                spend.setSpendDate(new Date());
                spend.setAmount(annotation.amount());
                spend.setCurrency(annotation.currency());
                SpendJson created = spendRestClient.addSpend(spend);
                user.getSpends().add(created);
            }
        }
    }

    private UserJson waitWhileUserToBeConsumed(String username, long maxWaitTime) {
        Stopwatch sw = Stopwatch.createStarted();
        while (sw.elapsed(TimeUnit.MILLISECONDS) < maxWaitTime) {
            UserJson userJson = userRestClient.getCurrentUser(username);
            if (userJson != null) {
                return userJson;
            } else {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        throw new IllegalStateException("Can`t obtain user from niffler-userdata");
    }
}
