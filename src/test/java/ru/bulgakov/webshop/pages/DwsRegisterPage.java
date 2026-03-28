package ru.bulgakov.webshop.pages;

import com.codeborne.selenide.SelenideElement;

import java.util.Random;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

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
        checkRegisterOpened()
                .selectRandomGender()
                .inputFirstName(firstName)
                .inputLastName(lastName)
                .inputEmail(email)
                .inputPassword(password)
                .inputConfirmPassword(password)
                .clickRegisterButton();
    }

    public DwsRegisterPage selectRandomGender() {
        SelenideElement[] genderRadio = { maleGenderRadio, femaleGenderRadio };
        int randomIndex = new Random().nextInt(genderRadio.length);
        genderRadio[randomIndex].click();
        return this;
    }

    public DwsRegisterPage inputFirstName(String firstName) {
        fieldFirstName.setValue(firstName);
        return this;
    }

    public DwsRegisterPage inputLastName(String lastName) {
        fieldLastName.setValue(lastName);
        return this;
    }

    public DwsRegisterPage inputEmail(String email) {
        fieldEmail.setValue(email);
        return this;
    }

    public DwsRegisterPage inputPassword(String password) {
        fieldPassword.setValue(password);
        return this;
    }

    public DwsRegisterPage inputConfirmPassword(String confirmPassword) {
        fieldConfirmPassword.setValue(confirmPassword);
        return this;
    }

    public DwsRegisterPage clickRegisterButton() {
        registerButton.click();
        return this;
    }

    public DwsRegisterPage checkRegisterOpened() {
        pageTitle.shouldHave(text("Register"));
        return this;
    }

    public DwsRegisterPage checkRegisterResultText(String resultText) {
        registerResultText.shouldHave(text(resultText));
        return this;
    }

    public DwsRegisterPage checkVisibleUserEmail(String headerEmail) {
        myAccount.shouldHave(text(headerEmail));
        return this;
    }
}
