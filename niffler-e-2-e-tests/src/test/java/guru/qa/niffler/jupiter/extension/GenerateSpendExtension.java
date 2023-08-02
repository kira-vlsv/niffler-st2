package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.client.SpendRestClient;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.model.SpendJson;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.util.Date;

public class GenerateSpendExtension implements ParameterResolver, BeforeEachCallback {

    public static ExtensionContext.Namespace SPEND_NAMESPACE = ExtensionContext.Namespace
            .create(GenerateSpendExtension.class);

    private final SpendRestClient spendRestClient = new SpendRestClient();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        GenerateSpend annotation = context.getRequiredTestMethod().getAnnotation(GenerateSpend.class);
        if (annotation != null) {
            SpendJson spend = new SpendJson();
            spend.setUsername(annotation.username());
            spend.setDescription(annotation.description());
            spend.setCategory(annotation.category());
            spend.setSpendDate(new Date());
            spend.setAmount(annotation.amount());
            spend.setCurrency(annotation.currency());
            SpendJson created = spendRestClient.addSpend(spend);
            context.getStore(SPEND_NAMESPACE).put(context.getRequiredTestMethod(), created);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(SpendJson.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(SPEND_NAMESPACE).get(extensionContext.getRequiredTestMethod(), SpendJson.class);
    }
}
