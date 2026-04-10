package mytests.demowebshop.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class DwsCartPage {

    public SelenideElement getProductName() {
        return $("td.product a.product-name");
    }

    public SelenideElement getQuantityInput() {
        return $("input.qty-input");
    }

    public SelenideElement getSubtotal() {
        return $("span.product-subtotal");
    }
}
