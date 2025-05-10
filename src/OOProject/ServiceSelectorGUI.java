package OOProject;

import javax.swing.*;
import java.awt.*;

public class ServiceSelectorGUI extends JFrame {
	private String filePath = "/Users/k.z/Downloads/RestaurantSystemGUI/src/OOProject/restaurant.csv";
	public ServiceSelectorGUI() {
		setTitle("Restaurant System");
        setSize(500, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        //Role Selection ComboBox
        JPanel topPanel = new JPanel();
        JLabel roleLabel = new JLabel("Select Your Role: ");
        String[] roles = {"Manager", "Dinner"};
        JComboBox<String> roleComboBox = new JComboBox<>(roles);
        topPanel.add(roleLabel);
        topPanel.add(roleComboBox);

        //Restaurant Selection Area
        JLabel restaurantLabel = new JLabel("Restaurant ID or Name:");
        JTextField restaurantField = new JTextField(15);
        topPanel.add(restaurantLabel);
        topPanel.add(restaurantField);
        add(topPanel, BorderLayout.NORTH);
        
        //Service Buttons
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        JButton reserveBtn = new JButton("Reservation");
        JButton orderBtn = new JButton("Order");
        JButton membershipBtn = new JButton("Membership");

        buttonPanel.add(reserveBtn);
        buttonPanel.add(orderBtn);
        buttonPanel.add(membershipBtn);
        add(buttonPanel, BorderLayout.CENTER);

        
        //ActionListeners for reservation button
        reserveBtn.addActionListener(e -> {
        	String role = (String) roleComboBox.getSelectedItem();
        	String restaurant = restaurantField.getText().trim();
            if (restaurant.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter restaurant ID or name.");
                return;
            }

            try {
            	RestaurantConfig config = RestaurantConfig.loadFromCSV(filePath, restaurant);
                ReservationService service = new ReservationService(config);
                
                if (role == "Manager") {
            		javax.swing.SwingUtilities.invokeLater(() -> {
            			new ManagerReservationGUI(service, config).setVisible(true);
            		});
            	}
            	else if (role == "Dinner") {
            		javax.swing.SwingUtilities.invokeLater(() -> {
            			new DinnerReservationGUI(service, config).setVisible(true);
            		});
            	}
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Restaurant not found: " + ex.getMessage());
            }
        	
        });
        
//ActionListeners for order button
        orderBtn.addActionListener(e -> {
            String role = (String) roleComboBox.getSelectedItem();
            String restaurant = restaurantField.getText().trim();
            if (restaurant.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter restaurant ID or name.");
                return;
            }

            try {
                RestaurantConfig config = RestaurantConfig.loadFromCSV(filePath, restaurant);
                OrderService service = new OrderService();
                
                if (role.equals("Manager")) {
                    javax.swing.SwingUtilities.invokeLater(() -> {
                        new ManagerOrderGUI(service, config).setVisible(true);
                    });
                } else if (role.equals("Dinner")) {
                    javax.swing.SwingUtilities.invokeLater(() -> {
                        new DinnerOrderGUI(service, config).setVisible(true);
                    });
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Restaurant not found: " + ex.getMessage());
            }
        });        

        //ActionListeners for membership button
        membershipBtn.addActionListener(e -> {
        	String role = (String) roleComboBox.getSelectedItem();
        	String restaurant = restaurantField.getText().trim();
            if (restaurant.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter restaurant ID or name.");
                return;
            }

            try {
            	RestaurantConfig config = RestaurantConfig.loadFromCSV(filePath, restaurant);
                MembershipService service = new MembershipService();
                
                if (role.equals("Manager")) {
            		javax.swing.SwingUtilities.invokeLater(() -> {
            			new ManagerMembershipGUI(service, config).setVisible(true);
            		});
            	}
            	else if (role.equals("Dinner") ) {
            		javax.swing.SwingUtilities.invokeLater(() -> {
            			new DinnerMembershipGUI(service, config.getRestaurantId(), config.getRestaurantName()).setVisible(true);
            		});
            	}
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Restaurant not found: " + ex.getMessage());
            }
        });
    }
}
