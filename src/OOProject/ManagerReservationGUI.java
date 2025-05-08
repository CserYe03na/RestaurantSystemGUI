package OOProject;

import javax.swing.*;
import java.util.List;
import java.util.stream.Collectors;


public class ManagerReservationGUI extends AbstractManagerGUI {
	private ReservationService service;

    public ManagerReservationGUI(ReservationService service, RestaurantConfig config) {
        super(config, "Manager - View Reservations",
                new String[]{"Customer", "Phone", "Date", "Time", "Party", "Table #", "Special Request"});
        this.service = service;
    }

    @Override
    protected void loadData() {
        tableModel.setRowCount(0);
        String currentRestaurantId = config.getRestaurantId();

        for (Reservation r : service.getAllReservations()) {
            if (r.getRestaurantId().equals(currentRestaurantId)) {
                tableModel.addRow(new Object[]{
                        r.getCustomerName(),
                        r.getPhonenumber(),
                        r.getDate(),
                        r.getTimeSlot(),
                        r.getPartySize(),
                        r.getTableId(),
                        r.getNotes()
                });
            }
        }
    }

    @Override
    protected void manageSelectedItem() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a reservation to manage.");
            return;
        }

        String customer = tableModel.getValueAt(selectedRow, 0).toString();
        String phone = tableModel.getValueAt(selectedRow, 1).toString();
        String date = tableModel.getValueAt(selectedRow, 2).toString();
        String time = tableModel.getValueAt(selectedRow, 3).toString();
        int tableId = Integer.parseInt(tableModel.getValueAt(selectedRow, 5).toString());

        Reservation selectedReservation = service.getAllReservations().stream()
                .filter(r -> r.getCustomerName().equals(customer) &&
                        r.getPhonenumber().equals(phone) &&
                        r.getDate().equals(date) &&
                        r.getTimeSlot().toString().equals(time) &&
                        r.getTableId() == tableId &&
                        r.getRestaurantId().equals(config.getRestaurantId()))
                .findFirst()
                .orElse(null);

        if (selectedReservation != null) {
            ReservationActionHandler.showReservationList(
                    this,
                    List.of(selectedReservation),
                    new CancelReservationStrategy(service, new DefaultListModel<>()),
                    new ModifyReservationStrategy(service, config)
            );
            loadData();
        } else {
            JOptionPane.showMessageDialog(this, "Unable to find full reservation details.");
        }
    }

    @Override
    protected void search() {
        if (service == null) return;

        String name = searchNameField.getText().trim().toLowerCase();
        String phone = searchPhoneField.getText().trim();
        if (phone.isEmpty() || name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name and phone number are required.",
                    "Missing Info", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<Reservation> matches = service.findByNameAndPhone(name, phone).stream()
                .filter(r -> r.getRestaurantId().equals(config.getRestaurantId()))
                .collect(Collectors.toList());

        tableModel.setRowCount(0);
        for (Reservation r : matches) {
            tableModel.addRow(new Object[]{
                    r.getCustomerName(),
                    r.getPhonenumber(),
                    r.getDate(),
                    r.getTimeSlot(),
                    r.getPartySize(),
                    r.getTableId(),
                    r.getNotes()
            });
        }
    }

}

