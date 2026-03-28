package ru.bulgakov.webshop.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.bulgakov.webshop.pages.DwsBasePage;
import ru.bulgakov.webshop.pages.DwsCartPage;
import ru.bulgakov.webshop.pages.DwsItemPage;
import ru.bulgakov.webshop.steps.AuthSteps;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.bulgakov.webshop.config.Config.WEB_SHOP_URL;

public class CartTest {
    private final AuthSteps authSteps = new AuthSteps();

    @BeforeEach
    void beforeEach() {
        authSteps.registerNewUser();
    }

    @Test
    void addItemToCartTest() {
        open(WEB_SHOP_URL, DwsBasePage.class)
                .hoverTopMenu("Computers")
                .clickTopSubMenu("Desktops")
                .openFirstItem();

        DwsItemPage itemPage = page(DwsItemPage.class);

        // Получаем имя и цену выбранного товара для последующих проверок
        String itemName = itemPage.itemName();
        String itemQuantity = "2";
        int processorIndex = 1;

        itemPage
                .selectProcessor(processorIndex)
                .setQuantity(itemQuantity)
                .addItemToCart();

        float processorPrice = itemPage.getProcessorPriceByIndex(processorIndex);
        float basePrice = Float.parseFloat(itemPage.itemPrice());
        float expectedTotal = (basePrice + processorPrice) * Float.parseFloat(itemQuantity);

        DwsBasePage basePage = page(DwsBasePage.class);

        // Проверяем, что появляется уведомление об успешном добавлении товара в корзину
        basePage.getSuccessNotification()
                .shouldBe(visible)
                .shouldHave(text("The product has been added"));

        // Проверяем, что в шапке сайта отображается правильное количество добавленных товаров
        basePage.getCartQuantity()
                .shouldHave(text("(" + itemQuantity + ")"));

        // Открываем страницу корзины для проверки содержимого
        DwsCartPage cartPage = basePage.openCart();

        // Проверяем, что имя товара в корзине совпадает с выбранным ранее
        cartPage.getProductName().shouldHave(text(itemName));

        // Проверяем, что количество товара в корзине соответствует заданному количеству
        assertEquals(itemQuantity, cartPage.getQuantityInput().getAttribute("value"));

        // Проверяем, что итоговая стоимость в корзине равна цене товара, умноженной на количество
        cartPage.getSubtotal().shouldHave(text(String.valueOf(expectedTotal)));
    }
}
