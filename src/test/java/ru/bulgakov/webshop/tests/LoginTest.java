package ru.bulgakov.webshop.tests;

import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.bulgakov.junit.BaseTest;
import ru.bulgakov.webshop.pages.DwsBasePage;
import ru.bulgakov.webshop.pages.DwsRegisterPage;

import java.lang.annotation.*;

import static com.codeborne.selenide.Selenide.*;
import static ru.bulgakov.webshop.config.Config.WEB_SHOP_REGISTER_URL;
import static ru.bulgakov.webshop.config.Config.WEB_SHOP_URL;

// --- Аннотация для пропуска регистрации
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@interface SkipRegistration {}

public class LoginTest extends BaseTest {
    private static final Faker faker = new Faker();
    private String email;
    private String password;

    @BeforeEach
    void beforeEach(TestInfo testInfo) {

        // Проверяем, нужно ли пропустить регистрацию
        boolean skip = testInfo.getTestMethod()
                .map(method -> method.isAnnotationPresent(SkipRegistration.class))
                .orElse(false);

        if (skip) return;

        // Генерация email и пароля
        password = faker.harryPotter().character() + faker.number().numberBetween(0, 100);
        email = faker.internet().emailAddress();

        // Регистрация пользователя
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
    @DisplayName("Проверка успешного логина")
    void testSuccessfulLogin() {
        open(WEB_SHOP_URL, DwsBasePage.class)
                .openLogin()
                .inputEmail(email)
                .inputPassword(password)
                .clickLogin()
                .checkUserLoggedIn(email);
    }

    @SkipRegistration
    @ParameterizedTest
    @DisplayName("Проверка вывода ошибки валидации email в логине")
    @ValueSource(strings = {
            "plainaddress",
            "missing@domain",
            "missingdomain@.com",
            "@nouser.com"
    })
    void testInvalidEmailShowsError(String email) {
        open(WEB_SHOP_URL, DwsBasePage.class)
                .openLogin()
                .inputEmail(email)
                .clickLogin()
                .checkEmailValidationErrorAppear();

    }
}
