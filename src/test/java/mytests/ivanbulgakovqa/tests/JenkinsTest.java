package mytests.ivanbulgakovqa.tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

public class JenkinsTest {

    @Test
    @Tags({@Tag("UI"), @Tag("positive")})
    @DisplayName("UI positive")
    void jenkinsTest1() {
        System.out.println("UI positive test");
    }

    @Test
    @Tags({@Tag("UI"), @Tag("negative")})
    @DisplayName("UI negative")
    void jenkinsTest2() {
        System.out.println("UI negative test");
    }

    @Test
    @Tags({@Tag("API"), @Tag("positive")})
    @DisplayName("API positive")
    void jenkinsTest3() {
        System.out.println("API positive test");
    }

    @Test
    @Tags({@Tag("API"), @Tag("negative")})
    @DisplayName("API negative")
    void jenkinsTest4() {
        System.out.println("API negative test");
    }
}
