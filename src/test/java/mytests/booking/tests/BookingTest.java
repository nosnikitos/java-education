package mytests.booking.tests;

import io.qameta.allure.Owner;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import mytests.booking.dto.AuthRequest;
import mytests.booking.dto.AuthResponse;
import mytests.booking.dto.CreateBookingDTO;
import mytests.booking.dto.CreateBookingResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BookingTest {
    public static final String BOOKING_URL = "https://restful-booker.herokuapp.com";
    public static final String AUTH_USER = "admin";
    public static final String AUTH_PASS = "password123";

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
        AuthResponse resp = given()
                .contentType(ContentType.JSON)
                .body(new AuthRequest(AUTH_USER, AUTH_PASS))
                .post(BOOKING_URL + "/auth")
                .then()
                .statusCode(200)
                .body("token", notNullValue())
                .extract().as(AuthResponse.class);

        assertThat(resp.getToken()).isNotNull();
    }

    @ParameterizedTest
    @DisplayName("Создание нового токена авторизации в API c невалидными данными")
    @Tags({@Tag("API"), @Tag("negative")})
    @Owner("nosnikitos")
    @MethodSource("invalidAuthData")
    void authWithInvalidDataTest(String description, Object requestBody) {
        AuthResponse resp = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post(BOOKING_URL + "/auth")
                .then()
                .statusCode(200)
                .body("reason", equalTo("Bad credentials"))
                .extract().as(AuthResponse.class);

        assertThat(resp.getReason()).isEqualTo("Bad credentials");
//        сорри тут проверка одна на всех, смысла нет в разных, апишка скудна на ошибки
    }

    @Test
    @DisplayName("Создание нового токена авторизации в API - без body")
    @Tags({@Tag("API"), @Tag("negative")})
    @Owner("nosnikitos")
    void authWithoutBodyTest() {

        AuthResponse resp = given()
                .contentType(ContentType.JSON)
                .post(BOOKING_URL + "/auth")
                .then()
                .statusCode(200)
                .body("reason", equalTo("Bad credentials"))
                .extract().as(AuthResponse.class);

        assertThat(resp.getReason()).isEqualTo("Bad credentials");
    }

    @Test
    @DisplayName("Создание нового бронирования в API")
    @Tags({@Tag("API"), @Tag("positive")})
    @Owner("nosnikitos")
    void createBookingTest () {
        CreateBookingDTO req = buildBookingRequest();
        CreateBookingResponse resp = given()
                .contentType(ContentType.JSON)
                .body(req)
                .post(BOOKING_URL + "/booking")
                .then()
                .statusCode(200)
                .extract().as(CreateBookingResponse.class);

        assertAll(
                () -> assertThat(resp.getBookingid()).isPositive(),
                () -> assertThat(resp.getBooking())
                        .usingRecursiveComparison()
                        .isEqualTo(req)
        );
    }

    @ParameterizedTest
    @DisplayName("Создание нового бронирования в API - c null полями")
    @Tags({@Tag("API"), @Tag("negative")})
    @Owner("nosnikitos")
    @MethodSource("invalidBookingDataWithoutField")
    void createBookingWithoutRequiredFieldTest (String description, Object requestBody) {

            Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post(BOOKING_URL + "/booking")
                .then()
                .statusCode(500)
                    .extract().response();

            assertThat(response.statusCode()).isEqualTo(500);

    }

    @Test
    @DisplayName("Создание нового бронирования в API - без body")
    @Tags({@Tag("API"), @Tag("negative")})
    @Owner("nosnikitos")
    void createBookingWithoutBodyTest () {

        Response response = given()
                .contentType(ContentType.JSON)
                .post(BOOKING_URL + "/booking")
                .then()
                .statusCode(500)
                .extract().response();

        assertThat(response.statusCode()).isEqualTo(500);
    }

    private static CreateBookingDTO buildBookingRequest() {
        return CreateBookingDTO.builder()
                .firstname("Nikita")
                .lastname("Nozdrin")
                .totalprice(1000)
                .depositpaid(true)
                .bookingdates(new CreateBookingDTO.BookingDates("2026-05-22", "2026-05-26"))
                .additionalneeds("Need whores")
                .build();
    }

    static Stream<Arguments> invalidAuthData() {
        return Stream.of(
                Arguments.of("Неверный пароль", (new AuthRequest(AUTH_USER, "xxx"))),
                Arguments.of("Неверный логин", (new AuthRequest("xxx", AUTH_PASS))),
                Arguments.of("Пустой пароль", (new AuthRequest(AUTH_USER, ""))),
                Arguments.of("Пустой логин", (new AuthRequest("", AUTH_PASS))),
                Arguments.of("null значения", (new AuthRequest())),
                Arguments.of("Пустое body", "{}")
        );
    }
    static Stream<Arguments> invalidBookingDataWithoutField() {
        return Stream.of(
                Arguments.of("Без поля firstname", CreateBookingDTO.builder()
                        .lastname("Nozdrin")
                        .totalprice(1000)
                        .depositpaid(true)
                        .bookingdates(new CreateBookingDTO.BookingDates("2026-05-22", "2026-05-26"))
                        .additionalneeds("Need water")
                        .build()
                ),
                Arguments.of("Без поля lastname", CreateBookingDTO.builder()
                        .firstname("Nikita")
                        .totalprice(1000)
                        .depositpaid(true)
                        .bookingdates(new CreateBookingDTO.BookingDates("2026-05-22", "2026-05-26"))
                        .additionalneeds("Need water")
                        .build()
                ),
                Arguments.of("Без поля depositpaid", CreateBookingDTO.builder()
                        .firstname("Nikita")
                        .lastname("Nozdrin")
                        .totalprice(1000)
                        .bookingdates(new CreateBookingDTO.BookingDates("2026-05-22", "2026-05-26"))
                        .additionalneeds("Need water")
                        .build()
                ),
                Arguments.of("Без поля totalprice", CreateBookingDTO.builder()
                        .firstname("Nikita")
                        .lastname("Nozdrin")
                        .depositpaid(true)
                        .bookingdates(new CreateBookingDTO.BookingDates("2026-05-22", "2026-05-26"))
                        .additionalneeds("Need water")
                        .build()
                ),
                Arguments.of("Без поля checkin", CreateBookingDTO.builder()
                        .firstname("Nikita")
                        .lastname("Nozdrin")
                        .depositpaid(true)
                        .totalprice(1000)
                        .bookingdates(new CreateBookingDTO.BookingDates(null, "2026-05-26"))
                        .additionalneeds("Need water")
                        .build()
                ),
                Arguments.of("Без поля checkout", CreateBookingDTO.builder()
                        .firstname("Nikita")
                        .lastname("Nozdrin")
                        .depositpaid(true)
                        .totalprice(1000)
                        .bookingdates(new CreateBookingDTO.BookingDates("2026-05-22", null))
                        .additionalneeds("Need water")
                        .build()
                )
        );
    }
}
