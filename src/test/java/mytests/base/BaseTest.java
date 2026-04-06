package mytests.base;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;


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
    void clearBrowser() {
        Selenide.clearBrowserLocalStorage();
        Selenide.clearBrowserLocalStorage();
    }
}
