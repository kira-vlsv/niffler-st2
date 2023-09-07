package guru.qa.niffler.jupiter.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(METHOD)
@Retention(RUNTIME)
public @interface GenerateUserAPI {

    String username() default "";

    String password() default "";

    GenerateCategory[] categories() default {};

    GenerateSpend[] spends() default {};
}
