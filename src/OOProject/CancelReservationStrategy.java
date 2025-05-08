package OOProject;

import javax.swing.*;

public class CancelReservationStrategy implements ReservationActionStrategy {
    private final ReservationService service;
    private final DefaultListModel<Reservation> listModel;

    public CancelReservationStrategy(ReservationService service, DefaultListModel<Reservation> listModel) {
        this.service = service;
        this.listModel = listModel;
    }

    @Override
    public void execute(Reservation reservation) {
        int confirm = JOptionPane.showConfirmDialog(null, 
            "Are you sure you want to cancel this reservation?", 
            "Confirm Cancel", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            service.removeReservation(reservation);
            listModel.removeElement(reservation);
            JOptionPane.showMessageDialog(null, "Reservation canceled.");
        }
    }
}