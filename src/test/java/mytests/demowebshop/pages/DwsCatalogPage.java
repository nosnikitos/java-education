package mytests.demowebshop.pages;

import com.codeborne.selenide.ElementsCollection;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$$;

public class DwsCatalogPage {
    private final ElementsCollection items = $$("div.product-grid div.item-box");

    @Step("Нажать на первый товар")
    public DwsItemPage openFirstItem() {
        items
                .get(0)
                .click();
        return new DwsItemPage();
    }
}
