package guru.qa.niffler.config;

public interface Config {

    static Config getConfig() {
        if ("docker".equals(System.getProperty("env"))) {
            return new DockerConfig();
        }
        return new LocalConfig();
    }
    String getDBHost();
    int getDBPort();
    String getDBLogin();
    String getDBPassword();

}
