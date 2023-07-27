package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;

import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class UserQueueExtension implements
        BeforeEachCallback,
        AfterTestExecutionCallback,
        ParameterResolver {

    public static Namespace USER_EXTENSION_NAMESPACE = Namespace.create(UserQueueExtension.class);

    private static Queue<UserJson> USERS_WITH_FRIENDS_QUEUE = new ConcurrentLinkedQueue<>();
    private static Queue<UserJson> USERS_INVITATION_SENT_QUEUE = new ConcurrentLinkedQueue<>();
    private static Queue<UserJson> USERS_INVITATION_RECEIVED_QUEUE = new ConcurrentLinkedQueue<>();

    static {
        USERS_WITH_FRIENDS_QUEUE.addAll(List.of(
                userJson("user1", "1234"),
                userJson("user2", "1234")));
        USERS_INVITATION_SENT_QUEUE.addAll(List.of(
                userJson("user3", "1234"),
                userJson("user4", "1234")));
        USERS_INVITATION_RECEIVED_QUEUE.addAll(List.of(
                userJson("user5", "1234"),
                userJson("user6", "1234")));
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        final String testId = getTestId(context);
        Parameter[] testParameters = context.getRequiredTestMethod().getParameters();
        for (Parameter testParameter : testParameters) {
            User desiredUser = testParameter.getAnnotation(User.class);
            if (desiredUser != null) {
                User.UserType userType = desiredUser.userType();
                UserJson user = null;
                while (user == null) {
                    switch (userType) {
                        case WITH_FRIENDS -> user = USERS_WITH_FRIENDS_QUEUE.poll();
                        case INVITATION_RECEIVED -> user = USERS_INVITATION_RECEIVED_QUEUE.poll();
                        case INVITATION_SENT -> user = USERS_INVITATION_SENT_QUEUE.poll();
                    }
                }
                context.getStore(USER_EXTENSION_NAMESPACE).put(testId, Map.of(userType, user));
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        final String testId = getTestId(context);
        Map<User.UserType, UserJson> user = (Map<User.UserType, UserJson>) context.getStore(USER_EXTENSION_NAMESPACE)
                .get(testId);
        User.UserType userType = user.keySet().iterator().next();
        switch (userType) {
            case WITH_FRIENDS -> USERS_WITH_FRIENDS_QUEUE.add(user.get(userType));
            case INVITATION_RECEIVED -> USERS_INVITATION_RECEIVED_QUEUE.add(user.get(userType));
            case INVITATION_SENT -> USERS_INVITATION_SENT_QUEUE.add(user.get(userType));
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return
                parameterContext.getParameter().isAnnotationPresent(User.class) &&
                parameterContext.getParameter().getType().isAssignableFrom(UserJson.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public UserJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        final String testId = getTestId(extensionContext);
        Map<User.UserType, UserJson> user = (Map<User.UserType, UserJson>) extensionContext.getStore(USER_EXTENSION_NAMESPACE)
                .get(testId);
        return user.values().iterator().next();
    }

    private String getTestId(ExtensionContext extensionContext) {
        return Objects.requireNonNull(extensionContext.getRequiredTestMethod()
                .getAnnotation(AllureId.class)
                .value());
    }

    private static UserJson userJson(String userName, String password) {
        UserJson user = new UserJson();
        user.setUsername(userName);
        user.setPassword(password);
        return user;
    }
}
