package mytests.booking.dto;

import lombok.Data;

@Data
public class CreateBookingResponse {
    private Integer bookingid;
    private CreateBookingDTO booking;
}
