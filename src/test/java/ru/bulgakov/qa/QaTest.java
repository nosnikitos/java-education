package ru.bulgakov.qa;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.bulgakov.pages.PaymentPage;
import ru.bulgakov.pages.WelcomePage;
import ru.bulgakov.pages.WikipediaPage;
import ru.bulgakov.pages.YandexSearchPage;
import java.util.List;
import static com.codeborne.selenide.Selenide.*;

public class QaTest {

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

        page(WikipediaPage.class)
                .checkTitleText("Ждун")
                .checkTextOnPage("интернет-мем")
                .checkTocSize(8)
                .checkTocText(List.of("Общие сведения",
                        "Создание скульптуры",
                        "Мем",
                        "Как прозвище (с 2022 года)",
                        "Ждун в массовой культуре",
                        "См. также",
                        "Примечания",
                        "Ссылки"));
    }
}
