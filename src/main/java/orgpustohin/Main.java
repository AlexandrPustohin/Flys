package orgpustohin;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Main {
    private static Map<String, List<Ticket>> ticketsMapList;
    private static Map<Ticket, Long> minFlyDuration;
    private static double difference;


    public static void main(String[] args) throws IOException, ParseException {

        if (args.length>1) {
            String inFile = args[0];
            String outFile = args[1];
            ticketsMapList = getTicketList(inFile);



            List<Ticket> ticketsList = sortedTicketList(ticketsMapList, "VVO", "TLV");
            minFlyDuration = getListMinFlyDuration(ticketsList);
            //перевозчики с минимальной длительностью перелета
            System.out.println("перевозчики с минимальной длительностью перелета:");
            minFlyDuration.entrySet().stream()
                    .map(c -> c.getKey().getCarrier() + " "
                            + convertMillisToTime(c.getValue()))
                    .forEach(System.out::println);
            List<Ticket> sortTicketsList = ticketsList.stream().sorted((t1, t2)-> {
                try {
                    return Ticket.comparePrice(t1, t2);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.toList());
            difference = (averagePrice(sortTicketsList) - medianPrice(sortTicketsList));
            System.out.println("The difference between average price and median is " + difference);
            writeToFile(outFile);
        } else{
            throw new RuntimeException("Not enough parameters!");
        }
    }

    private static double medianPrice(List<Ticket> ticketsList) {
        double medianPrice;
        if (ticketsList.size() % 2 == 0)
            medianPrice = ((double)ticketsList.get(ticketsList.size()/2).getPrice()
                    + (double) ticketsList.get(ticketsList.size()/2 - 1).getPrice())/2;
        else
            medianPrice = (double) ticketsList.get(ticketsList.size()/2).getPrice();
        return medianPrice;
    }

    private static double averagePrice(List<Ticket> ticketsList) {
        double averagePrice;
        averagePrice = ticketsList.stream().mapToDouble(t->t.getPrice()).average().getAsDouble();
        return averagePrice;
    }

    private static Map<Ticket, Long> getListMinFlyDuration(List<Ticket> ticketsList) throws ParseException {
        Map<Ticket, Long> minFlyDuration = new HashMap<>();
        Set<String> carriers=ticketsList.stream().map(t-> t.getCarrier()).collect(Collectors.toSet());
        List<Ticket> temp;
        for (String carrier: carriers) {
            temp = ticketsList.stream().filter(t -> t.getCarrier().equals(carrier)).collect(Collectors.toList());
            Optional<Ticket> minDuration = temp.stream().min((p1, p2) -> {
                try {
                    return Ticket.compare(p1, p2);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            });
            if(minDuration.isPresent()){
                minFlyDuration.put(minDuration.get(), minDuration.get().getDuration());
            }
        }
        return  minFlyDuration;
    }

    private static String convertMillisToTime(long averageFlightTime) {
        long HH = TimeUnit.MILLISECONDS.toHours(averageFlightTime);
        long MM = TimeUnit.MILLISECONDS.toMinutes(averageFlightTime) % 60;
        long SS = TimeUnit.MILLISECONDS.toSeconds(averageFlightTime) % 60;

        return String.format("%02d:%02d:%02d", HH, MM, SS);
    }

    private static Map<String, List<Ticket>> getTicketList(String fileName) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File(fileName);
        Map<String, List<Ticket>> ticketList = objectMapper.readValue(file, new TypeReference<Map<String, List<Ticket>>>() {});
        return ticketList;
    }

    private static List<Ticket> sortedTicketList(Map<String, List<Ticket>> ticketsList, String origin, String destination) {
        List<Ticket> tickets = new ArrayList<>();
        for (Map.Entry<String, List<Ticket>> entry : ticketsList.entrySet()) {
            if (entry.getKey().equals("tickets")) {
                tickets = entry.getValue();
            }
        }
        return tickets.stream()
                .filter(t -> t.getOrigin().toUpperCase().equals(origin.toUpperCase()) && t.getDestination().toUpperCase().equals(destination.toUpperCase()))
                .collect(Collectors.toList());

    }

    private static void writeToFile(String fileName) {
        try(FileWriter writer = new FileWriter(fileName, false)) {
            minFlyDuration.entrySet().stream().forEach(f-> {
                try {
                    writer.write(f.getKey().getCarrier()+" "+convertMillisToTime(f.getValue()) +"\n");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            writer.write("The difference between average price and median is: " + difference + "\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}