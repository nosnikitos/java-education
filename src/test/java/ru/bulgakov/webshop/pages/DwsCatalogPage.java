package ru.bulgakov.webshop.pages;

import com.codeborne.selenide.ElementsCollection;

import static com.codeborne.selenide.Selenide.$$;

public class DwsCatalogPage {
    private final ElementsCollection items = $$("div.product-grid div.item-box");

    public DwsItemPage openFirstItem() {
        items
                .get(0)
                .click();
        return new DwsItemPage();
    }
}
