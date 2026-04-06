package mytests.ivanbulgakovqa.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;


import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class WelcomePage {

    private final SelenideElement priceButton = $$(".t-menu__list li").last();
    private final SelenideElement wantToBeQa = $(byText("Хочу вкатиться в QA"));
    private final SelenideElement runToPay = $(byText("Бегу оплачивать"));

    @Step("Нажать кнопку 'Стоимость'")
    public WelcomePage clickPrice() {
        priceButton.click();
        return this;
    }

    @Step("Нажать кнопку 'Хочу вкатиться в QA'")
    public WelcomePage clickWantToBeQa() {
        wantToBeQa.click();
        return this;
    }

    @Step("Нажать кнопку 'Бегу оплачивать'")
    public PaymentPage clickRunToPay() {
        runToPay.click();
        return new PaymentPage();
    }
}
