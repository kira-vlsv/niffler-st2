package guru.qa.niffler.jupiter;

import guru.qa.niffler.api.SpendService;
import guru.qa.niffler.model.CategoryJson;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class GenerateCategoryExtension implements ParameterResolver, BeforeEachCallback {

    public static ExtensionContext.Namespace CATEGORY_NAMESPACE = ExtensionContext.Namespace
            .create(GenerateCategoryExtension.class);

    public static final OkHttpClient httpClient = new OkHttpClient.Builder().build();

    public final Retrofit retrofit = new Retrofit.Builder()
            .client(httpClient)
            .baseUrl("http://127.0.0.1:8093")
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private final SpendService spendService = retrofit.create(SpendService.class);

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        GenerateCategory annotation = context.getRequiredTestMethod().getAnnotation(GenerateCategory.class);
        if (annotation != null) {
            CategoryJson category = new CategoryJson();
            category.setCategory(annotation.category());
            category.setUsername(annotation.username());
            CategoryJson created = spendService.addCategory(category).execute().body();
            context.getStore(CATEGORY_NAMESPACE).put(context.getRequiredTestMethod(), created);
        }

    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(CategoryJson.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return extensionContext.getStore(CATEGORY_NAMESPACE).get(extensionContext.getRequiredTestMethod(), CategoryJson.class);
    }
}
