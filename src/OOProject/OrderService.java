
package OOProject;

import java.io.*;
import java.util.*;

public class OrderService {
    private Map<String, Order> orders;
    private String filePath = "/Users/k.z/Downloads/RestaurantSystemGUI/src/OOProject/orders.csv";
    private int currentId = 0;

    public OrderService() {
        orders = new HashMap<>();
        loadOrders();
    }

    public Order createOrder(String restaurantId, String customerName, String phoneNumber) {
        String orderId = String.format("ORD%04d", ++currentId);
        Order order = new Order(orderId, restaurantId, customerName, phoneNumber);
        orders.put(orderId, order);
        return order;
    }

    public Order getOrder(String orderId) {
        return orders.get(orderId);
    }

    public List<Order> getAllOrders() {
        return new ArrayList<>(orders.values());
    }

    public List<MenuItem> getMenuItems() {
        List<MenuItem> items = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("/Users/k.z/Downloads/RestaurantSystemGUI/src/OOProject/menu.csv"))) {
            String header = reader.readLine(); // Skip header
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                items.add(new MenuItem(parts[0], parts[1], Double.parseDouble(parts[2]), parts[3], parts[4]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return items;
    }

    public void loadOrders() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine(); // Skip header
            
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 8) continue; // Skip invalid lines
                Order order = new Order(parts[0], parts[1], parts[2], parts[3]);
                order.setPaid(Boolean.parseBoolean(parts[4]));
                order.setPaymentMethod(parts[5]);
                if ("OrderStatus".equals(parts[6])) {
                    // skip header row
                    continue;
                }
                order.setStatus(Order.OrderStatus.valueOf(parts[6]));
                // Set total amount directly using the setter
                order.setTotalAmount(Double.parseDouble(parts[7]));

                orders.put(order.getOrderId(), order);
                int id = Integer.parseInt(parts[0].substring(3));
                if (id > currentId) currentId = id;
            }
        } catch (IOException e) {
            System.err.println("Error loading orders: " + e.getMessage());
        }
    }

    public void saveOrders() {
        try (PrintWriter writer = new PrintWriter(filePath)) {
            writer.println("orderId,restaurantId,customerName,phoneNumber,isPaid,paymentMethod,status,totalAmount");

            for (Order order : orders.values()) {
                writer.println(String.format("%s,%s,%s,%s,%b,%s,%s,%.2f",
                    order.getOrderId(),
                    order.getRestaurantId(),
                    order.getCustomerName(),
                    order.getPhoneNumber(),
                    order.isPaid(),
                    order.getPaymentMethod(),
                    order.getStatus(),
                    order.getTotalAmount()
                    ));
            }
        } catch (IOException e) {
            System.err.println("Error saving orders: " + e.getMessage());
        }
    }
}
