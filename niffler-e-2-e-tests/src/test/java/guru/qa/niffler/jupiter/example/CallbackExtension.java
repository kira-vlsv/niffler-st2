package guru.qa.niffler.jupiter.example;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;

public class CallbackExtension implements
        BeforeAllCallback,
        AfterAllCallback,
        BeforeEachCallback,
        AfterEachCallback,
        BeforeTestExecutionCallback,
        AfterTestExecutionCallback,
        TestExecutionExceptionHandler {

    @Override
    public void beforeAll(ExtensionContext context) {
//        context.getRequiredTestClass(); // class guru.qa.niffler.tests.simpletestsexample.CallbackExampleSimpleTest
        System.out.println("BeforeAll Callback!");
    }

    @Override
    public void afterAll(ExtensionContext context) {
        System.out.println("AfterAll Callback!");
    }

    @Override
    public void beforeEach(ExtensionContext context) {
//        context.getRequiredTestClass(); // class guru.qa.niffler.tests.simpletestsexample.CallbackExampleSimpleTest
//        context.getRequiredTestInstance(); // guru.qa.niffler.tests.simpletestsexample.CallbackExampleSimpleTest@52954945
//        context.getRequiredTestMethod(); // void guru.qa.niffler.tests.simpletestsexample.CallbackExampleSimpleTest.test2()
//        context.getParent().get().getRequiredTestClass(); // class guru.qa.niffler.tests.simpletestsexample.CallbackExampleSimpleTest
//        context.getRoot(); // context for test run
        System.out.println("   BeforeEach Callback!");
    }

    @Override
    public void afterEach(ExtensionContext context) {
        System.out.println("   AfterEach Callback!");
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
        System.out.println("      AfterTest Callback!");
    }

    @Override
    public void beforeTestExecution(ExtensionContext context) {
        System.out.println("      BeforeTest Callback!");
    }

    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        System.out.println("            Exception!!");
         throw throwable;
    }
}
