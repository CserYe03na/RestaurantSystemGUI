
package OOProject;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class ManagerOrderGUI extends AbstractManagerGUI {
    private OrderService service;

    public ManagerOrderGUI(OrderService service, RestaurantConfig config) {
        super(config, "Manager - View Orders",
                new String[]{"Order ID", "Customer", "Phone", "Total Amount", "Payment Method", "Status", "Paid"});
        this.service = service;
    }

    @Override
    protected void loadData() {
        tableModel.setRowCount(0);
        String currentRestaurantId = config.getRestaurantId();

        for (Order order : service.getAllOrders()) {
            if (order.getRestaurantId().equals(currentRestaurantId)) {
                tableModel.addRow(new Object[]{
                    order.getOrderId(),
                    order.getCustomerName(),
                    order.getPhoneNumber(),
                    String.format("%.2f", order.getTotalAmount()),
                    order.getPaymentMethod(),
                    order.getStatus(),
                    order.isPaid() ? "Yes" : "No"
                });
            }
        }
    }

    @Override
    protected void search() {
        String searchName = searchNameField.getText().trim();
        String searchPhone = searchPhoneField.getText().trim();
        
        tableModel.setRowCount(0);
        for (Order order : service.getAllOrders()) {
            if ((searchName.isEmpty() || order.getCustomerName().toLowerCase().contains(searchName.toLowerCase())) &&
                (searchPhone.isEmpty() || order.getPhoneNumber().contains(searchPhone))) {
                tableModel.addRow(new Object[]{
                    order.getOrderId(),
                    order.getCustomerName(),
                    order.getPhoneNumber(),
                    String.format("%.2f", order.getTotalAmount()),
                    order.getPaymentMethod(),
                    order.getStatus(),
                    order.isPaid() ? "Yes" : "No"
                });
            }
        }
    }

    @Override
    protected void manageSelectedItem() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an order to manage.");
            return;
        }

        String orderId = (String) tableModel.getValueAt(selectedRow, 0);
        Order order = service.getOrder(orderId);
        
        String[] options = {"Update Status", "Mark as Paid", "View Details"};
        int choice = JOptionPane.showOptionDialog(this, "Choose action:", "Manage Order",
            JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
            null, options, options[0]);

        switch (choice) {
            case 0: // Update Status
                Order.OrderStatus[] statuses = Order.OrderStatus.values();
                Order.OrderStatus selected = (Order.OrderStatus) JOptionPane.showInputDialog(
                    this, "Select new status:", "Update Status",
                    JOptionPane.QUESTION_MESSAGE, null,
                    statuses, order.getStatus());
                if (selected != null) {
                    order.setStatus(selected);
                    service.saveOrders();
                    loadData();
                }
                break;
            case 1: // Mark as Paid
                order.setPaid(true);
                service.saveOrders();
                loadData();
                break;
            case 2: // View Details
            	JOptionPane.showMessageDialog(this, order.toString());
                break;
//                StringBuilder details = new StringBuilder();
//                details.append("Order Details:\n\n");
//                for (Map.Entry<MenuItem, Integer> entry : order.getItems().entrySet()) {
//                    details.append(String.format("%s x%d - $%.2f\n",
//                        entry.getKey().getName(),
//                        entry.getValue(),
//                        entry.getKey().getPrice() * entry.getValue()));
//                }
//                details.append("\nTotal: $").append(String.format("%.2f", order.getTotalAmount()));
//                JOptionPane.showMessageDialog(this, details.toString());
//                break;
        }
    }
}
