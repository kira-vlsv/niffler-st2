package guru.qa.niffler.utils;

import com.github.javafaker.Faker;

public class DataUtils {

    private static final Faker faker = new Faker();

    public static String getRandomUsername() {
        return faker.name().username();
    }

    public static String getRandomPassword() {
        return faker.internet().password();
    }
}
