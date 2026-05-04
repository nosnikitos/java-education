package mytests.booking;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import mytests.booking.config.BookingConfig;
import mytests.booking.dto.AuthRequest;
import mytests.booking.dto.AuthResponse;
import mytests.booking.dto.BookingDTO;

import static io.restassured.RestAssured.given;
import static mytests.booking.config.BookingApiConfig.getBookingConfig;


public class BookingApiClient {


    private static final BookingConfig CFG = getBookingConfig();
    public static final String BOOKER_AUTH_URL = CFG.url() + "/auth";
    public static final String BOOKER_BOOKING_URL = CFG.url() + "/booking";

    public Response auth(String user, String password) {
        return given()
                .contentType(ContentType.JSON)
                .body(new AuthRequest(user, password))
                .post(BOOKER_AUTH_URL)
                .then()
                .extract().response();
    }

    public Response createBooking(BookingDTO booking) {
        return given()
                .contentType(ContentType.JSON)
                .body(booking)
                .post(BOOKER_BOOKING_URL)
                .then()
                .extract().response();
    }

    public Response updateBooking(BookingDTO booking, Integer id) {
        return given()
                .cookie("token", getToken())
                .contentType(ContentType.JSON)
                .body(booking)
                .pathParam("BOOKING_ID", id)
                .put(BOOKER_BOOKING_URL + "/{BOOKING_ID}")
                .then()
                .extract().response();
    }

    private String getToken() {
        return auth(CFG.username(), CFG.password()).as(AuthResponse.class).getToken();
    }
}
