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
    private static Channel channel;

    static {
        channel = ManagedChannelBuilder
                .forAddress(CFG.getCurrencyGrpcAddress(), CFG.getCurrencyGrpcPort())
                .intercept(new AllureGrpc())
                .usePlaintext()
                .build();
    }

    protected final NifflerCurrencyServiceGrpc.NifflerCurrencyServiceBlockingStub currencyStub
            = NifflerCurrencyServiceGrpc.newBlockingStub(channel);
}
