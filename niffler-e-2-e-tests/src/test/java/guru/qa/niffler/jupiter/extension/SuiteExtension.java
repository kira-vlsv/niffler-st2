package guru.qa.niffler.jupiter.extension;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Store.CloseableResource;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;

public interface SuiteExtension extends BeforeAllCallback {

    default void beforeSuite() {

    }

    default void afterSuite() {

    }

    @Override
    default void beforeAll(ExtensionContext context) throws Exception {
        context.getRoot().getStore(Namespace.GLOBAL)
                .getOrComputeIfAbsent(
                        SuiteExtension.class,
                        suiteExtensionClass -> {
                            beforeSuite();
                            return (CloseableResource) this::afterSuite;
                        });
    }

//    default void beforeAll(ExtensionContext context) throws Exception {
//        context.getRoot().getStore(Namespace.GLOBAL)
//                .getOrComputeIfAbsent(
//                        SuiteExtension.class,
//                        new Function<Class<SuiteExtension>, ExtensionContext.Store.CloseableResource>() {
//                            @Override
//                            public ExtensionContext.Store.CloseableResource apply(Class<SuiteExtension> suiteExtensionClass) {
//                                beforeSuite();
//                                return new ExtensionContext.Store.CloseableResource() {
//                                    @Override
//                                    public void close() throws Throwable {
//                                        afterSuite();
//                                    }
//                                };
//                            }
//                        });
//    }
}
