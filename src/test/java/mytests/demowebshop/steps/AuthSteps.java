package mytests.demowebshop.steps;

import net.datafaker.Faker;
import mytests.demowebshop.pages.DwsRegisterPage;
import static com.codeborne.selenide.Selenide.*;
import static mytests.config.Config.WEB_SHOP_REGISTER_URL;

public class AuthSteps {
    private static final Faker faker = new Faker();

    public void registerNewUser() {
        page(DwsRegisterPage.class)
                .register(
                        faker.name().firstName(),
                        faker.name().lastName(),
                        faker.internet().emailAddress(),
                        faker.harryPotter().character() + faker.number().numberBetween(0, 100));
}
}
