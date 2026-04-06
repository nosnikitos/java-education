package mytests.demowebshop.tests;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.*;
import mytests.demowebshop.pages.DwsRegisterPage;
import net.datafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import mytests.base.BaseTest;
import mytests.demowebshop.pages.DwsBasePage;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;
import static mytests.config.Config.*;
import static mytests.config.Config.WEB_SHOP_LOGIN_URL;

public class RegistrationTest extends BaseTest {
    private static final Faker faker = new Faker();

    @Test
    @DisplayName("Успешная регистрация нового пользователя")
    @Description("Регистрируем нового пользователя со случайными данными через UI")
    @Tag("positive")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("nosnikitos")
    @Link(name = "TASK-003", url = "https://...")
    void registrationTest() {
        String password = faker.harryPotter().character() + faker.number().numberBetween(0,100);
        String email = faker.internet().emailAddress();

        page(DwsRegisterPage.class)
                .openRegistration()
                .checkRegisterOpened()
                .selectRandomGender()
                .inputFirstName(faker.name().firstName())
                .inputLastName(faker.name().lastName())
                .inputEmail(email)
                .inputPassword(password)
                .inputConfirmPassword(password)
                .clickRegisterButton()
                .checkRegisterResultText("Your registration completed");
                page(DwsBasePage.class).checkVisibleUserEmail(email);
    }
}
