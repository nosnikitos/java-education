package ru.bulgakov.webshop.tests;

import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.bulgakov.webshop.pages.DwsBasePage;
import ru.bulgakov.webshop.pages.DwsRegisterPage;

import static com.codeborne.selenide.Selenide.*;
import static ru.bulgakov.webshop.config.Config.WEB_SHOP_REGISTER_URL;
import static ru.bulgakov.webshop.config.Config.WEB_SHOP_URL;

public class LoginTest {
    private static final Faker faker = new Faker();
    private String email;
    private String password;

    @BeforeEach
    void beforeEach() {
        password = faker.harryPotter().character() + faker.number().numberBetween(0, 100);
        email = faker.internet().emailAddress();

        open(WEB_SHOP_REGISTER_URL, DwsRegisterPage.class)
                .register(
                        faker.name().firstName(),
                        faker.name().lastName(),
                        email,
                        password);

        clearBrowserCookies();
        clearBrowserLocalStorage();

    }

    @Test
    void successLoginTest() {
        open(WEB_SHOP_URL, DwsBasePage.class)
                .openLogin()
                .inputEmail(email)
                .inputPassword(password)
                .clickLogin();
    }
}
