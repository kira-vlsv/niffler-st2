package guru.qa.niffler.jupiter.annotation;

import guru.qa.niffler.jupiter.extension.ApiLoginExtension;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.extension.GenerateCategoryExtension;
import guru.qa.niffler.jupiter.extension.GenerateSpendExtension;
import guru.qa.niffler.jupiter.extension.GenerateUserAPIExtension;
import io.qameta.allure.junit5.AllureJunit5;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(TYPE)
@Retention(RUNTIME)
@ExtendWith({AllureJunit5.class,
        BrowserExtension.class,
        GenerateUserAPIExtension.class,
        GenerateCategoryExtension.class,
        GenerateSpendExtension.class,
        ApiLoginExtension.class})
public @interface WebTest {
}
