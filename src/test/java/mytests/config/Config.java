package mytests.config;

import org.aeonbits.owner.ConfigFactory;
import org.openqa.selenium.chrome.ChromeOptions;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Config {

    public static final String WEB_SHOP_URL = "https://demowebshop.tricentis.com";
    public static final String WEB_SHOP_REGISTER_URL = WEB_SHOP_URL + "/register";
    public static final String WEB_SHOP_LOGIN_URL = WEB_SHOP_URL + "/login";
    public static final String WIKIPEDIA_URL = "https://ru.wikipedia.org";
    public static final String BULGAKOV_URL = "https://ivanbulgakovqa.ru";


    private static final WebDriverConfig config = ConfigFactory.create(WebDriverConfig.class, System.getProperties());
    public static WebDriverConfig getWebDriverConfig() {
        return config;
    }

    public static ChromeOptions getSelemoidChromeOptions() {

        WebDriverConfig cfg = getWebDriverConfig();

        ChromeOptions options = new ChromeOptions();
        options.setCapability("browserVersion", cfg.browserVersion());

        Map<String, Object> selenoidOptions = new HashMap<>();
        selenoidOptions.put("name", "NTest");                    // имя сессии
        selenoidOptions.put("sessionTimeout", "15m");                   // таймаут сессии
        selenoidOptions.put("env", List.of("TZ=UTC"));               // временная зона
        selenoidOptions.put("labels", Map.of("manual", "true")); // кнопка ручного удаления
        selenoidOptions.put("enableVideo", cfg.enableVideo());                       // запись видео
        selenoidOptions.put("enableVNC", cfg.enableVNC());                         // просмотр сессии онлайн

        options.setCapability("selenoid:options", selenoidOptions);
        return options;
    }
}
