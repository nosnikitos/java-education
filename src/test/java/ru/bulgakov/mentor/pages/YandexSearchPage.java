package ru.bulgakov.mentor.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class YandexSearchPage {

    private final SelenideElement searchInput = $("#text");
    private final SelenideElement searchButton = $("[type=submit]");

    public YandexSearchPage inputSearchText(String query) {
        searchInput.setValue(query);
        return this;
    }

    public YandexSearchResultsPage clickSearchButton() {
        searchButton.click();
        return new YandexSearchResultsPage();
    }
}
