package mytests.booking.tests;

import io.qameta.allure.Owner;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import mytests.booking.BookingApiClient;
import mytests.booking.config.BookingConfig;
import mytests.booking.dto.AuthResponse;
import mytests.booking.dto.BookingDTO;
import mytests.booking.dto.BookingResponse;
import net.datafaker.Faker;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;
import static io.restassured.RestAssured.given;
import static mytests.booking.BookingApiClient.BOOKER_AUTH_URL;
import static mytests.booking.BookingApiClient.BOOKER_BOOKING_URL;
import static mytests.booking.config.BookingApiConfig.getBookingConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


public class BookingTest {

    private static final BookingConfig CFG = getBookingConfig();
    private static final String
            username = CFG.username(),
            password = CFG.password();
    public static final Faker faker = new Faker();
    private final BookingApiClient bookingClient = new BookingApiClient();

    @BeforeAll
    static void setUp () {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        RestAssured.filters(new AllureRestAssured());
    }

    @Test
    @DisplayName("Создание нового токена авторизации в API")
    @Tags({@Tag("API"), @Tag("positive")})
    @Owner("nosnikitos")
    void authTest () {
        Response authResp = bookingClient.auth(username, password);
        assertThat(authResp.statusCode()).isEqualTo(200);
        assertThat(authResp.as(AuthResponse.class).getToken()).isNotNull();
    }

    @ParameterizedTest
    @DisplayName("Создание нового токена авторизации в API - c невалидными данными")
    @Tags({@Tag("API"), @Tag("negative")})
    @Owner("nosnikitos")
    @MethodSource("invalidAuthData")
    void authWithInvalidDataTest(String description, String username, String password) {

        Response respAuth = bookingClient.auth(username, password);

        assertThat(respAuth.as(AuthResponse.class).getReason()).as(description).isEqualTo("Bad credentials");
    }

    @Test
    @DisplayName("Создание нового токена авторизации в API - без body")
    @Tags({@Tag("API"), @Tag("negative")})
    @Owner("nosnikitos")
    void authWithoutBodyTest() {

        AuthResponse resp = given()
                .contentType(ContentType.JSON)
                .post(BOOKER_AUTH_URL)
                .then()
                .statusCode(200)
                .extract().as(AuthResponse.class);

        assertThat(resp.getReason()).isEqualTo("Bad credentials");
    }

    @Test
    @DisplayName("Создание нового бронирования в API")
    @Tags({@Tag("API"), @Tag("positive")})
    @Owner("nosnikitos")
    void createBookingTest () {
        BookingDTO requestBody = buildValidBookingRequestBody();
        Response response = bookingClient.createBooking(requestBody);

        assertThat(response.statusCode()).isEqualTo(200);

        BookingResponse responseBody = response.as(BookingResponse.class);

        assertAll(
                () -> assertThat(responseBody.getBookingid()).isPositive(),
                () -> assertThat(responseBody.getBooking())
                        .usingRecursiveComparison()
                        .isEqualTo(requestBody)
        );
    }

    @ParameterizedTest
    @DisplayName("Создание нового бронирования в API - c null полями")
    @Tags({@Tag("API"), @Tag("negative")})
    @Owner("nosnikitos")
    @MethodSource("invalidBookingDataWithoutField")
    void createBookingWithoutRequiredFieldTest (String description, BookingDTO requestBody) {

            Response response = bookingClient.createBooking(requestBody);

            assertThat(response.statusCode()).as(description).isEqualTo(500);
    }

    @Test
    @DisplayName("Создание нового бронирования в API - без body")
    @Tags({@Tag("API"), @Tag("negative")})
    @Owner("nosnikitos")
    void createBookingWithoutBodyTest () {

        Response response = given()
                .contentType(ContentType.JSON)
                .post(BOOKER_BOOKING_URL)
                .then()
                .statusCode(500)
                .extract().response();

        assertThat(response.statusCode()).isEqualTo(500);
    }

    @Test
    @DisplayName("Апдейт бронирования в API")
    @Tags({@Tag("API"), @Tag("positive")})
    @Owner("nosnikitos")
    void updateBookingTest() {
        BookingDTO createRequestBody = buildValidBookingRequestBody();
        Response createResponse = bookingClient.createBooking(createRequestBody);

        assertThat(createResponse.statusCode()).isEqualTo(200);

        Integer bookingId = createResponse.as(BookingResponse.class).getBookingid();
        BookingDTO updateRequestBody = buildValidBookingRequestBody();
        Response updateResponse = bookingClient.updateBooking(updateRequestBody, bookingId);

        assertThat(updateResponse.statusCode()).isEqualTo(200);

        BookingDTO updateResponseBody = updateResponse.as(BookingDTO.class);

        assertThat(updateResponseBody).usingRecursiveComparison().isEqualTo(updateRequestBody);
    }

    private static BookingDTO buildValidBookingRequestBody() {
        return BookingDTO.builder()
                .firstname(faker.name().firstName())
                .lastname(faker.name().lastName())
                .totalprice(faker.number().numberBetween(1000, 10000))
                .depositpaid(faker.bool().bool())
                .bookingdates(new BookingDTO.BookingDates("2026-05-22", "2026-05-26"))
                .additionalneeds(faker.football().competitions())
                .build();
    }
    static Stream<Arguments> invalidAuthData() {
        return Stream.of(
                Arguments.of("Неверный пароль", username, "xxx"),
                Arguments.of("Неверный логин", "xxx", password),
                Arguments.of("Пустой пароль", username, ""),
                Arguments.of("Пустой логин", "", password),
                Arguments.of("null значения", null, null)
        );
    }
    static Stream<Arguments> invalidBookingDataWithoutField() {
        BookingDTO valid = buildValidBookingRequestBody();

        return Stream.of(
                Arguments.of("Без поля firstname", valid.toBuilder().firstname(null).build()),
                Arguments.of("Без поля lastname", valid.toBuilder().lastname(null).build()),
                Arguments.of("Без поля depositpaid", valid.toBuilder().depositpaid(null).build()),
                Arguments.of("Без поля totalprice", valid.toBuilder().totalprice(null).build()),
                Arguments.of("Без поля checkin", valid.toBuilder()
                        .bookingdates(new BookingDTO.BookingDates(null, "2026-05-26"))
                        .build()),
                Arguments.of("Без поля checkout", valid.toBuilder()
                        .bookingdates(new BookingDTO.BookingDates("2026-05-22", null))
                        .build())
        );
    }
}
