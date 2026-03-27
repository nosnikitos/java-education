package ru.bulgakov.webshop.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Condition.visible;

public class DwsItemPage {

    private final SelenideElement itemName = $("[itemprop=name]");
    private final SelenideElement itemPrice = $("[itemprop=price]");
    private final SelenideElement addToCartButton = $("input.add-to-cart-button");
    private final SelenideElement quantityInput = $("input.qty-input");


    public String itemName() {
        return itemName.getText();
    }

    public String itemPrice() {
        return itemPrice.getText();
    }

    public DwsItemPage selectFirstOption() {
        $$("dl dd ul li").get(0).$$("li input").get(0).click();
        return this;
    }

    public DwsItemPage setQuantity(String quantity) {
        quantityInput.setValue(quantity);
        return this;
    }

    public DwsItemPage addItemToCart() {
        addToCartButton.click();
        return this;
    }
}
