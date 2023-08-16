package guru.qa.niffler.jupiter.extension;

import com.github.javafaker.Faker;
import guru.qa.niffler.api.client.AuthRestClient;
import guru.qa.niffler.api.client.UserRestClient;
import guru.qa.niffler.db.dao.NifflerUsersDAO;
import guru.qa.niffler.db.dao.NifflerUsersDAOSpringJdbc;
import guru.qa.niffler.db.entity.UserEntity;
import guru.qa.niffler.jupiter.annotation.GenerateUserAPI;
import guru.qa.niffler.model.UserJson;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.util.Objects;
import java.util.UUID;

public class GenerateUserAPIExtension implements ParameterResolver, BeforeEachCallback, AfterEachCallback {

    private final Faker faker = new Faker();

    public static ExtensionContext.Namespace GENERATED_USER_NAMESPACE = ExtensionContext.Namespace
            .create(GenerateUserAPIExtension.class);

    private final AuthRestClient authRestClient = new AuthRestClient();
    private final UserRestClient userRestClient = new UserRestClient();
    private final NifflerUsersDAO nifflerUsersDAO = new NifflerUsersDAOSpringJdbc();

    @Override
    public void beforeEach(ExtensionContext context) {
        GenerateUserAPI generateUserAnnotation = context.getRequiredTestMethod().getAnnotation(GenerateUserAPI.class);
        if (Objects.nonNull(generateUserAnnotation)) {
            String username;
            String password;
            if (Objects.nonNull(generateUserAnnotation.username())
                    && Objects.nonNull(generateUserAnnotation.password())) {
                username = generateUserAnnotation.username();
                password = generateUserAnnotation.password();
            } else {
                username = faker.name().username();
                password = faker.internet().password();
            }
            UserJson user = doRegister(username, password);
            context.getStore(GENERATED_USER_NAMESPACE).put(context.getRequiredTestMethod(), user);
        }
    }

    @Override
    public void afterEach(ExtensionContext context) {
        UserJson user = context.getStore(GENERATED_USER_NAMESPACE).get(context.getRequiredTestMethod(), UserJson.class);
        if (Objects.nonNull(user)) {
            UUID id = UUID.fromString(nifflerUsersDAO.getUserId(user.getUsername()));
            UserEntity userEntity = new UserEntity();
            userEntity.setId(id);
            nifflerUsersDAO.deleteUser(userEntity);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().isAnnotationPresent(GenerateUserAPI.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public UserJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(GENERATED_USER_NAMESPACE).get(extensionContext.getRequiredTestMethod(), UserJson.class);
    }

    private UserJson doRegister(String username, String password) {
//        authRestClient.getToken();
        authRestClient.register(username, password);
        return userRestClient.getCurrentUser(username);
    }
}
