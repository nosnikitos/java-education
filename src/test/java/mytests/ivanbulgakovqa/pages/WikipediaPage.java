package mytests.ivanbulgakovqa.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class WikipediaPage {

    private final SelenideElement titleText = $(".mw-page-title-main"),
                              body = $("body");
    private final SelenideElement searchInput = $("input.cdx-text-input__input[name='search'][accesskey='f']");
    private final SelenideElement searchButton = $("button.cdx-search-input__end-button");

    @Step("Ввести в поиске - {searchText}")
    public WikipediaPage inputSearchText(String searchText) {
        searchInput.setValue(searchText);
        return this;
    }

    @Step("Нажать кнопку 'Найти'")
    public WikipediaPage clickSearchButton() {
        searchButton
                .shouldHave(text("Найти"))
                .click();
        return this;
    }

    @Step("Проверить, что название статьи - {expectedTitleText}")
    public WikipediaPage checkTitleText(String expectedTitleText) {
        titleText.shouldHave(text(expectedTitleText));
        return this;
    }

    @Step("Проверить, что в статье есть текст - {expectedPageText}")
    public void checkPageText(String expectedPageText) {
        body.shouldHave(text(expectedPageText));
    }
}
