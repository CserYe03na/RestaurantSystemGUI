package OOProject;

import javax.swing.*;
import java.awt.*;

public class updateMembershipGUI extends JFrame {
    private JTextField nameField, phoneField, addressField, preferenceField;
    private Membership member;
    private MembershipService membershipService;

    public updateMembershipGUI(Membership member, MembershipService membershipService) {
        this.member = member;
        this.membershipService = membershipService;

        setTitle("Update Profile");
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        nameField = new JTextField(member.getName());
        phoneField = new JTextField(member.getPhoneNumber());
        addressField = new JTextField(member.getAddress());
        preferenceField = new JTextField(member.getPreference());

        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);
        panel.add(new JLabel("Address:"));
        panel.add(addressField);
        panel.add(new JLabel("Preference:"));
        panel.add(preferenceField);

        JButton confirmBtn = new JButton("Confirm");
        confirmBtn.addActionListener(e -> updateMember());

        panel.add(confirmBtn);
        add(panel);
    }

    private void updateMember() {
        String newName = nameField.getText().trim();
        String newPhone = phoneField.getText().trim();
        String address = addressField.getText().trim();
        String preference = preferenceField.getText().trim();

        member.setName(newName);
        member.setPhoneNumber(newPhone);
        member.setAddress(address);
        member.setPreference(preference);

        membershipService.saveToFile();

        JOptionPane.showMessageDialog(this,
                "Profile updated successfully!\n\n" + member.toString(),
                "Update Successful",
                JOptionPane.INFORMATION_MESSAGE
            );
        
        dispose();
    }
}
