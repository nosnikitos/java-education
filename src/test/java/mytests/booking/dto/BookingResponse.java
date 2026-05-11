package mytests.booking.dto;

import lombok.Data;

@Data
public class BookingResponse {
    private Integer bookingid;
    private BookingDTO booking;
}
