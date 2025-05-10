
package OOProject;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Order {
    private String orderId;
    private String restaurantId;
    private String customerName;
    private String phoneNumber;
    private Map<MenuItem, Integer> items;
    private double totalAmount;
    private String paymentMethod;
    private boolean isPaid;
    private LocalDateTime orderTime;
    private OrderStatus status;
    
    public enum OrderStatus{
        RECEIVED,
        IN_PREPARATION,
        DELIVERED
    }
    
    public Order(String orderId, String restaurantId, String customerName, String phoneNumber) {
        this.orderId = orderId;
        this.restaurantId = restaurantId;
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.items = new HashMap<>();
        this.totalAmount = 0.0;
        this.isPaid = false;
        this.orderTime = LocalDateTime.now();
        this.status = OrderStatus.RECEIVED;
    }
    public OrderStatus getStatus() { 
        return status; }
    public void setStatus(OrderStatus status) { this.status = status; }
    
    public void addItem(MenuItem item, int quantity) {
        items.put(item, items.getOrDefault(item, 0) + quantity);
        calculateTotal();
    }
    
    public void removeItem(MenuItem item) {
        items.remove(item);
        calculateTotal();
    }
    
    private void calculateTotal() {
        totalAmount = items.entrySet().stream()
            .mapToDouble(entry -> entry.getKey().getPrice() * entry.getValue())
            .sum();
    }
    
    public void setPaymentMethod(String method) {
        this.paymentMethod = method;
    }
    
    public void setPaid(boolean paid) {
        this.isPaid = paid;
    }
    
    // Getters
    public String getOrderId() { return orderId; }
    public String getRestaurantId() { return restaurantId; }
    public Map<MenuItem, Integer> getItems() { return items; }
    
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double amount) { this.totalAmount = amount; }

    public boolean isPaid() { return isPaid; }
    public String getPaymentMethod() { return paymentMethod; }
    public LocalDateTime getOrderTime() { return orderTime; }
    public String getCustomerName() { return customerName; }
    public String getPhoneNumber() { return phoneNumber; }
}
