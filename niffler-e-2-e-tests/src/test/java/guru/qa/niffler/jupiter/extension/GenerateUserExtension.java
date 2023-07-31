package guru.qa.niffler.jupiter.extension;

import com.github.javafaker.Faker;
import guru.qa.niffler.db.dao.NifflerUsersDAO;
import guru.qa.niffler.db.dao.NifflerUsersDAOJdbc;
import guru.qa.niffler.db.entity.Authority;
import guru.qa.niffler.db.entity.AuthorityEntity;
import guru.qa.niffler.db.entity.UserEntity;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class GenerateUserExtension implements ParameterResolver, BeforeEachCallback, AfterEachCallback {

    private final NifflerUsersDAO nifflerUsersDAO = new NifflerUsersDAOJdbc();
    private final Faker faker = new Faker();

    public static ExtensionContext.Namespace GENERATED_USER_NAMESPACE = ExtensionContext.Namespace
            .create(GenerateUserExtension.class);

    @Override
    public void beforeEach(ExtensionContext context) {
        List<UserEntity> userEntities = new ArrayList<>();
        Parameter[] testParameters = context.getRequiredTestMethod().getParameters();
        Parameter[] generateUserAnnotation = Arrays.stream(testParameters)
                .filter(tp -> tp.isAnnotationPresent(GenerateUser.class))
                .toArray(Parameter[]::new);
        if (Objects.nonNull(generateUserAnnotation)) {
            for (Parameter ignored : generateUserAnnotation) {
                UserEntity userEntity = new UserEntity();
                userEntity.setUsername(faker.name().username());
                userEntity.setPassword(faker.internet().password());
                userEntity.setAccountNonExpired(true);
                userEntity.setEnabled(true);
                userEntity.setCredentialsNonExpired(true);
                userEntity.setAccountNonLocked(true);
                userEntity.setAuthorities(Arrays.stream(Authority.values()).map(
                        a -> {
                            AuthorityEntity ae = new AuthorityEntity();
                            ae.setAuthority(a);
                            return ae;
                        }
                ).toList());
                nifflerUsersDAO.createUser(userEntity);
                userEntities.add(userEntity);
            }
            context.getStore(GENERATED_USER_NAMESPACE).put(context.getRequiredTestMethod(), userEntities);
        }
    }

    @Override
    public void afterEach(ExtensionContext context) {
        List<UserEntity> list = context.getStore(GENERATED_USER_NAMESPACE).get(context.getRequiredTestMethod(), List.class);
        for(UserEntity user : list){
            nifflerUsersDAO.deleteUser(user.getUsername());
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().isAnnotationPresent(GenerateUser.class) &&
                parameterContext.getParameter().getType().isAssignableFrom(UserEntity.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public UserEntity resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        List<UserEntity> userEntities = extensionContext.getStore(GENERATED_USER_NAMESPACE).get(extensionContext.getRequiredTestMethod(), List.class);
        return userEntities.get(parameterContext.getIndex());
    }
}
