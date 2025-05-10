
package OOProject;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class DinnerOrderGUI extends JFrame {
    private OrderService service;
    private RestaurantConfig config;
    private Map<MenuItem, Integer> cart;
    private JPanel menuPanel;
    private JLabel totalLabel;
    private double total;

    public DinnerOrderGUI(OrderService service, RestaurantConfig config) {
        this.service = service;
        this.config = config;
        this.cart = new HashMap<>();
        this.total = 0.0;

        setTitle("Place Order - " + config.getRestaurantName());
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Main layout
        setLayout(new BorderLayout(10, 10));

        // Menu panel
        menuPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        JScrollPane scrollPane = new JScrollPane(menuPanel);
        add(scrollPane, BorderLayout.CENTER);

        // Cart panel
        JPanel cartPanel = new JPanel(new BorderLayout());
        totalLabel = new JLabel("Total: $0.00");
        cartPanel.add(totalLabel, BorderLayout.NORTH);

        JButton checkoutBtn = new JButton("Proceed to Checkout");
        checkoutBtn.addActionListener(e -> processCheckout());
        cartPanel.add(checkoutBtn, BorderLayout.SOUTH);
        add(cartPanel, BorderLayout.SOUTH);

        loadMenu();
    }

    private void loadMenu() {
        for (MenuItem item : service.getMenuItems()) {
            JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            itemPanel.add(new JLabel(item.getName() + " - $" + String.format("%.2f", item.getPrice())));
            
            JButton addBtn = new JButton("+");
            JButton removeBtn = new JButton("-");
            JLabel quantityLabel = new JLabel("0");

            addBtn.addActionListener(e -> {
                int qty = Integer.parseInt(quantityLabel.getText());
                cart.put(item, qty + 1);
                quantityLabel.setText(String.valueOf(qty + 1));
                updateTotal();
            });

            removeBtn.addActionListener(e -> {
                int qty = Integer.parseInt(quantityLabel.getText());
                if (qty > 0) {
                    cart.put(item, qty - 1);
                    quantityLabel.setText(String.valueOf(qty - 1));
                    if (qty - 1 == 0) {
                        cart.remove(item);
                    }
                    updateTotal();
                }
            });

            itemPanel.add(removeBtn);
            itemPanel.add(quantityLabel);
            itemPanel.add(addBtn);
            menuPanel.add(itemPanel);
        }
    }

    private void updateTotal() {
        total = cart.entrySet().stream()
            .mapToDouble(entry -> entry.getKey().getPrice() * entry.getValue())
            .sum();
        totalLabel.setText(String.format("Total: $%.2f", total));
    }

    private void processCheckout() {
        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please add items to your cart first.");
            return;
        }

        JTextField nameField = new JTextField();
        JTextField phoneField = new JTextField();
        String[] paymentMethods = {"Cash", "Credit Card"};
        JComboBox<String> paymentCombo = new JComboBox<>(paymentMethods);

        Object[] message = {
            "Name:", nameField,
            "Phone:", phoneField,
            "Payment Method:", paymentCombo
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Checkout",
            JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            String paymentMethod = (String) paymentCombo.getSelectedItem();

            if (name.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.");
                return;
            }

            Order order = service.createOrder(config.getRestaurantId(), name, phone);
            for (Map.Entry<MenuItem, Integer> entry : cart.entrySet()) {
                order.addItem(entry.getKey(), entry.getValue());
            }
            order.setPaymentMethod(paymentMethod);
            service.saveOrders();

            JOptionPane.showMessageDialog(this,
                "Order placed successfully!\nOrder ID: " + order.getOrderId());
            dispose();
        }
    }
}
