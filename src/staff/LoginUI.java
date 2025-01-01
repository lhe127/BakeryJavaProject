package staff;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginUI::createLoginScreen);
    }

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
                CakeManagementUI.createAndShowGUI(); // Show the cake management UI
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid username or password.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Making the login frame visible
        frame.setLocationRelativeTo(null); // Center the frame on the screen
        frame.setVisible(true);
    }

    // Dummy method to simulate login validation
    private static boolean isValidUser(String username, char[] password) {
        // For demonstration purposes, you can use hardcoded values or validate from a database
        return "admin".equals(username) && "password".equals(new String(password));
    }
}
