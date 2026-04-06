package mytests.demowebshop.tests;

import io.qameta.allure.*;
import net.datafaker.Faker;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import mytests.base.BaseTest;
import mytests.demowebshop.pages.DwsLoginPage;
import mytests.demowebshop.pages.DwsRegisterPage;

import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;
import static mytests.config.Config.*;


public class LoginTest extends BaseTest {
    private static final Faker faker = new Faker();
    private String email;
    private String password;

    @Nested
    public class PositiveTests {

        @BeforeEach
        void registerNewUser() {

        // Генерация email и пароля
        password = faker.harryPotter().character() + faker.number().numberBetween(0, 100);
        email = faker.internet().emailAddress();

        page(DwsRegisterPage.class)
                .register(
                        faker.name().firstName(),
                        faker.name().lastName(),
                        email,
                        password);

        step("Почистить браузер", () -> {
            clearBrowserCookies();
            clearBrowserLocalStorage();
        });
    }

        @Test
        @DisplayName("Успешная авторизация")
        @Description("Проверяем успешную авторизацию(логин) на UI")
        @Tag("positive")
        @Severity(SeverityLevel.CRITICAL)
        @Owner("nosnikitos")
        @Link(name = "TASK-004", url = "https://...")
        void testSuccessfulLogin() {

            page(DwsLoginPage.class)
                    .openLogin()
                    .inputEmail(email)
                    .inputPassword(password)
                    .clickLogin()
                    .checkVisibleUserEmail(email);
        }}

    @ParameterizedTest
    @DisplayName("Вывод ошибки валидации email в логине")
    @Description("Проверка, что при вводе невалидного емейла в форме логина выходит ошибка валидации")
    @Tag("negative")
    @Severity(SeverityLevel.NORMAL)
    @Owner("nosnikitos")
    @Link(name = "TASK-005", url = "https://...")
    @ValueSource(strings = {
            "plainaddress",
            "missing@domain",
            "missingdomain@.com",
            "@nouser.com"
    })
    void testInvalidEmailShowsError(String email) {

        page(DwsLoginPage.class)
                .openLogin()
                .inputEmail(email)
                .inputPassword("234234")
                .checkEmailValidationErrorAppear();

    }
}
