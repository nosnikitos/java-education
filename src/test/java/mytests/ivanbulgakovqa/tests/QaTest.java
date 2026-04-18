package mytests.ivanbulgakovqa.tests;

import io.qameta.allure.*;
import mytests.utils.BrowserUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import mytests.base.BaseTest;
import mytests.ivanbulgakovqa.pages.PaymentPage;
import mytests.ivanbulgakovqa.pages.WelcomePage;
import mytests.ivanbulgakovqa.pages.WikipediaPage;

import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;
import static mytests.config.Config.BULGAKOV_URL;
import static mytests.config.Config.WIKIPEDIA_URL;

public class QaTest extends BaseTest {

    @Test
    @DisplayName("Проверить, что цена обучения ментора - 47000 рублей")
    @Description("Проверяем, что отображаемая цена обучения на сайте корректна")
    @Tags({@Tag("UI"), @Tag("positive")})
    @Severity(SeverityLevel.NORMAL)
    @Owner("nosnikitos")
    @Link(name = "TASK-001", url = "https://...")
    void testMentoringPriceShouldBe47000Test() {

        step("Открыть сайт ментора - " + BULGAKOV_URL, () -> open(BULGAKOV_URL));

        page(WelcomePage.class)
                .clickPrice()
                .clickWantToBeQa()
                .clickRunToPay();


        step("Перейти на новую вкладку", () -> {
            sleep(1000); //окно не сразу открывается
            BrowserUtils.switchToNextWindow();
        });

        page(PaymentPage.class)
                .openCurrencyDropdown()
                .selectCurrency("RUB")
                .shouldHavePrice("₽ 47 000");
    }

    @Test
    @DisplayName("Проверить страничку Ждуна")
    @Description("Проверяем корректность названия страницы и страница содержит информацию о том, что это интернет-мем")
    @Tags({@Tag("UI"), @Tag("positive")})
    @Severity(SeverityLevel.NORMAL)
    @Owner("nosnikitos")
    @Link(name = "TASK-002", url = "https://...")
    void testJdunCheckArticle() {

        step("Открыть сайт Википедии - " + WIKIPEDIA_URL, () -> open(WIKIPEDIA_URL));

        page(WikipediaPage.class)
                .inputSearchText("Ждун")
                .clickSearchButton()
                .checkTitleText("Ждун")
                .checkPageText("интернет-мем");
    }
}
