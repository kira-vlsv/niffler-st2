package guru.qa.niffler.jupiter.example;

import guru.qa.niffler.jupiter.extension.SuiteExtension;

public class ExampleSuiteExtension implements SuiteExtension {
    @Override
    public void beforeSuite() {
        System.out.println("!BEFORE SUITE!");
    }

    @Override
    public void afterSuite() {
        System.out.println("!AFTER SUITE!");
    }
}
