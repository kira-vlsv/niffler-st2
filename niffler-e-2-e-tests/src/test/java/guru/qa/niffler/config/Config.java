package guru.qa.niffler.config;

public interface Config {

    static Config getConfig() {
        return LocalConfig.INSTANCE;
//        if ("docker".equals(System.getProperty("test.env"))) {
//            return DockerConfig.INSTANCE;
//        } else if ("local".equals(System.getProperty("test.env"))) {
//            return LocalConfig.INSTANCE;
//        } else throw new IllegalStateException("Can`t read 'test.env' System property");
    }

    String getDBHost();

    int getDBPort();

    String getDBLogin();

    String getDBPassword();

    String getSpendServiceBaseUrl();

    String getUserServiceBaseUrl();

    String getFrontUrl();

    String getAuthUrl();

    String getCurrencyGrpcAddress();

    int getCurrencyGrpcPort();
}
