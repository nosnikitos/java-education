package mytests.demowebshop.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static mytests.config.Config.WEB_SHOP_LOGIN_URL;

public class DwsLoginPage {

    private final SelenideElement loginPageTitle = $("div.page-title");
    private final SelenideElement fieldEmail = $("input#Email");
    private final SelenideElement fieldPassword = $("input#Password");
    private final SelenideElement checkBoxRememberMe = $("input#RememberMe");
    private final SelenideElement loginButton = $("input.login-button");
    private final ElementsCollection myAccount = $$("div.header-links ul li a");
    private final SelenideElement fieldValidationError = $("span.field-validation-error");


    @Step("Открыть страницу авторизации - " + WEB_SHOP_LOGIN_URL)
    public DwsLoginPage openLogin() {
        open(WEB_SHOP_LOGIN_URL);
        return this;
    }

    @Step("Ввести email - {email}")
    public DwsLoginPage inputEmail(String email) {
        fieldEmail.setValue(email);
        return this;
    }

    @Step("Ввести пароль - {password}")
    public DwsLoginPage inputPassword(String password) {
        fieldPassword.setValue(password);
        return this;
    }

    @Step("Нажать кнопку Логин")
    public DwsBasePage clickLogin() {
        loginButton.click();
        return new DwsBasePage();
    }

    @Step("Проверить, что появилась ошибка валидации email в форме логина")
    public void checkEmailValidationErrorAppear() {
        fieldValidationError.shouldBe(visible);
    }
}
