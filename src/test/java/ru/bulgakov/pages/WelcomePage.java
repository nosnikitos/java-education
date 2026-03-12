package ru.bulgakov.pages;

import com.codeborne.selenide.SelenideElement;


import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class WelcomePage extends BasePage {

    private final SelenideElement priceButton = $$(".t-menu__list li").last();
    private final SelenideElement wantToBeQa = $(byText("Хочу вкатиться в QA"));
    private final SelenideElement runToPay = $(byText("Бегу оплачивать"));

    public WelcomePage clickPrice() {
        priceButton.click();
        return this;
    }

    public WelcomePage clickWantToBeQa() {
        wantToBeQa.click();
        return this;
    }

    public PaymentPage clickRunToPay() {
        runToPay.click();
        switchToLastWindow();
        return new PaymentPage();
    }
}
