package orgpustohin;

import lombok.*;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {
    private String origin;
    private String origin_name;
    private String destination;
    private String destination_name;
    private String departure_date;
    private String departure_time;
    private String arrival_date;
    private String arrival_time;
    private String carrier;
    private int stops;
    private int price;

    public static int compare(Ticket p1, Ticket p2) throws ParseException {
        if(p1.getDuration() > p2.getDuration())
            return 1;
        return -1;
    }

    public long getDuration () throws ParseException {
        Date departure;
        Date arrival;
        long flightDuration;

        java.text.SimpleDateFormat sdf =
                new java.text.SimpleDateFormat("dd.MM.yy HH:mm");
        departure = sdf.parse(this.getDeparture_date() + " " + this.getDeparture_time());
        arrival = sdf.parse(this.getArrival_date() + " " + this.getArrival_time());
        flightDuration = arrival.getTime() - departure.getTime();

        return  flightDuration;
    }
}
