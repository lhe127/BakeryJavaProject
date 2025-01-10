package staff;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;

public class DonationSummary {
    private JFrame frame;
    private JLabel totalDonationLabel;
    private JButton refreshButton;
    private JList<String> donationHistoryList;
    private DefaultListModel<String> listModel;

    public DonationSummary() {
        // Initialize the frame
        frame = new JFrame("Total Donation Summary");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400); // Increased size for history display
        frame.setLocationRelativeTo(null); // Center the window
        frame.setLayout(new BorderLayout());

        // Title label with some custom font and background
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(70, 130, 180)); // Steel blue
        JLabel titleLabel = new JLabel("Total Donation");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);

        // Donation amount label
        totalDonationLabel = new JLabel("Loading...");
        totalDonationLabel.setFont(new Font("Segoe UI", Font.PLAIN, 36));
        totalDonationLabel.setForeground(new Color(34, 139, 34)); // Green
        totalDonationLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Refresh button to update total donation
        refreshButton = new JButton("Refresh");
        refreshButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        refreshButton.setBackground(new Color(255, 140, 0)); // Orange
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);
        refreshButton.setPreferredSize(new Dimension(120, 40));
        refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshButton.addActionListener(e -> updateDonationHistory());

        // Create a JList for displaying donation history
        listModel = new DefaultListModel<>();
        donationHistoryList = new JList<>(listModel);
        donationHistoryList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        donationHistoryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        donationHistoryList.setVisibleRowCount(10); // Show up to 10 items at a time
        JScrollPane historyScrollPane = new JScrollPane(donationHistoryList);
        historyScrollPane.setPreferredSize(new Dimension(500, 150));

        // Add components to the frame
        frame.add(titlePanel, BorderLayout.NORTH);
        frame.add(totalDonationLabel, BorderLayout.CENTER);
        frame.add(refreshButton, BorderLayout.SOUTH);
        frame.add(historyScrollPane, BorderLayout.EAST); // Add the history scroll pane to the frame

        // Fetch and display the initial donation amount and history
        updateDonationHistory();

        // Make the window visible
        frame.setVisible(true);
    }

    private void updateDonationHistory() {
        // Update the total donation
        double totalDonation = DatabaseManager.getTotalDonationFromOrders();
        DecimalFormat formatter = new DecimalFormat("#,###.00"); // Format the donation amount
        totalDonationLabel.setText("RM" + formatter.format(totalDonation)); // Display donation amount

        // Fetch and display the donation history
        List<String> history = DatabaseManager.getDonationHistoryFromOrders();
        listModel.clear(); // Clear the previous history
        for (String donation : history) {
            listModel.addElement(donation); // Add new history to the list
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DonationSummary::new);
    }
}
