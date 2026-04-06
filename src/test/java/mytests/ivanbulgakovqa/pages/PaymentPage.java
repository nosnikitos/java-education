package mytests.ivanbulgakovqa.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class PaymentPage {

    private final SelenideElement currencyDropdown = $(".ant-select-selection-item");
    private final SelenideElement priceValue = $(".styles-module-scss-module__t92_WG__price");
    private final ElementsCollection currenciesInDropdown = $$(".ant-select-item-option-content");

    @Step("Нажать на окно выбора валюты")
    public PaymentPage openCurrencyDropdown() {
        currencyDropdown.shouldBe(visible).click();
        return this;
    }

    @Step("Выбрать валюту - {currency}")
    public PaymentPage selectCurrency(String currency) {
        currenciesInDropdown.findBy(text(currency)).shouldBe(visible).click();
        return this;
    }

    @Step("Проверить, что цена обучения равна {expectedPrice}")
    public void shouldHavePrice(String expectedPrice) {
        priceValue.shouldHave(text(expectedPrice));
    }

}
