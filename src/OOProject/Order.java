
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
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Order ID: ").append(orderId).append("\n");
        sb.append("Customer: ").append(customerName).append(" (").append(phoneNumber).append(")\n");
        sb.append("Restaurant ID: ").append(restaurantId).append("\n");
        sb.append("Order Time: ").append(orderTime).append("\n");
        sb.append("Status: ").append(status).append("\n");
        sb.append("Payment Method: ").append(paymentMethod != null ? paymentMethod : "N/A").append("\n");
        sb.append("Paid: ").append(isPaid ? "Yes" : "No").append("\n\n");

        sb.append("Items:\n");
        if (items.isEmpty()) {
            sb.append("No items in this order.\n");
        } else {
            for (Map.Entry<MenuItem, Integer> entry : items.entrySet()) {
                MenuItem item = entry.getKey();
                int quantity = entry.getValue();
                sb.append(String.format(" - %s x%d ($%.2f)\n", item.getName(), quantity, item.getPrice() * quantity));
            }
        }

        sb.append("\nTotal: $").append(String.format("%.2f", totalAmount));
        return sb.toString();
    }

}
