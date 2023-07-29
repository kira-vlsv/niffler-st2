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
import java.util.Arrays;
import java.util.HashMap;
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
        Parameter[] userAnnotation = Arrays.asList(testParameters)
                .stream()
                .filter(tp -> tp.isAnnotationPresent(User.class))
                .toArray(Parameter[]::new);
        if (Objects.nonNull(userAnnotation)) {
            Map<User.UserType, Map<String, UserJson>> users = new HashMap<>();
            for (Parameter parameter : userAnnotation) {
                User desiredUser = parameter.getAnnotation(User.class);
                User.UserType userType = desiredUser.userType();
                UserJson user = null;
                while (user == null) {
                    switch (userType) {
                        case WITH_FRIENDS -> user = USERS_WITH_FRIENDS_QUEUE.poll();
                        case INVITATION_RECEIVED -> user = USERS_INVITATION_RECEIVED_QUEUE.poll();
                        case INVITATION_SENT -> user = USERS_INVITATION_SENT_QUEUE.poll();
                    }
                }
                users.putIfAbsent(userType, new HashMap<>());
                users.get(userType).put(parameter.getName(), user);
            }
            context.getStore(USER_EXTENSION_NAMESPACE).put(testId, users);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        final String testId = getTestId(context);
        if (Objects.nonNull(context.getStore(USER_EXTENSION_NAMESPACE).get(testId))) {
            Map<User.UserType, Map<String, UserJson>> users = context.getStore(USER_EXTENSION_NAMESPACE)
                    .get(testId, Map.class);
            for (User.UserType userType : users.keySet()) {
                switch (userType) {
                    case WITH_FRIENDS :
                        for (UserJson user : users.get(userType).values()) {
                            USERS_WITH_FRIENDS_QUEUE.add(user);
                        }
                    case INVITATION_RECEIVED :
                        for (UserJson user : users.get(userType).values()) {
                            USERS_INVITATION_RECEIVED_QUEUE.add(user);
                        }
                    case INVITATION_SENT :
                        for (UserJson user : users.get(userType).values()) {
                            USERS_INVITATION_SENT_QUEUE.add(user);
                        }
                }
            }
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
        User.UserType userType = parameterContext.getParameter().getAnnotation(User.class).userType();
        Map<User.UserType, Map<String, UserJson>> users = extensionContext.getStore(USER_EXTENSION_NAMESPACE)
                .get(testId, Map.class);
        return users.get(userType).get(parameterContext.getParameter().getName());
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
