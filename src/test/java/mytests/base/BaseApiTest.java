package mytests.base;

import org.junit.jupiter.api.BeforeAll;

import static mytests.utils.RestAssuredSpec.setupRestAssured;

public class BaseApiTest {
    @BeforeAll
    static void setUp () {
        setupRestAssured();
    }
}
