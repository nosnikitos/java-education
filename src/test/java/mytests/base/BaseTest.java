package mytests.base;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;

import mytests.config.Config;
import mytests.config.WebDriverConfig;
import mytests.utils.AttachManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import static com.codeborne.selenide.Selenide.clearBrowserCookies;
import static com.codeborne.selenide.Selenide.clearBrowserLocalStorage;
import static io.qameta.allure.Allure.step;


public class BaseTest {
   private static final WebDriverConfig cfg = Config.getWebDriverConfig();

    @BeforeAll
    static void setUp() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide( ));
        Configuration.browserSize = cfg.browserSize();
        Configuration.browser = cfg.browser();

        if ("remote".equals(cfg.run())) {
            Configuration.remote =
                    "https://" + cfg.selenoidUser() + ":" + cfg.selenoidPassword() + "@" + cfg.selenoidUrl();
            Configuration.browserCapabilities = Config.getSelemoidChromeOptions();
        }

    }

    @AfterEach
    void tearDown() {
        AttachManager.takeScreenshot();
        AttachManager.getPageSource();
        AttachManager.getBrowserConsoleLogs();
        if ("remote".equals(cfg.run())) {
            AttachManager.addVideo();
        }

        step("Clear Browser", () -> {
            clearBrowserCookies();
            clearBrowserLocalStorage();
        });
    }
}
