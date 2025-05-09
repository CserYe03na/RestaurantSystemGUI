package OOProject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.text.SimpleDateFormat;
import java.util.Comparator;

public class DinnerReservationGUI extends JFrame {
	private JLabel restaurantInfoLabel;
    private JSpinner dateSpinner;
    private JComboBox<String> timeCombo;
    private JTextField partySizeField, nameField, phoneField, noteField;
    private JTextArea availabilityArea;
    private Table assignedTable;
    private ReservationService service;
    private RestaurantConfig config;

    public DinnerReservationGUI(ReservationService Service, RestaurantConfig Config) {
    	this.service = Service;
    	this.config = Config;
        setTitle("Dinner Reservation");
        setSize(600, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //JPanel topPanel = new JPanel(new FlowLayout());
        JLabel restaurantInfoLabel = new JLabel("Restaurant: " + config.getRestaurantId() + " - " + config.getRestaurantName());
        restaurantInfoLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        add(restaurantInfoLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;

        // Date Picker
        gbc.gridx = 0; gbc.gridy = 0;
        centerPanel.add(new JLabel("Date:"), gbc);
        dateSpinner = new JSpinner(new SpinnerDateModel());
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd"));
        gbc.gridx = 1;
        centerPanel.add(dateSpinner, gbc);

        // Time Dropdown
        gbc.gridx = 0; gbc.gridy = 2;
        centerPanel.add(new JLabel("Time:"), gbc);
        String[] times = java.util.stream.IntStream.range(0, 25)
                .mapToObj(i -> String.format("%02d:%02d", 11 + (i / 2), (i % 2 == 0 ? 0 : 30)))
                .toArray(String[]::new);
        timeCombo = new JComboBox<>(times);
        gbc.gridx = 1;
        centerPanel.add(timeCombo, gbc);

        // Party Size
        gbc.gridx = 0; gbc.gridy = 4;
        centerPanel.add(new JLabel("Party Size:"), gbc);
        partySizeField = new JTextField(5);
        gbc.gridx = 1;
        centerPanel.add(partySizeField, gbc);

        // Check Availability
        JButton checkBtn = new JButton("Check Availability");
        gbc.gridx = 3; gbc.gridy = 2;
        centerPanel.add(checkBtn, gbc);        

        // Name, Phone, and Notes
        gbc.gridy = 6; gbc.gridwidth = 1;
        gbc.gridx = 0;
        centerPanel.add(new JLabel("Name:"), gbc);
        nameField = new JTextField(10);
        gbc.gridx = 1; 
        centerPanel.add(nameField, gbc);

        gbc.gridy = 7; gbc.gridx = 0;
        centerPanel.add(new JLabel("Phone:"), gbc);
        phoneField = new JTextField(10);
        gbc.gridx = 1;
        centerPanel.add(phoneField, gbc);
        
        gbc.gridy = 8; gbc.gridx = 0;
        centerPanel.add(new JLabel("Special Request:"), gbc);
        noteField = new JTextField(10);
        gbc.gridx = 1;
        centerPanel.add(noteField, gbc);

        // Reserve Button
        JButton reserveBtn = new JButton("Reserve");
        gbc.gridy = 6; gbc.gridx = 3;
        centerPanel.add(reserveBtn, gbc);
        
        // Search my reservation Button
        JButton searchBtn = new JButton("View My Reservation");
        gbc.gridy = 8; gbc.gridx = 3;
        centerPanel.add(searchBtn, gbc);
       
        add(centerPanel, BorderLayout.CENTER);


        checkBtn.addActionListener(e -> {
            try {
                String date = new SimpleDateFormat("yyyy-MM-dd").format(dateSpinner.getValue());
                LocalTime time = LocalTime.parse((String) timeCombo.getSelectedItem());
                int partySize = Integer.parseInt(partySizeField.getText().trim());

                List<Table> availableTables = service.findAvailableTables(date, time, partySize, config);

                if (availableTables.isEmpty()) {
                    assignedTable = null;
                    JOptionPane.showMessageDialog(this, "No tables available at selected time.", "No Availability", JOptionPane.WARNING_MESSAGE);
                } else {
                    assignedTable = availableTables.get(0);  // best-fit table

                    // Build popup
                    StringBuilder sb = new StringBuilder();
                    sb.append("Available tables for ").append(date).append(" at ").append(time).append(":\n\n");
                    for (Table t : availableTables) {
                        sb.append("Table #").append(t.getTableId())
                          .append(" (Capacity: ").append(t.getCapacity()).append(")\n");
                    }
                    sb.append("\nBest-fit table automatically assigned: #").append(assignedTable.getTableId());

                    JTextArea textArea = new JTextArea(sb.toString());
                    textArea.setEditable(false);
                    textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
                    textArea.setLineWrap(true);
                    textArea.setWrapStyleWord(true);

                    JScrollPane scrollPane = new JScrollPane(textArea);
                    scrollPane.setPreferredSize(new Dimension(400, 200));

                    JOptionPane.showMessageDialog(this, scrollPane, "Available Tables", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception ex) {
                assignedTable = null;
                JOptionPane.showMessageDialog(this, "Please enter your party size.", "Missing info.", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        
        reserveBtn.addActionListener(e -> {
            if (assignedTable == null) {
                JOptionPane.showMessageDialog(this, "Please check availability first.", "No Table Assigned", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                // Get and validate input fields
                String name = nameField.getText().trim();
                String phone = phoneField.getText().trim();
                String date = new SimpleDateFormat("yyyy-MM-dd").format(dateSpinner.getValue());
                LocalTime time = LocalTime.parse((String) timeCombo.getSelectedItem());
                int partySize = Integer.parseInt(partySizeField.getText().trim());
                String notes = noteField.getText().trim();

                if (name.isEmpty() || phone.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Name and phone number are required.", "Missing Info", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Create and save reservation
                Reservation reservation = new Reservation(config.getRestaurantId(), name, phone, date, time, assignedTable.getTableId(), partySize, notes);
                service.addReservation(reservation);

                // Confirmation dialog
                JOptionPane.showMessageDialog(this,
                    "Reservation confirmed!\n" +
                    "Name: " + name + "\n" +
                    "Phone: " + phone + "\n" +
                    "Date: " + date + "\n" +
                    "Time: " + time + "\n" +
                    "Party Size: " + partySize + "\n" +
                    "Table #: " + assignedTable.getTableId() + "\n" +
                    "Special Request: " + notes,
                    "Reservation Confirmed",
                    JOptionPane.INFORMATION_MESSAGE
                );

                nameField.setText("");
                phoneField.setText("");
                partySizeField.setText("");
                noteField.setText("");
                assignedTable = null;

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error creating reservation: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        searchBtn.addActionListener(e -> {
        	String name = nameField.getText().trim().toLowerCase();
            String phone = phoneField.getText().trim();
            
            if (name.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name and phone number are required.", "Missing Info", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            List<Reservation> matches = service.findByNameAndPhone(name, phone).stream()
                    .filter(r -> r.getRestaurantId().equals(config.getRestaurantId()))
                    .collect(Collectors.toList());
            
            if (matches.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No reservations found.");
                return;
            }
            
            ReservationActionHandler.showReservationList(
                    this,
                    matches,
                    new CancelReservationStrategy(service, new DefaultListModel<>()),
                    new ModifyReservationStrategy(service, config)
                );
    	});
    }
        
  
}

