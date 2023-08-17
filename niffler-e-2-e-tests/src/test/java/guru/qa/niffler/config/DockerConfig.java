package guru.qa.niffler.config;

public class DockerConfig implements Config {
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
