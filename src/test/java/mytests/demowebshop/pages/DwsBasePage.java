package mytests.demowebshop.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static mytests.config.Config.WEB_SHOP_URL;

public class DwsBasePage {

    private final SelenideElement registerButton = $("a.ico-register");
    private final SelenideElement loginButton = $("a.ico-login");
    private final SelenideElement cartButton = $("a.ico-cart");
    private final ElementsCollection topMenu = $$("ul.top-menu li");
    private final ElementsCollection topSubMenu = $$("ul.top-menu li ul li");
    private final SelenideElement myAccount = $("div.header-links li a.account");

    @Step("Открыть главную страницу  - " + WEB_SHOP_URL)
    public DwsBasePage openWebShop() {
        open(WEB_SHOP_URL);
        return this;
    }

    @Step("Нажать на кнопку Регистрация")
    public DwsRegisterPage openRegister() {
        registerButton
                .click();
        return new DwsRegisterPage();
    }

    @Step("Нажать на кнопку Логин")
    public DwsLoginPage openLogin() {
        loginButton
                .click();
        return new DwsLoginPage();
    }

    @Step("Навести курсор на {topMenuItem}")
    public DwsBasePage hoverTopMenu(String topMenuItem) {
        // наводим на кнопки меню курсор
        topMenu
                .findBy(text(topMenuItem))
                .shouldBe(visible)
                .hover();
        return this;
    }

    @Step("Нажать на {topMenuItem}")
    public DwsCatalogPage clickTopMenu(String topMenuItem) {
        //кликаем по кнопке меню
        topMenu
                .findBy(text(topMenuItem))
                .shouldBe(visible)
                .click();
        return new DwsCatalogPage();
    }

    @Step("Нажать на {topSubMenuItem}")
    public DwsCatalogPage clickTopSubMenu(String topSubMenuItem) {
        // клик по подменю, которое стало видимым после hover
        topSubMenu
                .findBy(text(topSubMenuItem))
                .click();
        return new DwsCatalogPage();
    }

    public SelenideElement getSuccessNotification() {
        return $("div.bar-notification.success");
    }

    public SelenideElement getCartQuantity() {
        return $("span.cart-qty");
    }

    @Step("Перейти в Корзину")
    public DwsCartPage clickCart() {
        cartButton.click();
        return new DwsCartPage();
    }

    @Step("Проверить отображение {headerEmail} в шапке сайта")
    public void checkVisibleUserEmail(String headerEmail) {
        myAccount.shouldHave(text(headerEmail));
    }
}
