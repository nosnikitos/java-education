package ru.bulgakov.qa;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.bulgakov.pages.YandexSearchPage;
import java.util.List;
import static com.codeborne.selenide.Selenide.*;

public class QaTest {

    @Test
    @DisplayName("Проверить, что цена обучения ментора - 47000 рублей")
    void mentoringPriceShouldBe47000Test() {

        YandexSearchPage yaSearchPage = new YandexSearchPage();
        open("https://ya.ru/");
        yaSearchPage
                .inputSearchText("bulgakov qa")
                .clickSearchButton()
                .openMentorsSite()
                .clickPrice()
                .clickWantToBeQa()
                .clickRunToPay()
                .openCurrencyDropdown()
                .selectCurrency("RUB")
                .shouldHavePrice("₽ 47 000");
    }

    @Test
    @DisplayName("Проверить страничку Ждуна")
    void jdunCheckArticle() {

        open("https://ya.ru/");
        YandexSearchPage yandexSearchPage = new YandexSearchPage();
        yandexSearchPage
                .inputSearchText("Ждун")
                .clickSearchButton()
                .openWikipedia()
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
