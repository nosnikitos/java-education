package mytests.booking.steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import mytests.booking.dto.BookingDTO;
import mytests.booking.dto.BookingIds;
import mytests.booking.dto.BookingResponse;
import net.datafaker.Faker;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class BookingSteps {
    public static final Faker faker = new Faker();
    private static final BookingApiClient bookingApiClient = new BookingApiClient();

    public BookingResponse createBooking() {
        return createBooking(randomBooking());
    }

    public BookingResponse createBooking(BookingDTO booking) {
        Response response = bookingApiClient.createBooking(booking);
        assertThat(response.statusCode()).isEqualTo(200);

        return response.as(BookingResponse.class);
    }

    @Step("Сгенерировать {bookingQuantity} бронирований с именем {firstname}")
    public List<Integer> generateBookingIds(int bookingQuantity, String firstname) {
        List<Integer> createdBookingIds = new ArrayList<>();

        for (int i = 0; i < bookingQuantity; i++) {
            BookingDTO bookingDTO = randomBooking();
            bookingDTO.setFirstname(firstname);

            Integer bookingId = createBooking(bookingDTO).getBookingid();
            createdBookingIds.add(bookingId);
        }
        return createdBookingIds;
    }

    public static BookingDTO randomBooking() {
        return BookingDTO.builder()
                .firstname(faker.name().firstName())
                .lastname(faker.name().lastName())
                .totalprice(faker.number().numberBetween(1000, 10000))
                .depositpaid(faker.bool().bool())
                .bookingdates(new BookingDTO.BookingDates("2026-05-22", "2026-05-26"))
                .additionalneeds(faker.football().competitions())
                .build();
    }


    @Step("Проверить соответствие полей в ответе")
    public static void verifyBookingEquals(BookingDTO actual, BookingDTO expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Step("Проверить список бронирований")
    public static void verifyBookingList(List<BookingIds> bookings, List<Integer> expectedBookingIds, Integer bookingQuantity) {
        assertThat(bookings)
                .as("Количество бронирований")
                .hasSize(bookingQuantity)
                .as("Список бронирований не должен содержать дубликаты")
                .doesNotHaveDuplicates()
                .as("Список бронирований не должен содержать null")
                .doesNotContainNull()
                .extracting(BookingIds::bookingid)
                .as("ID бронирований должны совпадать с ожидаемыми")
                .containsExactlyElementsOf(expectedBookingIds);
    }



}
