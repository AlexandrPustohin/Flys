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
    public static int comparePrice(Ticket p1, Ticket p2) throws ParseException {
        if(p1.getPrice() > p2.getPrice())
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

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getOrigin_name() {
        return origin_name;
    }

    public void setOrigin_name(String origin_name) {
        this.origin_name = origin_name;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDestination_name() {
        return destination_name;
    }

    public void setDestination_name(String destination_name) {
        this.destination_name = destination_name;
    }

    public String getDeparture_date() {
        return departure_date;
    }

    public void setDeparture_date(String departure_date) {
        this.departure_date = departure_date;
    }

    public String getDeparture_time() {
        return departure_time;
    }

    public void setDeparture_time(String departure_time) {
        this.departure_time = departure_time;
    }

    public String getArrival_date() {
        return arrival_date;
    }

    public void setArrival_date(String arrival_date) {
        this.arrival_date = arrival_date;
    }

    public String getArrival_time() {
        return arrival_time;
    }

    public void setArrival_time(String arrival_time) {
        this.arrival_time = arrival_time;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public int getStops() {
        return stops;
    }

    public void setStops(int stops) {
        this.stops = stops;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
