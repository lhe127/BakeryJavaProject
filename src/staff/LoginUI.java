package staff;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * The {@code LoginUI} class represents the user interface for the login screen
 * and main menu of the staff dashboard application. It provides an intuitive
 * login system and navigation to other features like cake management, donation
 * summary, and order summary.
 */
public class LoginUI {

    /**
     * The main entry point of the application.
     * Launches the login screen on the Event Dispatch Thread (EDT).
     *
     * @param args the command-line arguments (not used)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginUI::createLoginScreen);
    }

    /**
     * Creates and displays the login screen with input fields for username and password.
     * Includes a "Show Password" option and a login button with validation.
     */
    private static void createLoginScreen() {
        JFrame frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        // Background Panel
        JPanel backgroundPanel = new JPanel();
        backgroundPanel.setLayout(new GridBagLayout());
        backgroundPanel.setBackground(new Color(240, 240, 255)); // Light blue background

        // Font and style customization
        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Font textFieldFont = new Font("Arial", Font.PLAIN, 12);

        // GridBagConstraints for positioning components
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding around components

        // Username Field
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 0;
        backgroundPanel.add(usernameLabel, gbc);

        JTextField usernameField = new JTextField(20);
        usernameField.setFont(textFieldFont);
        gbc.gridx = 1;
        backgroundPanel.add(usernameField, gbc);

        // Password Field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 1;
        backgroundPanel.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setFont(textFieldFont);
        gbc.gridx = 1;
        backgroundPanel.add(passwordField, gbc);

        // Password Visibility Toggle
        JCheckBox showPasswordCheckBox = new JCheckBox("Show Password");
        gbc.gridx = 1;
        gbc.gridy = 2;
        backgroundPanel.add(showPasswordCheckBox, gbc);

        showPasswordCheckBox.addActionListener(e -> {
            if (showPasswordCheckBox.isSelected()) {
                passwordField.setEchoChar((char) 0); // Show password
            } else {
                passwordField.setEchoChar('*'); // Hide password
            }
        });

        // Login Button
        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setBackground(new Color(30, 144, 255)); // Blue button color
        loginButton.setForeground(Color.WHITE);
        gbc.gridy = 3;
        gbc.gridwidth = 2; // Make the button span across two columns
        backgroundPanel.add(loginButton, gbc);

        // Adding the background panel to the frame
        frame.add(backgroundPanel, BorderLayout.CENTER);

        // Action Listener for the login button
        loginButton.addActionListener((ActionEvent e) -> {
            String username = usernameField.getText();
            char[] password = passwordField.getPassword();

            if (isValidUser(username, password)) {
                JOptionPane.showMessageDialog(frame, "Login Successful!");
                frame.dispose(); // Close the login screen
                showMainMenu(); // Show the main menu UI
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid username or password.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Making the login frame visible
        frame.setLocationRelativeTo(null); // Center the frame on the screen
        frame.setVisible(true);
    }

    /**
     * Validates the login credentials entered by the user.
     *
     * @param username the username entered by the user
     * @param password the password entered by the user
     * @return {@code true} if the username and password are valid, otherwise {@code false}
     */
    private static boolean isValidUser(String username, char[] password) {
        // For demonstration purposes, you can use hardcoded values or validate from a database
        return "admin".equals(username) && "password".equals(new String(password));
    }

    /**
     * Displays the main menu UI after successful login.
     * Provides options for navigating to Cake Management, Donation Summary, and Order Summary.
     */
    private static void showMainMenu() {
        JFrame mainMenuFrame = new JFrame("Staff Dashboard");
        mainMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainMenuFrame.setSize(500, 350);
        mainMenuFrame.setLayout(new GridBagLayout());

        // Background Panel
        JPanel backgroundPanel = new JPanel();
        backgroundPanel.setBackground(new Color(255, 255, 255)); // White background
        backgroundPanel.setLayout(new GridBagLayout());

        // Font Customization
        Font buttonFont = new Font("Arial", Font.BOLD, 16);

        // GridBagConstraints for positioning components
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15); // Padding around components
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        // Title Label
        JLabel welcomeLabel = new JLabel("Welcome to the Staff Dashboard!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(new Color(30, 144, 255)); // Blue color for title
        backgroundPanel.add(welcomeLabel, gbc);

        // Cake Management Button
        JButton cakeManagementButton = createButton("Cake Management");
        gbc.gridy = 1;
        backgroundPanel.add(cakeManagementButton, gbc);

        // Donation Summary Button
        JButton donationSummaryButton = createButton("Donation Summary");
        gbc.gridy = 2;
        backgroundPanel.add(donationSummaryButton, gbc);

        // Order Summary Button
        JButton orderSummaryButton = createButton("Order Summary");
        gbc.gridy = 3;
        backgroundPanel.add(orderSummaryButton, gbc);

        // Action listeners for buttons
        cakeManagementButton.addActionListener(e -> {
            CakeManagementUI.createAndShowGUI();
            mainMenuFrame.dispose();
        });

        donationSummaryButton.addActionListener(e -> {
            DonationSummary.createAndShowGUI();
            mainMenuFrame.dispose();
        });

        orderSummaryButton.addActionListener(e -> {
            StaffOrderSummary ordersummary = new StaffOrderSummary();
            ordersummary.createAndShowGUI();
            mainMenuFrame.dispose();
        });

        // Adding the background panel to the frame
        mainMenuFrame.add(backgroundPanel);

        // Make the frame visible and center it
        mainMenuFrame.setLocationRelativeTo(null);
        mainMenuFrame.setVisible(true);
    }

    /**
     * Creates a styled button for the main menu with hover effects and custom dimensions.
     *
     * @param text the label to be displayed on the button
     * @return a styled {@link JButton} instance
     */
    private static JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(new Color(30, 144, 255)); // Blue button color
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(250, 50)); // Button size
        button.setFocusable(false);

        // Adding hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(70, 130, 180)); // Darker blue on hover
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(30, 144, 255)); // Original blue
            }
        });

        return button;
    }
}
