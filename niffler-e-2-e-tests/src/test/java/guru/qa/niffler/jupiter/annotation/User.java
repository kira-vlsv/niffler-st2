package guru.qa.niffler.jupiter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface User {

    UserType userType();

    enum UserType {
        WITH_FRIENDS, // user1, user2
        INVITATION_SENT, // user3, user4
        INVITATION_RECEIVED //user5, user6
    }
}
