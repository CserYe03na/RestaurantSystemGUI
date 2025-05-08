package OOProject;

import java.io.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class RestaurantConfig {
	private final String restaurantId;
    private final String restaurantName;
    private final List<Table> tables;
    private final LocalTime openTime;
    private final LocalTime closeTime;

    public RestaurantConfig(String restaurantId, String restaurantName, List<Table> tables, LocalTime openTime, LocalTime closeTime) {
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.tables = tables;
        this.openTime = openTime;
        this.closeTime = closeTime;
    }

    public static RestaurantConfig loadFromCSV(String filePath, String targetRestaurant) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
        	String line;
            int tableId = 1;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String id = parts[0];
                String name = parts[1];
                if (targetRestaurant.equalsIgnoreCase(id) || targetRestaurant.equalsIgnoreCase(name)) {
                    int table2 = Integer.parseInt(parts[2]);
                    int table4 = Integer.parseInt(parts[3]);
                    int table6 = Integer.parseInt(parts[4]);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
                    java.time.LocalTime open = java.time.LocalTime.parse(parts[5],formatter);
                    java.time.LocalTime close = java.time.LocalTime.parse(parts[6],formatter);

                    java.util.List<Table> tables = new java.util.ArrayList<>();
                    for (int i = 0; i < table2; i++) tables.add(new Table(tableId++, 2));
                    for (int i = 0; i < table4; i++) tables.add(new Table(tableId++, 4));
                    for (int i = 0; i < table6; i++) tables.add(new Table(tableId++, 6));

                    return new RestaurantConfig(id, name, tables, open, close);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Restaurant not found: " + targetRestaurant);
    }

    public List<Table> getTables() { return tables; }
    public boolean isWithinBusinessHours(LocalTime time) {
        return !time.isBefore(openTime) && !time.isAfter(closeTime);
    }
    public String getRestaurantName() { return restaurantName; }
    public String getRestaurantId() { return restaurantId; }
}