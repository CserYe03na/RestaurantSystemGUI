package OOProject;

import java.time.LocalTime;

public class Reservation {
	private String restaurantId;
	private String customerName;
	private String phonenumber;
	private String date;
	private LocalTime timeSlot;
	private int tableId;
    private int partySize;
    private String notes;
    
    public Reservation(String restaurantId, String name, String phone, String date, LocalTime timeSlot, int tableId, int partySize, String notes) {
        this.restaurantId = restaurantId;
        this.customerName = name;
        this.phonenumber = phone;
        this.date = date;
        this.timeSlot = timeSlot;
        this.tableId = tableId;
        this.partySize = partySize;
        this.notes = notes;
    }
    
    public String getCustomerName() { return customerName; }
    public String getPhonenumber(){ return phonenumber;}
    public String getDate() { return date; }
    public LocalTime getTimeSlot() { return timeSlot; }
    public int getTableId() { return tableId; }
    public int getPartySize() { return partySize; }
    public String getRestaurantId() { return restaurantId;}
    public String getNotes() {return notes;}
    
//    public String toFileString() {
//        return customerName + ";" + phonenumber + ";" + date + ";" + timeSlot + ";" + tableId + ";" + partySize;
//    }

    public static Reservation fromFileString(String line) {
        String[] parts = line.split(";");
        return new Reservation(parts[0], parts[1], parts[2], parts[3], LocalTime.parse(parts[4]),
                Integer.parseInt(parts[5]), Integer.parseInt(parts[6]), parts[7]);
    }
    
    @Override
    public String toString() {
        return "Date: " + date +
               ", Time: " + timeSlot +
               ", Party: " + partySize +
               ", Table #: " + tableId +
               ", Special Request: " + notes;
    }
}
