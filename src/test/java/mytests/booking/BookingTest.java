package mytests.booking;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import mytests.booking.dto.AuthRequest;
import mytests.booking.dto.AuthResponse;
import mytests.booking.dto.CreateBookingDTO;
import mytests.booking.dto.CreateBookingResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BookingTest {
    public static final String BOOKING_URL = "https://restful-booker.herokuapp.com";

    @BeforeAll
    static void setUp () {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        RestAssured.filters(new AllureRestAssured());
    }

    @Test
    void authTest () {
        String user = "admin";
        String password = "password123";

        AuthResponse resp = given()
                .contentType(ContentType.JSON)
                .body(new AuthRequest(user, password))
                .post(BOOKING_URL + "/auth")
                .then()
                .statusCode(200)
                .body("token", notNullValue())
                .extract().as(AuthResponse.class);

        assertThat(resp.getToken()).isNotNull();
    }

    @Test
    void createBookingTest () {

        CreateBookingDTO req = buildBookingRequest();
        CreateBookingResponse resp = given()
                .contentType(ContentType.JSON)
                .body(req)
                .post(BOOKING_URL + "/booking")
                .then()
                .statusCode(200)
                .extract().as(CreateBookingResponse.class);

        assertThat(resp.getBookingid()).isNotNull();
        assertThat(resp.getBooking().getFirstname().equals(req.getFirstname()));
        assertThat(resp.getBooking().getLastname().equals(req.getLastname()));
        assertThat(resp.getBooking().getDepositpaid().equals(req.getDepositpaid()));
        assertThat(resp.getBooking().getTotalprice().equals(req.getTotalprice()));
        assertThat(resp.getBooking().getBookingdates().getCheckin()
                .equals(req.getBookingdates().getCheckin()));
        assertThat(resp.getBooking().getBookingdates().getCheckout()
                .equals(req.getBookingdates().getCheckout()));
        assertThat(resp.getBooking().getAdditionalneeds().equals(req.getAdditionalneeds()));
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
}
