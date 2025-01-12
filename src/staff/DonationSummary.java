package staff;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;

public class DonationSummary {
    private JFrame frame;
    private JLabel totalDonationLabel;
    private JList<String> donationHistoryList;
    private DefaultListModel<String> listModel;

    public DonationSummary() {
        // Initialize the frame without a title bar (undecorated)
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
//        frame.setUndecorated(true); // Remove window decoration (title bar)

        // Title panel (minimal style)
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(0, 122, 204));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel titleLabel = new JLabel("Total Donation Summary", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);

        // Donation amount label with modern look
        totalDonationLabel = new JLabel("Loading...", SwingConstants.CENTER);
        totalDonationLabel.setFont(new Font("Segoe UI", Font.PLAIN, 50));
        totalDonationLabel.setForeground(new Color(0, 128, 0));
        totalDonationLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Donation history panel
        listModel = new DefaultListModel<>();
        donationHistoryList = new JList<>(listModel);
        donationHistoryList.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        donationHistoryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        donationHistoryList.setBackground(new Color(245, 245, 245));
        donationHistoryList.setSelectionBackground(new Color(0, 122, 204));
        donationHistoryList.setSelectionForeground(Color.WHITE);
        JScrollPane historyScrollPane = new JScrollPane(donationHistoryList);
        historyScrollPane.setPreferredSize(new Dimension(500, 250));

        // Left panel for total donation (50% width)
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());
        leftPanel.setBackground(Color.WHITE);
        leftPanel.add(totalDonationLabel, BorderLayout.CENTER);

        // Right panel for donation history (50% width)
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.setBackground(new Color(245, 245, 245));
        rightPanel.add(historyScrollPane, BorderLayout.CENTER);

        // bakery.Main panel with flexible layout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(1, 2, 10, 0));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);

        // Add title panel on top
        frame.add(titlePanel, BorderLayout.NORTH);
        frame.add(mainPanel, BorderLayout.CENTER);

        // Fetch and display initial donation data
        updateDonationHistory();

        // Make the window visible
        frame.setVisible(true);
    }

    private void updateDonationHistory() {
        try {
            // Simulate fetching donation data
            double totalDonation = DatabaseManager.getTotalDonationFromOrders();
            DecimalFormat formatter = new DecimalFormat("#,###.00");
            totalDonationLabel.setText("RM" + formatter.format(totalDonation));

            List<String> history = DatabaseManager.getDonationHistoryFromOrders();
            listModel.clear();
            for (String donation : history) {
                listModel.addElement(donation);
            }
        } catch (Exception e) {
            totalDonationLabel.setText("Error loading data");
            listModel.clear();
            listModel.addElement("No data available");
        }
    }

    public static void createAndShowGUI() {
        SwingUtilities.invokeLater(DonationSummary::new);
    }
}
