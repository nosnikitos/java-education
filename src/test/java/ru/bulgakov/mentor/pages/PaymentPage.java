package ru.bulgakov.mentor.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class PaymentPage {

    private final SelenideElement currencyDropdown = $(".ant-select-selection-item");
    private final SelenideElement priceValue = $(".styles-module-scss-module__t92_WG__price");

    public PaymentPage openCurrencyDropdown() {
        sleep(3000);
        currencyDropdown.click();
        return this;
    }

    public PaymentPage selectCurrency(String currency) {
        sleep(2000);
        $$(".ant-select-item-option-content").findBy(text(currency)).shouldBe(visible).click();
        return this;
    }

    public PaymentPage shouldHavePrice(String expectedPrice) {
        priceValue.shouldHave(text(expectedPrice));
        return this;
    }

}
