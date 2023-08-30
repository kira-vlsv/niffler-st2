package guru.qa.niffler.service;

import com.google.protobuf.Empty;
import guru.qa.grpc.niffler.grpc.*;
import guru.qa.niffler.data.CurrencyEntity;
import guru.qa.niffler.data.repository.CurrencyRepository;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import static guru.qa.niffler.data.CurrencyValues.*;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GrpcCurrencyServiceTest {

    GrpcCurrencyService grpcCurrencyService;
    List<CurrencyEntity> testCurrencies;

    @BeforeEach
    void setUp(@Mock CurrencyRepository currencyRepository) {
        CurrencyEntity rub = new CurrencyEntity();
        rub.setCurrency(RUB);
        rub.setCurrencyRate(0.015);
        CurrencyEntity usd = new CurrencyEntity();
        usd.setCurrency(USD);
        usd.setCurrencyRate(1.0);
        CurrencyEntity eur = new CurrencyEntity();
        eur.setCurrency(EUR);
        eur.setCurrencyRate(1.08);
        CurrencyEntity kzt = new CurrencyEntity();
        kzt.setCurrency(KZT);
        kzt.setCurrencyRate(0.0021);

        testCurrencies = List.of(rub, kzt, eur, usd);

        lenient()
                .when(currencyRepository.findAll())
                .thenReturn(testCurrencies);

        grpcCurrencyService = new GrpcCurrencyService(currencyRepository);
    }


    static Stream<Arguments> convertSpendTo() {
        return Stream.of(
                Arguments.of(150.00, guru.qa.grpc.niffler.grpc.CurrencyValues.RUB, guru.qa.grpc.niffler.grpc.CurrencyValues.KZT, 1071.43),
                Arguments.of(34.00, guru.qa.grpc.niffler.grpc.CurrencyValues.USD, guru.qa.grpc.niffler.grpc.CurrencyValues.EUR, 31.48),
                Arguments.of(150.00, guru.qa.grpc.niffler.grpc.CurrencyValues.RUB, guru.qa.grpc.niffler.grpc.CurrencyValues.RUB, 150.00),
                Arguments.of(0.00, guru.qa.grpc.niffler.grpc.CurrencyValues.KZT, guru.qa.grpc.niffler.grpc.CurrencyValues.RUB, 0.00)
        );
    }

    @MethodSource
    @ParameterizedTest
    void convertSpendTo(double spend,
                        guru.qa.grpc.niffler.grpc.CurrencyValues spendCurrency,
                        guru.qa.grpc.niffler.grpc.CurrencyValues desiredCurrency,
                        double expectedResult) {

        BigDecimal result = grpcCurrencyService.convertSpendTo(spend, spendCurrency,
                desiredCurrency, testCurrencies);

        Assertions.assertEquals(expectedResult, result.doubleValue());
    }

    @Test
    void calculateRate() {
        StreamObserver streamObserverMock = Mockito.mock(StreamObserver.class);
        CalculateRequest request = CalculateRequest.newBuilder()
                .setSpendCurrency(CurrencyValues.EUR)
                .setDesiredCurrency(CurrencyValues.RUB)
                .setAmount(1000)
                .build();
        grpcCurrencyService.calculateRate(request, streamObserverMock);

        // Verify that the onNext() method was called on the mock object
        verify(streamObserverMock)
                .onNext(refEq(CalculateResponse.newBuilder()
                        .setCalculatedAmount(72000.0)
                        .build()));

        verify(streamObserverMock).onCompleted();
    }

    static Stream<Arguments> courseForCurrency() {
        return Stream.of(
                Arguments.of(CurrencyValues.KZT, new BigDecimal("0.0021")),
                Arguments.of(CurrencyValues.RUB, new BigDecimal("0.015")),
                Arguments.of(CurrencyValues.EUR, new BigDecimal("1.08")),
                Arguments.of(CurrencyValues.USD, new BigDecimal("1.0"))
        );
    }

    @MethodSource
    @ParameterizedTest
    void courseForCurrency(CurrencyValues currency, BigDecimal expectedValue) {
        BigDecimal actualValue = grpcCurrencyService
                .courseForCurrency(currency, testCurrencies);
        Assertions.assertEquals(expectedValue, actualValue);
    }

    @Test
    void getAllCurrencies() {
        StreamObserver streamObserverMock = Mockito.mock(StreamObserver.class);
        CurrencyResponse response = CurrencyResponse.newBuilder()
                .addAllAllCurrencies(testCurrencies.stream().map(e -> Currency.newBuilder()
                                .setCurrency(CurrencyValues.valueOf(e.getCurrency().name()))
                                .setCurrencyRate(e.getCurrencyRate())
                                .build())
                        .toList())
                .build();

        grpcCurrencyService.getAllCurrencies(Empty.newBuilder().build(), streamObserverMock);
        verify(streamObserverMock).onNext(refEq(response));
        verify(streamObserverMock).onCompleted();
    }
}