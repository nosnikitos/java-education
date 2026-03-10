package ru.bulgakov.qa;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class QaTest {

    @Test
    void mentoringPriceShouldBe47000Test() {
        /*
        * Тест-кейс - проверить, что предоплата по обучению - 4700 рублей
        * 1. открыть поисковик (яндекс)
        * 2. ввести данные сайта
        * 3. нажать кнопку поиск
        * 4. в поисковой выдаче найти нужный сайт, кликнуть на него
        * 5. нажать на кнопку "Стоимость"
        * 6. нажать кнопку "Хочу вкатиться в QA"
        * 7. нажать на кнопку "Бегу оплачивать"
        * 9. проверить что к оплате 47000 рублей
         */

        open("https://ya.ru/");
        $("#text").setValue("bulgakov qa");
        $("[type=submit]").click();
        $(byText("ivanbulgakovqa.ru")).click();

        switchTo().window(1);
        $$(".t-menu__list li").last().click();
        $x("/html/body/div[1]/div[42]/div/div/div[32]/div/a").click();
        $(byText("Бегу оплачивать")).click();

        switchTo().window(2);
        $(".styles-module-scss-module__t92_WG__price").shouldHave(text("₽ 47 000 "));
    }

    @Test
    void jdunCheckArticle() {
        /*
        Тест-кейс, который проверяет, что на википедии написано, что Ждун это гомункул слоноподобный
        1. Открыть Википедию
        2. Ввести в поиске "Ждун"
        3. Нажать поиск
        4. Проверить, что в статье есть упоминание, что это "интернет-мем"
        5. Проверить, что в содержании 7 пунктов
        6. Проверяем названия пунктов содержания
         */
        Configuration.holdBrowserOpen = true;

        open("https://ru.wikipedia.org/");
        $(".vector-search-box-input").setValue("Ждун");
        $(".searchButton").click();

        //  проверяем, что статья называется "Ждун"
        $(".mw-page-title-main").shouldHave(text("Ждун"));

        //  проверяем, что в статье есть упоминание о том, что это интернет-мем
        $("body").shouldHave(text("интернет-мем"));

        //  проверяем, что содержание содержит 7 пунктов
        $$(".toc li").shouldHave(CollectionCondition.size(7));

        // проверяем, что каждый пункт оглавления содержит правильный текст
        $$("#toc .toctext").shouldHave(CollectionCondition.texts(
                "Общие сведения",
                "Создание скульптуры",
                "Мем",
                "Ждун в массовой культуре",
                "См. также",
                "Примечания",
                "Ссылки"));
    }
}
