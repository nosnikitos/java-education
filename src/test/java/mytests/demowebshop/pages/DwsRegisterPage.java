package mytests.demowebshop.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import java.util.Random;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static mytests.config.Config.WEB_SHOP_REGISTER_URL;

public class DwsRegisterPage {

    private final SelenideElement pageTitle = $("div.page-title");
    private final SelenideElement maleGenderRadio = $("input#gender-male");
    private final SelenideElement femaleGenderRadio = $("input#gender-female");
    private final SelenideElement fieldFirstName = $("input#FirstName");
    private final SelenideElement fieldLastName = $("input#LastName");
    private final SelenideElement fieldEmail = $("input#Email");
    private final SelenideElement fieldPassword = $("input#Password");
    private final SelenideElement fieldConfirmPassword = $("input#ConfirmPassword");
    private final SelenideElement registerButton = $("input#register-button");
    private final SelenideElement registerResultText = $("div.result");
    private final SelenideElement myAccount = $("div.header-links li a.account")
            ;

    public void register(String firstName, String lastName, String email, String password) {
        openRegistration()
                .checkRegisterOpened()
                .selectRandomGender()
                .inputFirstName(firstName)
                .inputLastName(lastName)
                .inputEmail(email)
                .inputPassword(password)
                .inputConfirmPassword(password)
                .clickRegisterButton();
    }

    @Step("Открыть страницу регистрации - " + WEB_SHOP_REGISTER_URL)
    public DwsRegisterPage openRegistration() {
        open(WEB_SHOP_REGISTER_URL);
        return this;
    }

    @Step("Выбрать случайный Gender")
    public DwsRegisterPage selectRandomGender() {
        SelenideElement[] genderRadio = { maleGenderRadio, femaleGenderRadio };
        int randomIndex = new Random().nextInt(genderRadio.length);
        genderRadio[randomIndex].click();
        return this;
    }

    @Step("Ввести First name - {firstName}")
    public DwsRegisterPage inputFirstName(String firstName) {
        fieldFirstName.setValue(firstName);
        return this;
    }

    @Step("Ввести Last name - {lastName}")
    public DwsRegisterPage inputLastName(String lastName) {
        fieldLastName.setValue(lastName);
        return this;
    }

    @Step("Ввести Email - {email}")
    public DwsRegisterPage inputEmail(String email) {
        fieldEmail.setValue(email);
        return this;
    }

    @Step("Ввести Password - {password}")
    public DwsRegisterPage inputPassword(String password) {
        fieldPassword.setValue(password);
        return this;
    }

    @Step("Ввести Confirm password - {confirmPassword}")
    public DwsRegisterPage inputConfirmPassword(String confirmPassword) {
        fieldConfirmPassword.setValue(confirmPassword);
        return this;
    }

    @Step("Нажать на кнопку Register")
    public DwsRegisterPage clickRegisterButton() {
        registerButton.click();
        return this;
    }

    @Step("Проверить, что страница регистрации открыта")
    public DwsRegisterPage checkRegisterOpened() {
        pageTitle.shouldHave(text("Register"));
        return this;
    }

    @Step("Проверить, что появилась надпись успешной регистрации")
    public DwsRegisterPage checkRegisterResultText(String resultText) {
        registerResultText.shouldHave(text(resultText));
        return this;
    }
}
