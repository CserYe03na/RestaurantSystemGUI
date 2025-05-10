
package OOProject;

import java.io.*;
import java.util.*;

public class OrderService {
    private Map<String, Order> orders;
    private String filePath = "C:/Users/SerenaC/eclipse-workspace/OOProject/src/OOProject/orders.csv";
    private String menuPath = "C:/Users/SerenaC/eclipse-workspace/OOProject/src/OOProject/menu.csv";
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
        try (BufferedReader reader = new BufferedReader(new FileReader(menuPath))) {
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
                String[] parts = line.split(",", -1);
                if (parts.length < 9) continue;

                Order order = new Order(parts[0], parts[1], parts[2], parts[3]);
                order.setPaid(Boolean.parseBoolean(parts[4]));
                order.setPaymentMethod(parts[5]);
                order.setStatus(Order.OrderStatus.valueOf(parts[6]));
                order.setTotalAmount(Double.parseDouble(parts[7]));

                String itemsStr = parts[8];
                for (String itemStr : itemsStr.split("\\|")) {
                    if (!itemStr.isEmpty()) {
                        String[] itemParts = itemStr.split("-");
                        String itemName = itemParts[0];
                        int qty = Integer.parseInt(itemParts[1]);

                        for (MenuItem item : getMenuItems()) {
                            if (item.getName().equals(itemName)) {
                                order.addItem(item, qty);
                                break;
                            }
                        }
                    }
                }

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
            writer.println("orderId,restaurantId,customerName,phoneNumber,isPaid,paymentMethod,status,totalAmount,items");

            for (Order order : orders.values()) {
                StringBuilder itemStr = new StringBuilder();
                for (Map.Entry<MenuItem, Integer> entry : order.getItems().entrySet()) {
                    itemStr.append(entry.getKey().getName()).append("-").append(entry.getValue()).append("|");
                }
                if (itemStr.length() > 0) itemStr.setLength(itemStr.length() - 1); // remove trailing |

                writer.println(String.format("%s,%s,%s,%s,%b,%s,%s,%.2f,%s",
                    order.getOrderId(),
                    order.getRestaurantId(),
                    order.getCustomerName(),
                    order.getPhoneNumber(),
                    order.isPaid(),
                    order.getPaymentMethod(),
                    order.getStatus(),
                    order.getTotalAmount(),
                    itemStr.toString()
                ));
            }
        } catch (IOException e) {
            System.err.println("Error saving orders: " + e.getMessage());
        }
    }
}
