package ru.bulgakov.webshop.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Condition.text;

public class DwsBasePage {

    private final SelenideElement registerButton = $("a.ico-register");
    private final SelenideElement loginButton = $("a.ico-login");
    private final SelenideElement cartButton = $("a.ico-cart");
    private final ElementsCollection topMenu = $$("ul.top-menu li");
    private final ElementsCollection topSubMenu = $$("ul.top-menu li ul li");



    public DwsRegisterPage openRegister() {
        registerButton
                .click();
        return new DwsRegisterPage();
    }

    public DwsLoginPage openLogin() {
        loginButton
                .click();
        return new DwsLoginPage();
    }

    public DwsBasePage hoverTopMenu(String topMenuItem) {
        // наводим на кнопки меню курсор
        topMenu
                .findBy(text(topMenuItem))
                .hover();
        return this;
    }

    public DwsCatalogPage clickTopMenu(String topMenuItem) {
        //кликаем по кнопке меню
        topMenu
                .findBy(text(topMenuItem))
                .click();
        return new DwsCatalogPage();
    }

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

    public DwsCartPage openCart() {
        cartButton.click();
        return new DwsCartPage();
    }

}
