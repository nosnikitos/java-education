package ru.bulgakov.webshop.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DwsLoginPage {

    private final SelenideElement loginPageTitle = $("div.page-title");
    private final SelenideElement fieldEmail = $("input#Email");
    private final SelenideElement fieldPassword = $("input#Password");
    private final SelenideElement checkBoxRememberMe = $("input#RememberMe");
    private final SelenideElement loginButton = $("input.login-button");
    private final ElementsCollection myAccount = $$("div.header-links ul li a");


    public DwsLoginPage inputEmail(String email) {
        fieldEmail.setValue(email);
        return this;
    }

    public DwsLoginPage inputPassword(String password) {
        fieldPassword.setValue(password);
        return this;
    }

    public DwsLoginPage clickLogin() {
        loginButton.click();
        return this;
    }

    public DwsLoginPage checkUserLoggedIn(String headerEmail) {
        myAccount.get(0).shouldHave(text(headerEmail));
        return this;
    }
}
