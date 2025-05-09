package OOProject;

import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

public class DinnerMembershipGUI extends JFrame {
    private MembershipService membershipService;
    private String restaurantId;
    private String restaurantName;
    
    private JTextField nameField, phoneField;
    private JTextArea outputArea;

    public DinnerMembershipGUI(MembershipService membershipService, String restaurantId, String restaurantName) {
        this.membershipService = membershipService;
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        
        setTitle("Dinner Membership Portal");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        

        // Input Panel - Label for restaurant
        JLabel restaurantLabel = new JLabel("Restaurant: " + restaurantName);
        restaurantLabel.setHorizontalAlignment(SwingConstants.CENTER);
        restaurantLabel.setFont(new Font("Arial", Font.BOLD, 14));

        // Input Panel - Input for name and phone
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);
        formPanel.add(new JLabel("Phone:"));
        phoneField = new JTextField();
        formPanel.add(phoneField);

        JPanel inputPanel = new JPanel(new BorderLayout(10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Profile Info"));
        inputPanel.add(restaurantLabel, BorderLayout.NORTH);
        inputPanel.add(formPanel, BorderLayout.CENTER);
        //inputPanel.add(new JLabel("Address:"));
        //inputPanel.add(addressField);

        // Buttons
        JButton createBtn = new JButton("Create Profile");
        JButton updateBtn = new JButton("Update Profile");
        JButton checkBtn = new JButton("Check Credits");
        
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        buttonPanel.add(createBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(checkBtn);

        // Output area
        outputArea = new JTextArea(6, 40);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        // Action Listeners
        createBtn.addActionListener(e -> createProfile());
        updateBtn.addActionListener(e -> updateMembershipGUI());
        checkBtn.addActionListener(e -> checkCredits());

        // Layout setup
        setLayout(new BorderLayout(10, 10));
        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);
    }

    private void createProfile() {
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();

        if (name.isEmpty() || phone.isEmpty()) {
            outputArea.setText("Name and phone number are required to create profile.");
            return;
        }

        String memberId = UUID.randomUUID().toString(); // unique ID
        Membership newMember = new Membership(restaurantId, memberId, name, phone);
        if(membershipService.addMember(newMember)) {
            membershipService.saveToFile();
            outputArea.setText("Membership profile created successfully:\n" + newMember);
        }
        else {
        	outputArea.setText("Member Exists.");
        }

    }

    private void updateMembershipGUI() {
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();

        if (name.isEmpty() || phone.isEmpty()) {
            outputArea.setText("Name and phone number are required to update.");
            return;
        }

        Membership member = membershipService.getMember(restaurantId, name, phone);

        if (member != null) {
            new updateMembershipGUI(member, membershipService).setVisible(true);
        } else {
            outputArea.setText("No member found with the given name and phone.");
        }
    }

    private void checkCredits() {
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        if (name.isEmpty() || phone.isEmpty()) {
            outputArea.setText("Name and phone number are required to check credits.");
            return;
        }

        Membership member = membershipService.getMember(restaurantId, name, phone);
        if (member != null) {
            outputArea.setText("Your credits: " + member.getCredits() + "\nLevel: " + member.getMembershipLevel());
        } else {
            outputArea.setText("Member not found.");
        }
    }


}

