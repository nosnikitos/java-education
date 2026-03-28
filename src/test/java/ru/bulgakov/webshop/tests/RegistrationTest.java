package ru.bulgakov.webshop.tests;

import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import ru.bulgakov.webshop.pages.DwsBasePage;
import static com.codeborne.selenide.Selenide.*;
import static ru.bulgakov.webshop.config.Config.WEB_SHOP_URL;

public class RegistrationTest {
    private static final Faker faker = new Faker();

    @Test
    void registrationTest() {
        String password = faker.harryPotter().character() + faker.number().numberBetween(0,100);
        String email = faker.internet().emailAddress();

        open(WEB_SHOP_URL, DwsBasePage.class)
                .openRegister()
                .checkRegisterOpened()
                .selectRandomGender()
                .inputFirstName(faker.name().firstName())
                .inputLastName(faker.name().lastName())
                .inputEmail(email)
                .inputPassword(password)
                .inputConfirmPassword(password)
                .clickRegisterButton()
                .checkRegisterResultText("Your registration completed")
                .checkVisibleUserEmail(email);
    }
}
