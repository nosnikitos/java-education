package ru.bulgakov.mentor.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import java.util.List;
import java.util.stream.Collectors;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class WikipediaPage {

    private final SelenideElement titleText = $(".mw-page-title-main"),
                              body = $("body");
    private final ElementsCollection toc = $$(".toc li"),
                            tocText = $$("#toc .toctext");

    public String getTitleText() {
        return titleText.getText();
    }

    public String getPageText() {
        return body.getText();
    }
    }
