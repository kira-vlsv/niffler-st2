package guru.qa.niffler.jupiter;

import guru.qa.niffler.api.SpendService;
import guru.qa.niffler.model.SpendJson;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.Date;

public class GenerateSpendExtension implements ParameterResolver, BeforeEachCallback {

    public static ExtensionContext.Namespace SPEND_NAMESPACE = ExtensionContext.Namespace
            .create(GenerateSpendExtension.class);

    public static final OkHttpClient httpClient = new OkHttpClient.Builder().build();

    public final Retrofit retrofit = new Retrofit.Builder()
            .client(httpClient)
            .baseUrl("http://127.0.0.1:8093")
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private final SpendService spendService = retrofit.create(SpendService.class);

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
            SpendJson created = spendService.addSpend(spend).execute().body();
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
