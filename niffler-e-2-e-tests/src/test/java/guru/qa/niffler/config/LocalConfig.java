package guru.qa.niffler.config;

public class LocalConfig implements Config {
    @Override
    public String getDBHost() {
        return "localhost";
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
}
