package mytests.booking.tests;

import io.qameta.allure.Owner;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import mytests.base.BaseApiTest;
import mytests.booking.dto.BookingIds;
import mytests.booking.steps.BookingApiClient;
import mytests.booking.config.BookingConfig;
import mytests.booking.dto.AuthResponse;
import mytests.booking.dto.BookingDTO;
import mytests.booking.dto.BookingResponse;
import mytests.booking.steps.BookingSteps;
import net.datafaker.Faker;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import static io.restassured.RestAssured.given;
import static mytests.booking.config.BookingApiConfig.getBookingConfig;
import static mytests.booking.steps.BookingSteps.randomBooking;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


public class BookingTest extends BaseApiTest {

    private static final BookingConfig CFG = getBookingConfig();
    private static final String
            username = CFG.username(),
            password = CFG.password(),
            url = CFG.url();
    public static final Faker faker = new Faker();
    private final BookingApiClient bookingClient = new BookingApiClient();
    private final BookingSteps bookingSteps = new BookingSteps();


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
                .post(url + "/auth")
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
        BookingDTO requestBody = randomBooking();
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
                .post(url + "/booking")
                .then()
                .statusCode(500)
                .extract().response();

        assertThat(response.statusCode()).isEqualTo(500);
    }

    @Test
    @DisplayName("Полное обновление бронирования в API")
    @Tags({@Tag("API"), @Tag("positive")})
    @Owner("nosnikitos")
    void updateBookingTest() {
        Integer bookingId = bookingSteps.createBooking().getBookingid();

        BookingDTO updateRequestBody = randomBooking();
        Response updateResponse = bookingClient.updateBooking(bookingId, updateRequestBody);
        assertThat(updateResponse.statusCode()).isEqualTo(200);
        BookingSteps.verifyBookingEquals(updateResponse.as(BookingDTO.class), updateRequestBody);
    }

    @Test
    @DisplayName("Частичное обновление бронирования в API - только firstname")
    @Tags({@Tag("API"), @Tag("positive")})
    @Owner("nosnikitos")
    void partialUpdateBookingTest() {
        BookingDTO createRequestBody = randomBooking();
        Integer bookingId = bookingSteps.createBooking(createRequestBody).getBookingid();

        String newName = faker.name().firstName();
        BookingDTO updateRequestBody = BookingDTO
                .builder()
                .firstname(newName)
                .build();

        BookingDTO expectedPartialUpdateResponseBody = createRequestBody.toBuilder().firstname(newName).build();

        Response partialUpdateResponse = bookingClient.partialUpdateBooking(bookingId, updateRequestBody);
        assertThat(partialUpdateResponse.statusCode()).isEqualTo(200);
        BookingSteps.verifyBookingEquals(partialUpdateResponse.as(BookingDTO.class), expectedPartialUpdateResponseBody);
    }

    @Test
    @DisplayName("Получение бронирования по id в API")
    @Tags({@Tag("API"), @Tag("positive")})
    @Owner("nosnikitos")
    void getBookingTest() {
        BookingResponse booking = bookingSteps.createBooking();

        Response getResponse = bookingClient.getBooking(booking.getBookingid());
        assertThat(getResponse.statusCode()).isEqualTo(200);
        BookingSteps.verifyBookingEquals(getResponse.as(BookingDTO.class), booking.getBooking());
    }

    @Test
    @DisplayName("Удаление бронирования в API")
    @Tags({@Tag("API"), @Tag("positive")})
    @Owner("nosnikitos")
    void deleteBookingTest() {
        Integer bookingId = bookingSteps.createBooking().getBookingid();

        Response deleteResp = bookingClient.deleteBooking(bookingId);
        assertThat(deleteResp.statusCode()).isEqualTo(201);

        Response getBookingResp = bookingClient.getBooking(bookingId);
        assertThat(getBookingResp.statusCode()).isEqualTo(404);
    }

    @Test
    @DisplayName("Получение бронирования по firstname в API")
    @Tags({@Tag("API"), @Tag("positive")})
    @Owner("nosnikitos")
    void getBookingByFirstName() {
        int bookingQuantity = faker.number().numberBetween(2,5);
        String firstname = faker.name().firstName();

        List<Integer> createdBookingIds = bookingSteps.generateBookingIds(bookingQuantity, firstname);

        Response getBookingsResponse = bookingClient.getBookings(Map.of("firstname", firstname));
        assertThat(getBookingsResponse.statusCode()).isEqualTo(200);

        List<BookingIds> bookings = getBookingsResponse.as(new TypeRef<List<BookingIds>>() {});
        BookingSteps.verifyBookingList(bookings, createdBookingIds, bookingQuantity);
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
        BookingDTO valid = randomBooking();

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
