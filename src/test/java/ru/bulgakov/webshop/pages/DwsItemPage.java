package ru.bulgakov.webshop.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DwsItemPage {

    private final SelenideElement itemName = $("[itemprop=name]");
    private final SelenideElement itemPrice = $("[itemprop=price]");
    private final SelenideElement addToCartButton = $("input.add-to-cart-button");
    private final SelenideElement quantityInput = $("input.qty-input");
    private final ElementsCollection optionList = $$("dl dd ul li");


    public String itemName() {
        return itemName.getText();
    }

    public String itemPrice() {
        return itemPrice.getText();
    }

    public DwsItemPage selectProcessor(int processorIndex) {
        if (processorIndex < 0 || processorIndex > 2) {
            throw new IllegalArgumentException("Processor index must be 0, 1 or 2");
        }

        String[] processorOptions = {"Slow", "Medium", "Fast"};

        optionList
                .findBy(text(processorOptions[processorIndex]))
                .$("input")
                .click();

        return this;
    }
    public float getProcessorPriceByIndex(int processorIndex) {
        if (processorIndex < 0 || processorIndex > 2) {
            throw new IllegalArgumentException("Processor index must be 0, 1 or 2");
        }

        String[] processorOptions = {"Slow", "Medium", "Fast"};

        // Находим элемент
        SelenideElement option = optionList
                .findBy(text(processorOptions[processorIndex]));

        String labelText = option.$("label").getText();

        // Если нет [+…], значит наценка = 0
        if (!labelText.contains("[+")) {
            return 0;
        }

        // Вытаскиваем число между [+ и ]
        String price = labelText.replaceAll(".*\\[\\+(\\d+(\\.\\d+)?)\\].*", "$1");

        return Float.parseFloat(price);
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
