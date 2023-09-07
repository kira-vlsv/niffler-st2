package guru.qa.niffler.jupiter.annotation;

import guru.qa.niffler.model.CurrencyValues;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(ElementType.METHOD)
public @interface GenerateSpend {

    String username() default "";

    String description();

    String category() default "";

    double amount();

    CurrencyValues currency();
}
