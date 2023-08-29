package guru.qa.niffler.tests.callbackexample;

import guru.qa.niffler.jupiter.example.CallbackExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@Disabled
@ExtendWith(CallbackExtension.class)
public class SecondExampleSimpleTest extends BaseTest {

    @BeforeAll
    static void beforeAll() {
        System.out.println("  BeforeAll");
    }

    @BeforeEach
    void beforeEach() {
        System.out.println("     BeforeEach");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("  AfterAll");
    }

    @AfterEach
    void afterEach() {
        System.out.println("     AfterEach");
    }

    @Test
    void test3() throws Exception {
        System.out.println("        Test3");
//        throw new Exception();
    }

    @Test
    void test4() {
        System.out.println("        Test4");
    }
}
