package mytests.booking.steps;

import mytests.booking.dto.BookingDTO;
import net.datafaker.Faker;


public class BookingSteps {
    public static final Faker faker = new Faker();

    public static BookingDTO buildValidBookingRequestBody() {
        return BookingDTO.builder()
                .firstname(faker.name().firstName())
                .lastname(faker.name().lastName())
                .totalprice(faker.number().numberBetween(1000, 10000))
                .depositpaid(faker.bool().bool())
                .bookingdates(new BookingDTO.BookingDates("2026-05-22", "2026-05-26"))
                .additionalneeds(faker.football().competitions())
                .build();
    }


}
