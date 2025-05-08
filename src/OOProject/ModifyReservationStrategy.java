package OOProject;

import javax.swing.*;
import java.awt.*;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public class ModifyReservationStrategy implements ReservationActionStrategy {
    private final ReservationService service;
    private final RestaurantConfig config;

    public ModifyReservationStrategy(ReservationService service, RestaurantConfig config) {
        this.service = service;
        this.config = config;
    }

    @Override
    public void execute(Reservation oldRes) {
        JPanel panel = new JPanel(new GridLayout(4, 2));
        JTextField dateField = new JTextField(oldRes.getDate());

        String[] timeOptions = java.util.stream.IntStream.range(0, 25)
            .mapToObj(i -> String.format("%02d:%02d", 11 + i / 2, (i % 2 == 0 ? 0 : 30)))
            .toArray(String[]::new);
        JComboBox<String> timeCombo = new JComboBox<>(timeOptions);
        timeCombo.setSelectedItem(oldRes.getTimeSlot().toString());

        JTextField partyField = new JTextField(String.valueOf(oldRes.getPartySize()));
        JTextField noteField = new JTextField(oldRes.getNotes());

        panel.add(new JLabel("Date:"));
        panel.add(dateField);
        panel.add(new JLabel("Time:"));
        panel.add(timeCombo);
        panel.add(new JLabel("Party Size:"));
        panel.add(partyField);
        panel.add(new JLabel("Special Request:"));
        panel.add(noteField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Modify Reservation", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String newDate = dateField.getText().trim();
                String newNotes = noteField.getText().trim();
                LocalTime newTime = LocalTime.parse((String) timeCombo.getSelectedItem());
                int newParty = Integer.parseInt(partyField.getText().trim());

                List<Table> availableTables = service.findAvailableTables(newDate, newTime, newParty, config);
                Optional<Table> newTable = availableTables.stream().findFirst();

                if (newTable.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No available table at that time.");
                    return;
                }

                service.removeReservation(oldRes);
                Reservation newRes = new Reservation(
                    config.getRestaurantId(), oldRes.getCustomerName(), oldRes.getPhonenumber(),
                    newDate, newTime, newTable.get().getTableId(), newParty, newNotes
                );
                service.addReservation(newRes);
                JOptionPane.showMessageDialog(null, "Reservation updated.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Update failed: " + ex.getMessage());
            }
        }
    }
}
