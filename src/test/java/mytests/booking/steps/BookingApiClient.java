package mytests.booking.steps;

import io.qameta.allure.Step;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import mytests.booking.config.BookingConfig;
import mytests.booking.dto.AuthRequest;
import mytests.booking.dto.AuthResponse;
import mytests.booking.dto.BookingDTO;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static mytests.booking.config.BookingApiConfig.getBookingConfig;


public class BookingApiClient {


    private static final BookingConfig CFG = getBookingConfig();
    private final RequestSpecification spec = new RequestSpecBuilder()
            .setBaseUri(CFG.url())
            .setContentType(ContentType.JSON)
            .build();

    @Step("Выполнить запрос POST /auth")
    public Response auth(String user, String password) {
        return given(spec)
                .body(new AuthRequest(user, password))
                .post("/auth")
                .then()
                .extract().response();
    }

    @Step("Выполнить запрос POST /booking")
    public Response createBooking(BookingDTO booking) {
        return given(spec)
                .body(booking)
                .post("/booking")
                .then()
                .extract().response();
    }

    @Step("Выполнить запрос PUT /booking/{id}")
    public Response updateBooking(Integer id, BookingDTO booking) {
        return given(spec)
                .cookie("token", getToken())
                .body(booking)
                .pathParam("BOOKING_ID", id)
                .put("/booking/{BOOKING_ID}")
                .then()
                .extract().response();
    }

    @Step("Выполнить запрос PATCH /booking/{id}")
    public Response partialUpdateBooking(Integer id, BookingDTO booking) {
        return given(spec)
                .cookie("token", getToken())
                .body(booking)
                .pathParam("BOOKING_ID", id)
                .patch("/booking/{BOOKING_ID}")
                .then()
                .extract().response();
    }

    @Step("Выполнить запрос DELETE /booking/{id}")
    public Response deleteBooking(Integer id) {
        return given(spec)
                .cookie("token", getToken())
                .pathParam("BOOKING_ID", id)
                .delete("/booking/{BOOKING_ID}")
                .then()
                .extract().response();
    }

    @Step("Выполнить запрос GET /booking{id}")
    public Response getBooking(Integer id) {
        return given(spec)
                .pathParam("BOOKING_ID", id)
                .get("/booking/{BOOKING_ID}")
                .then()
                .extract().response();
    }

    @Step("Выполнить запрос GET /booking")
    public Response getBookings(Map<String, Object> queryParams) {
        return given(spec)
                .queryParams(queryParams)
                .log().params()
                .get("/booking")
                .then()
                .extract().response();
    }

    @Step("Получить токен авторизации")
    private String getToken() {
        return auth(CFG.username(), CFG.password()).as(AuthResponse.class).getToken();
    }
}
