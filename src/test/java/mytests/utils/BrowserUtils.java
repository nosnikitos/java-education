package mytests.utils;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Selenide.sleep;
import static com.codeborne.selenide.Selenide.switchTo;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

public class BrowserUtils {
    public static void switchToNextWindow() {
        List<String> handles = new ArrayList<>(getWebDriver().getWindowHandles());
        String current = getWebDriver().getWindowHandle();
        int next = (handles.indexOf(current) + 1) % handles.size();
        switchTo().window(handles.get(next));
    }
}
