package OOProject;

import javax.swing.*;
import java.util.List;

public class ManagerMembershipGUI extends AbstractManagerGUI {
    private MembershipService service;

    public ManagerMembershipGUI(MembershipService service, RestaurantConfig config) {
        super(config, "Manager - Membership Management",
                new String[]{"Member ID", "Name", "Phone", "Credits", "Level"});
        this.service = service;
    }

    @Override
    protected void loadData() {
        tableModel.setRowCount(0);
        List<Membership> members = service.getAllMembers();
        for (Membership m : members) {
            if (m.getRestaurantId().equals(config.getRestaurantId())) {
                tableModel.addRow(new Object[]{
                        m.getMemberId(),
                        m.getName(),
                        m.getPhoneNumber(),
                        m.getCredits(),
                        m.getMembershipLevel()
                });
            }
        }
    }

    @Override
    protected void manageSelectedItem() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a member to manage.");
            return;
        }

        String name = tableModel.getValueAt(row, 1).toString();
        String phone = tableModel.getValueAt(row, 2).toString();
        Membership member = service.getMember(config.getRestaurantId(), name, phone);

        if (member == null) {
            JOptionPane.showMessageDialog(this, "Member not found.");
            return;
        }

        JTextField creditField = new JTextField(5);
        Object[] message = {"Credits to add (+) or deduct (-):", creditField};

        int option = JOptionPane.showConfirmDialog(this, message, "Manage Credits", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                int amount = Integer.parseInt(creditField.getText().trim());
                if (amount > 0) {
                    member.addCredits(amount);
                } else {
                    member.useCredits(-amount);
                }
                service.saveToFile();
                JOptionPane.showMessageDialog(this, "Credits updated successfully.");
                loadData();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please enter a number.");
            }
        }
    }
    
    @Override
    protected void search() {
    	String name = searchNameField.getText().trim();
        String phone = searchPhoneField.getText().trim();
        if (phone.isEmpty() || name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name and phone number are required.",
                    "Missing Info", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Membership m = service.getMember(config.getRestaurantId(), name, phone);
        tableModel.setRowCount(0);
        tableModel.addRow(new Object[]{
        		m.getMemberId(),
                m.getName(),
                m.getPhoneNumber(),
                m.getCredits(),
                m.getMembershipLevel()
        });
    }
}
