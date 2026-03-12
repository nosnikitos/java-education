package ru.bulgakov.pages;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.SelenideElement;

import java.util.List;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class WikipediaPage {

    private final SelenideElement titleText = $(".mw-page-title-main");
    private final SelenideElement body = $("body");

    public WikipediaPage checkTitleText(String expectedTitleText) {
        titleText.shouldHave(text(expectedTitleText));
        return this;
    }

    public WikipediaPage checkTextOnPage(String expectedTextOnPage) {
        body.shouldHave(text(expectedTextOnPage));
        return this;
    }

    public WikipediaPage checkTocSize(int tocNumber) {
        $$(".toc li").shouldHave(CollectionCondition.size(tocNumber));
        return this;
    }

    public WikipediaPage checkTocText(List<String> expectedTocTexts) {
        $$("#toc .toctext").shouldHave(CollectionCondition.texts(expectedTocTexts));
        return this;
    }
}
