package guru.qa.niffler.tests.grpc;

import com.google.protobuf.Empty;
import guru.qa.grpc.niffler.grpc.NifflerCurrencyServiceGrpc;
import guru.qa.niffler.config.Config;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.qameta.allure.grpc.AllureGrpc;

public class BaseGrpcTest {

    protected static final Config CFG = Config.getConfig();
    protected static final Empty EMPTY = Empty.getDefaultInstance();
    private static final Channel channel;

    // channel that describe where we should 'go'
    static {
        channel = ManagedChannelBuilder
                .forAddress(CFG.getCurrencyGrpcAddress(), CFG.getCurrencyGrpcPort())
                .intercept(new AllureGrpc())
                .usePlaintext() // By default, a secure connection mechanism such as TLS will be used.
                .build();
    }

    // stub to perform requests
    protected final NifflerCurrencyServiceGrpc.NifflerCurrencyServiceBlockingStub currencyStub
            = NifflerCurrencyServiceGrpc.newBlockingStub(channel);
}
