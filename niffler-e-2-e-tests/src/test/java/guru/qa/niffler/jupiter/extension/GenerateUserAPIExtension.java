package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.db.dao.NifflerUsersDAO;
import guru.qa.niffler.db.dao.NifflerUsersDAOSpringJdbc;
import guru.qa.niffler.db.entity.UserEntity;
import guru.qa.niffler.jupiter.annotation.GenerateUserAPI;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.extension.*;

import java.util.Objects;

public class GenerateUserAPIExtension implements ParameterResolver, BeforeEachCallback, AfterEachCallback {

    private final GenerateUserService generateUserService = new GenerateUserService();
    public static ExtensionContext.Namespace GENERATED_USER_NAMESPACE = ExtensionContext.Namespace
            .create(GenerateUserAPIExtension.class);
    private final NifflerUsersDAO nifflerUsersDAO = new NifflerUsersDAOSpringJdbc();

    @Override
    public void beforeEach(ExtensionContext context) {
        GenerateUserAPI generateUserAnnotation = context.getRequiredTestMethod().getAnnotation(GenerateUserAPI.class);
        if (Objects.nonNull(generateUserAnnotation)) {
            context.getStore(GENERATED_USER_NAMESPACE).put(getTestId(context),
                    generateUserService.generateUser(generateUserAnnotation));
        }
    }

    @Override
    public void afterEach(ExtensionContext context) {
        UserJson user = context.getStore(GENERATED_USER_NAMESPACE).get(context.getRequiredTestMethod(), UserJson.class);
        if (Objects.nonNull(user)) {
            UserEntity userEntity = nifflerUsersDAO.getUser(user.getUsername());
            nifflerUsersDAO.deleteUser(userEntity);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(UserJson.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public UserJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(GENERATED_USER_NAMESPACE).get(getTestId(extensionContext), UserJson.class);
    }

    private String getTestId(ExtensionContext context) {
        return Objects
                .requireNonNull(context.getRequiredTestMethod().getAnnotation(AllureId.class))
                .value();
    }
}
