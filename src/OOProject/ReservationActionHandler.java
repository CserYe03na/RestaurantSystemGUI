package OOProject;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ReservationActionHandler {
	
    public static void showReservationList(JFrame parent, List<Reservation> reservations, ReservationActionStrategy cancelStrategy, ReservationActionStrategy modifyStrategy) {
        DefaultListModel<Reservation> listModel = new DefaultListModel<>();
        for (Reservation r : reservations) listModel.addElement(r);

        JList<Reservation> reservationList = new JList<>(listModel);
        reservationList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(reservationList);
        scrollPane.setPreferredSize(new Dimension(400, 200));

        JButton cancelBtn = new JButton("Cancel Reservation");
        JButton modifyBtn = new JButton("Modify Reservation");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(cancelBtn);
        buttonPanel.add(modifyBtn);

        JPanel dialogPanel = new JPanel(new BorderLayout());
        dialogPanel.add(scrollPane, BorderLayout.CENTER);
        dialogPanel.add(buttonPanel, BorderLayout.SOUTH);

        JDialog dialog = new JDialog(parent, "Your Reservations", true);
        dialog.setContentPane(dialogPanel);
        dialog.pack();
        dialog.setLocationRelativeTo(parent);

        cancelBtn.addActionListener(e -> {
            Reservation selected = reservationList.getSelectedValue();
            if (selected != null) {
                cancelStrategy.execute(selected);
            }
        });

        modifyBtn.addActionListener(e -> {
            Reservation selected = reservationList.getSelectedValue();
            if (selected != null) {
                modifyStrategy.execute(selected);
            }
        });

        dialog.setVisible(true);
    }
}

