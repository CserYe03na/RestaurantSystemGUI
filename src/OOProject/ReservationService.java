package OOProject;

import java.io.*;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class ReservationService{
    private String filePath = "C:/Users/SerenaC/eclipse-workspace/CourseHW/src/OOProject/reservation.csv";
    private Map<String, Reservation> reservations = new HashMap<>();
    private int currentId = 0;
    private RestaurantConfig config;

    public ReservationService(RestaurantConfig config) {
        this.config = config;
        loadFromFile();
    }

    public void loadFromFile() {
        reservations.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length >= 7) {
                    String restaurantId = tokens[0];
                    String name = tokens[1];
                    String phone = tokens[2];
                    String date = tokens[3];
                    LocalTime timeSlot = LocalTime.parse(tokens[4]);
                    int tableId = Integer.parseInt(tokens[5]);
                    int partySize = Integer.parseInt(tokens[6]);
                    String notes = (tokens.length >= 8) ? tokens[7] : "";

                    Reservation res = new Reservation(restaurantId, name, phone, date, timeSlot, tableId, partySize, notes);
                    String key = date + "-" + timeSlot + "-" + tableId;
                    reservations.put(key, res);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            for (Reservation res : reservations.values()) {
                writer.println(String.join(",",
                    res.getRestaurantId(),
                    res.getCustomerName(),
                    res.getPhonenumber(),
                    res.getDate(),
                    res.getTimeSlot().toString(),
                    String.valueOf(res.getTableId()),
                    String.valueOf(res.getPartySize()),
                    res.getNotes().replace(",", ";")
                ));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean addReservation(Reservation reservation) {
        String key = reservation.getDate() + "-" + reservation.getTimeSlot() + "-" + reservation.getTableId();
        reservations.put(key, reservation);
        saveToFile();
        return true;
    }

    public List<Reservation> getAllReservations() {
        return new ArrayList<>(reservations.values());
    }

   
    public List<Table> findAvailableTables(String date, LocalTime requestedTime, int partySize, RestaurantConfig config) {
        return config.getTables().stream()
            .filter(t -> t.getCapacity() >= partySize)
            .filter(t -> this.getAllReservations().stream()
                .filter(r -> r.getDate().equals(date) && r.getTableId() == t.getTableId())
                .noneMatch(r -> Math.abs(r.getTimeSlot().until(requestedTime, java.time.temporal.ChronoUnit.MINUTES)) < 120))
            .sorted(Comparator.comparingInt(Table::getCapacity))
            .collect(Collectors.toList());
    }
    
    public List<Reservation> findByNameAndPhone(String name, String phone) {
        return reservations.values().stream()
            .filter(r -> r.getCustomerName().equalsIgnoreCase(name) && r.getPhonenumber().equals(phone))
            .collect(Collectors.toList());
    }
    
    public void removeReservation(Reservation r) {
        String key = r.getDate() + "-" + r.getTimeSlot() + "-" + r.getTableId();
        reservations.remove(key);
        saveToFile();
    }
}