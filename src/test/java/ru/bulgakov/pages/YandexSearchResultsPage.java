package ru.bulgakov.pages;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class YandexSearchResultsPage extends BasePage {


    public WelcomePage openMentorsSite() {
        $(byText("ivanbulgakovqa.ru")).click();
        switchToLastWindow();
        return new WelcomePage();
    }

    public WikipediaPage openWikipedia() {
        $(byText("ru.wikipedia.org")).click();
        switchToLastWindow();
        return new WikipediaPage();
    }

}
