package ru.bulgakov.mentor.qa;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.bulgakov.junit.BaseTest;
import ru.bulgakov.mentor.pages.PaymentPage;
import ru.bulgakov.mentor.pages.WelcomePage;
import ru.bulgakov.mentor.pages.WikipediaPage;
import ru.bulgakov.mentor.pages.YandexSearchPage;
import java.util.List;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class QaTest  extends BaseTest {

    @Test
    @DisplayName("Проверить, что цена обучения ментора - 47000 рублей")
    void mentoringPriceShouldBe47000Test() {

        open("https://ya.ru/", YandexSearchPage.class)
                .inputSearchText("bulgakov qa")
                .clickSearchButton()
                .openMentorsSite();

        switchTo().window(1);

        page(WelcomePage.class)
                .clickPrice()
                .clickWantToBeQa()
                .clickRunToPay();

        switchTo().window(2);

        page(PaymentPage.class)
                .openCurrencyDropdown()
                .selectCurrency("RUB")
                .shouldHavePrice("₽ 47 000");
    }

    @Test
    @DisplayName("Проверить страничку Ждуна")
    void jdunCheckArticle() {

        open("https://ya.ru/", YandexSearchPage.class)
                .inputSearchText("Ждун")
                .clickSearchButton()
                .openWikipedia();

        switchTo().window(1);

        WikipediaPage wikipediaPage = page(WikipediaPage.class);

        assertEquals("Ждун", wikipediaPage.getTitleText());
        assertTrue(wikipediaPage.getPageText().contains("интернет-мем"));
    }
}
