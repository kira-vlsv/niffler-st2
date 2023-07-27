package guru.qa.niffler.jupiter.annotation;

import guru.qa.niffler.jupiter.extension.ClassPathUserConverter;
import org.junit.jupiter.params.converter.ConvertWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@ConvertWith(ClassPathUserConverter.class)
public @interface ClassPathUser {
}
