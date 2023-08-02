package guru.qa.niffler.api.client;

import guru.qa.niffler.api.SpendService;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import org.junit.jupiter.api.Assertions;

import javax.annotation.Nonnull;
import java.io.IOException;

public class SpendRestClient extends BaseRestClient {

    private final SpendService spendService = retrofit.create(SpendService.class);

    public SpendRestClient() {
        super(Config.getConfig().getSpendServiceBaseUrl());
    }

    public @Nonnull SpendJson addSpend(SpendJson spend) {
        try {
            return spendService.addSpend(spend).execute().body();
        } catch (IOException e) {
            Assertions.fail("Can't execute api call to niffler-spend. " + e.getMessage());
            return null;
        }
    }

    public @Nonnull CategoryJson addCategory(CategoryJson category) {
        try {
            return spendService.addCategory(category).execute().body();
        } catch (IOException e) {
            Assertions.fail("Can't execute api call to niffler-spend. " + e.getMessage());
            return null;
        }
    }
}
