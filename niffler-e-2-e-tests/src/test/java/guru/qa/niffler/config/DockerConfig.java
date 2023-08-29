package guru.qa.niffler.config;

import com.codeborne.selenide.Configuration;

public class DockerConfig implements Config {

    static final DockerConfig INSTANCE = new DockerConfig();

    static {
        Configuration.browser = "chrome";
        Configuration.browserVersion = "110.0";
        Configuration.remote = "http://selenoid:4444/wd/hub";
        Configuration.browserSize = "1920x1080";
        Configuration.timeout = 10000;
    }

    private DockerConfig() {
    }

    @Override
    public String getDBHost() {
        return "niffler-all-db";
    }

    @Override
    public int getDBPort() {
        return 5432;
    }

    @Override
    public String getDBLogin() {
        return "postgres";
    }

    @Override
    public String getDBPassword() {
        return "secret";
    }

    @Override
    public String getSpendServiceBaseUrl() {
        return "niffler-spend";
    }

    @Override
    public String getUserServiceBaseUrl() {
        return "niffler-userdata";
    }

    @Override
    public String getFrontUrl() {
        return "http://niffler-frontend:3000/";
    }

    @Override
    public String getAuthUrl() {
        return "http://niffler-auth:9000/";
    }

    @Override
    public String getCurrencyGrpcAddress() {
        return "currency.niffler.dc";
    }

    @Override
    public int getCurrencyGrpcPort() {
        return 8092;
    }
}
