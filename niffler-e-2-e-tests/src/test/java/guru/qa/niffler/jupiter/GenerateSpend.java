package guru.qa.niffler.jupiter;

import guru.qa.niffler.model.CurrencyValues;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(ElementType.METHOD)
public @interface GenerateSpend {

    String username();

    String description();

    String category();

    double amount();

    CurrencyValues currency();
}
