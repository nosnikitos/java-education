package mytests.config;

import org.aeonbits.owner.Config;

@Config.Sources({
        "system:properties",
        "classpath:config/${run}.properties"
})
@Config.LoadPolicy(Config.LoadType.MERGE)
public interface WebDriverConfig extends Config {

    @DefaultValue("local")
    String run();

    @DefaultValue("chrome")
    String browser();

    @DefaultValue("128.0")
    String browserVersion();

    @DefaultValue("1920x1080")
    String browserSize();

    String selenoidUrl();
    String selenoidUser();
    String selenoidPassword();

    @DefaultValue("false")
    boolean enableVideo();

    @DefaultValue("false")
    boolean enableVNC();
}
