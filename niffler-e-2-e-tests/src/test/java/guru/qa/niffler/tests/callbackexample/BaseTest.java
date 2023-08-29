package guru.qa.niffler.tests.callbackexample;

import guru.qa.niffler.jupiter.example.ExampleSuiteExtension;
import org.junit.jupiter.api.extension.ExtendWith;

/*
    Tests in this package is to demonstrate all test's before and after callbacks:
        !BEFORE SUITE!
            BeforeAll Callback!
              BeforeAll
               BeforeEach Callback!
                 BeforeEach
                  BeforeTest Callback!
                    Test1
                  AfterTest Callback!
                 AfterEach
               AfterEach Callback!
              AfterAll
            AfterAll Callback!
        !AFTER SUITE!
 */

@ExtendWith(ExampleSuiteExtension.class)
public abstract class BaseTest {
}
