package OOProject;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public abstract class AbstractManagerGUI extends JFrame {
    protected JTable table;
    protected DefaultTableModel tableModel;
    protected JTextField searchNameField, searchPhoneField;
    protected JButton loadBtn, manageBtn, searchBtn;
    protected final RestaurantConfig config;

    public AbstractManagerGUI(RestaurantConfig config, String title, String[] columnHeaders) {
        this.config = config;

        setTitle(title);
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Top panel with restaurant info and buttons
        JPanel topPanel = new JPanel(new FlowLayout());
        JLabel restaurantLabel = new JLabel("Restaurant: " + config.getRestaurantId() + " - " + config.getRestaurantName());
        restaurantLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

        loadBtn = new JButton("Load Customer Info");
        manageBtn = new JButton("Manage Selected Customer");

        topPanel.add(restaurantLabel);
        topPanel.add(loadBtn);
        topPanel.add(manageBtn);

        // Optional search panel
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchNameField = new JTextField(10);
        searchPhoneField = new JTextField(10);
        searchBtn = new JButton("Search");
        searchPanel.add(new JLabel("Search Name:"));
        searchPanel.add(searchNameField);
        searchPanel.add(new JLabel("Phone:"));
        searchPanel.add(searchPhoneField);
        searchPanel.add(searchBtn);

        JPanel topWrapper = new JPanel();
        topWrapper.setLayout(new BoxLayout(topWrapper, BoxLayout.Y_AXIS));
        topWrapper.add(topPanel);
        topWrapper.add(searchPanel);

        tableModel = new DefaultTableModel(columnHeaders, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        setLayout(new BorderLayout());
        add(topWrapper, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Hook up buttons
        loadBtn.addActionListener(e -> loadData());
        manageBtn.addActionListener(e -> manageSelectedItem());
        searchBtn.addActionListener(e -> search());
    }

    protected abstract void loadData();
    protected abstract void manageSelectedItem();
    protected abstract void search();
}
