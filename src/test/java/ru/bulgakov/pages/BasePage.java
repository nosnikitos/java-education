package ru.bulgakov.pages;

import com.codeborne.selenide.WebDriverRunner;

import java.util.List;

import static com.codeborne.selenide.Selenide.switchTo;

public class BasePage
{

    protected void switchToLastWindow() {
        List<String> windows = WebDriverRunner.getWebDriver()
                .getWindowHandles()
                .stream()
                .toList();

        switchTo().window(windows.get(windows.size() - 1));
    }
}
