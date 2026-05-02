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
    public static final String BOOKER_AUTH_URL = "https://restful-booker.herokuapp.com/auth";
    public static final String BOOKER_BOOKING_URL = "https://restful-booker.herokuapp.com/booking";
    public static final String AUTH_USER = "admin";
    public static final String AUTH_PASS = "password123";
//    ну без конфига пока тут будут

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
                .post(BOOKER_AUTH_URL)
                .then()
                .statusCode(200)
                .extract().as(AuthResponse.class);

        assertThat(resp.getToken()).isNotNull();
    }

    @ParameterizedTest
    @DisplayName("Создание нового токена авторизации в API - c невалидными данными")
    @Tags({@Tag("API"), @Tag("negative")})
    @Owner("nosnikitos")
    @MethodSource("invalidAuthData")
    void authWithInvalidDataTest(String description, Object requestBody) {
        AuthResponse resp = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post(BOOKER_AUTH_URL)
                .then()
                .statusCode(200)
                .extract().as(AuthResponse.class);

        assertThat(resp.getReason()).isEqualTo("Bad credentials");
//        сорри тут проверка одна на всех, смысла нет в разных, апишка скудна на ошибки
//        можно было б на логин/пароль неверные 401 проверять, на остальное 400
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
//        ну тут тоже 400 была б
    }

    @Test
    @DisplayName("Создание нового бронирования в API")
    @Tags({@Tag("API"), @Tag("positive")})
    @Owner("nosnikitos")
    void createBookingTest () {
        CreateBookingDTO req = buildValidBookingRequest();
        CreateBookingResponse resp = given()
                .contentType(ContentType.JSON)
                .body(req)
                .post(BOOKER_BOOKING_URL)
                .then()
                .statusCode(200)
                .extract().as(CreateBookingResponse.class);

        assertAll(
                () -> assertThat(resp.getBookingid()).isPositive(),
                () -> assertThat(resp.getBooking())
                        .usingRecursiveComparison()
                        .isEqualTo(req)
        );
//        ну тут че писать то, все понятно ассерт олл и usingRecursiveComparison ии подсказал, хотя без него тож работает
//        но без него типо если что-то одно не совпадет не видно будет что именно
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
                .post(BOOKER_BOOKING_URL)
                .then()
                .statusCode(500)
                    .extract().response();

            assertThat(response.statusCode()).isEqualTo(500);
//            ну что тут проверять, тоже все печально, на даты ему пофиг, на -1000 тоже, вот без полей сделал

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
//        и тут тоже все печально, я высасывал тесты как мог
    }

    private static CreateBookingDTO buildValidBookingRequest() {
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
        CreateBookingDTO valid = buildValidBookingRequest();

        return Stream.of(
                Arguments.of("Без поля firstname", valid.toBuilder().firstname(null).build()),
                Arguments.of("Без поля lastname", valid.toBuilder().lastname(null).build()),
                Arguments.of("Без поля depositpaid", valid.toBuilder().depositpaid(null).build()),
                Arguments.of("Без поля totalprice", valid.toBuilder().totalprice(null).build()),
                Arguments.of("Без поля checkin", valid.toBuilder()
                        .bookingdates(new CreateBookingDTO.BookingDates(null, "2026-05-26"))
                        .build()),
                Arguments.of("Без поля checkout", valid.toBuilder()
                        .bookingdates(new CreateBookingDTO.BookingDates("2026-05-22", null))
                        .build())
        );
        //        ну тут мне ии подсказал @Builder(toBuilder = true), я снимаю шляпу, у меня простыня была в коммите можешь чекнуть
    }
}
