package mytests.demowebshop.tests;

import io.qameta.allure.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import mytests.base.BaseTest;
import mytests.demowebshop.pages.DwsBasePage;
import mytests.demowebshop.pages.DwsCartPage;
import mytests.demowebshop.pages.DwsItemPage;
import mytests.demowebshop.steps.AuthSteps;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Epic("E-commerce")
@Feature("Корзина")
public class CartTest extends BaseTest {
    private final AuthSteps authSteps = new AuthSteps();

    @BeforeEach
    void beforeEach() {
        step("Регистрация нового пользователя", authSteps::registerNewUser);
    }

    @Test
    @DisplayName("Корректное добавление товара в корзину")
    @Description("Проверяем добавление товара в корзину: уведомление, количество, содержимое, название и итоговая стоимость")
    @Tag("positive")
    @Severity(SeverityLevel.BLOCKER)
    @Owner("nosnikitos")
    @Link(name = "TASK-006", url = "https://...")
    void testAddItemToCartTest() {
        page(DwsBasePage.class)
                .openWebShop()
                .hoverTopMenu("Computers")
                .clickTopSubMenu("Desktops")
                .openFirstItem();

        DwsItemPage itemPage = page(DwsItemPage.class);

        String itemName = step("Получить имя выбранного товара", itemPage::itemName);
        String itemQuantity = "2";
        int processorIndex = 1;

        itemPage
                .selectProcessor(processorIndex)
                .setQuantity(itemQuantity)
                .addItemToCart();

        float processorPrice = step("Получить цену выбранного процессора",
                () -> itemPage.getProcessorPriceByIndex(processorIndex));
        float basePrice = Float.parseFloat(step("Получить базовую цену товара", itemPage::itemPrice));
        float expectedTotal = (basePrice + processorPrice) * Float.parseFloat(itemQuantity);

        DwsBasePage basePage = page(DwsBasePage.class);

        step("Проверить уведомление об успешном добавлении товара", () -> {
            basePage.getSuccessNotification()
                    .shouldBe(visible)
                    .shouldHave(text("The product has been added"));
        });

        step("Проверить количество товаров в шапке сайта", () -> {
            basePage.getCartQuantity()
                    .shouldHave(text("(" + itemQuantity + ")"));
        });

        DwsCartPage cartPage = basePage.clickCart();

        step("Проверить содержимое корзины", () -> {
            cartPage.getProductName().shouldHave(text(itemName)); // Проверяем, что имя товара в корзине совпадает с выбранным ранее
            cartPage.getQuantityInput().shouldHave(value(itemQuantity)); // Проверяем, что количество товара в корзине соответствует заданному количеству
            cartPage.getSubtotal().shouldHave(text(String.valueOf(expectedTotal))); // Проверяем, что итоговая стоимость в корзине равна цене товара, умноженной на количество
        });
    }
}
