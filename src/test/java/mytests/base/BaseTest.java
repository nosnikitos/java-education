package mytests.base;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import mytests.utils.AttachManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;


import static com.codeborne.selenide.Selenide.clearBrowserCookies;
import static com.codeborne.selenide.Selenide.clearBrowserLocalStorage;
import static io.qameta.allure.Allure.step;


public class BaseTest {

    @BeforeAll
    static void setUpSelenideLogger() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide( ));
    }

    @BeforeAll
    static void setUpBrowserSize() {
        Configuration.browserSize = "1920x1080";
    }

    @AfterEach
    void tearDown() {
        AttachManager.attachAll();
        step("Clear Browser", () -> {
            clearBrowserCookies();
            clearBrowserLocalStorage();
        });
    }
}
