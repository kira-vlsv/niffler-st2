package guru.qa.niffler.tests.grpc;

import guru.qa.grpc.niffler.grpc.CalculateRequest;
import guru.qa.grpc.niffler.grpc.CalculateResponse;
import guru.qa.grpc.niffler.grpc.CurrencyResponse;
import guru.qa.grpc.niffler.grpc.CurrencyValues;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;

class NifflerCurrencyGrpcTest extends BaseGrpcTest {

    @Test
    void getAllCurrenciesTest() {
        CurrencyResponse response = currencyStub.getAllCurrencies(EMPTY);
        assertEquals(4, response.getAllCurrenciesList().size());

        step("Verify RUB currency", () -> {
            assertEquals(CurrencyValues.RUB, response.getAllCurrenciesList().get(0).getCurrency());
            assertEquals(0.015, response.getAllCurrenciesList().get(0).getCurrencyRate());
        });
        step("Verify KZT currency", () -> {
            assertEquals(CurrencyValues.KZT, response.getAllCurrenciesList().get(1).getCurrency());
            assertEquals(0.0021, response.getAllCurrenciesList().get(1).getCurrencyRate());
        });
        step("Verify EUR currency", () -> {
            assertEquals(CurrencyValues.EUR, response.getAllCurrenciesList().get(2).getCurrency());
            assertEquals(1.08, response.getAllCurrenciesList().get(2).getCurrencyRate());
        });
        step("Verify USD currency", () -> {
            assertEquals(CurrencyValues.USD, response.getAllCurrenciesList().get(3).getCurrency());
            assertEquals(1.0, response.getAllCurrenciesList().get(3).getCurrencyRate());
        });
    }

    static Stream<Arguments> calculateRateTest() {
        return Stream.of(
                Arguments.of(CurrencyValues.EUR, CurrencyValues.RUB, 2345.0, 168840.0),
                Arguments.of(CurrencyValues.RUB, CurrencyValues.EUR, 2458.99, 34.15),
                Arguments.of(CurrencyValues.RUB, CurrencyValues.RUB, 100.0, 100.0),
                Arguments.of(CurrencyValues.USD, CurrencyValues.EUR, 0.50, 0.46),
                Arguments.of(CurrencyValues.KZT, CurrencyValues.USD, 7800900, 16381.89)
        );
    }

    @MethodSource
    @ParameterizedTest(name = "When exchange {0} to {1} amount of {2} expect response {3}")
    void calculateRateTest(CurrencyValues spendCurrency,
                           CurrencyValues desiredCurrency,
                           double amount,
                           double expectedAmount
    ) {
        CalculateResponse calculateResponse = currencyStub.calculateRate(CalculateRequest.newBuilder()
                .setSpendCurrency(spendCurrency)
                .setDesiredCurrency(desiredCurrency)
                .setAmount(amount)
                .build()
        );
        assertEquals(expectedAmount, calculateResponse.getCalculatedAmount());
    }
}
