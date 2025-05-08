package OOProject;

import javax.swing.SwingUtilities;

public class Main {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {new ServiceSelectorGUI().setVisible(true);});

	}

}
