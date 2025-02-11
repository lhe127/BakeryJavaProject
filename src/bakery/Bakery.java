package bakery;

import staff.DatabaseManager;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.awt.event.*;
import javax.swing.border.*;
import javax.swing.table.*;

/**
 * The Bakery class represents the main interface for the SUC Bakery application.
 * It allows users to view a welcome screen, place orders, and track their orders.
 * The class also handles the GUI components and event listeners for user interactions.
 */
public class Bakery {
    /**
     * List of JLabel components representing counters.
     */
    ArrayList<JLabel> counterLabels = new ArrayList<>();

    /**
     * A JLabel component for displaying counter information.
     */
    JLabel counterLabel;

    /**
     * Constructs the Bakery application interface.
     * Sets up the main frame and panels for the welcome screen, logo display,
     * and navigation buttons (e.g., order and track order functionalities).
     */
    public Bakery(){
        JFrame homeFrame = new JFrame("SUC Bakery");
        homeFrame.setLayout(new BorderLayout());

        // Welcome Panel
        JPanel welcomePanel = new JPanel();
        welcomePanel.setPreferredSize(new Dimension(1100,150));
        welcomePanel.setBackground(new Color(255, 246, 243));

        JLabel label1 = new JLabel ("WELCOME TO SUC BAKERY");
        welcomePanel.add(label1);
        label1.setFont(new Font("Dialog", Font.BOLD, 50));

        // Logo Panel
        JPanel logoPanel = new JPanel();
        logoPanel.setPreferredSize(new Dimension(1100,250));

        JLabel image1 = new JLabel (new ImageIcon("src/img/logo.png"));
        logoPanel.add(image1);
        logoPanel.setBackground(new Color(255, 246, 243));

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setPreferredSize(new Dimension(400, 150));
        buttonPanel.setBackground(new Color(255, 246, 243));

        JButton menuButton = new JButton("ORDER NOW");
        JButton trackButton = new JButton("TRACK ORDER");

        buttonPanel.add(menuButton);
        buttonPanel.add(trackButton);

        menuButton.setPreferredSize(new Dimension(200, 100));
        menuButton.setFont(new Font("SansSerif", Font.BOLD, 20));
        menuButton.setForeground(Color.red);

        trackButton.setPreferredSize(new Dimension(200, 100));
        trackButton.setFont(new Font("SansSerif", Font.BOLD, 20));
        trackButton.setForeground(new Color(42,178,123));

        // Add functionality to the "Track Order" button
        trackButton.addActionListener(new ActionListener() {

            /**
             * Displays the "Track Order" window when the "Track Order" button is clicked.
             *
             * @param e the action event triggered by clicking the "Track Order" button
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame trackFrame = new JFrame("Track Order");
                trackFrame.setLayout(new BorderLayout());

                // Input Panel
                JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
                inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

                JTextField nameField = new JTextField(20);
                JTextField phoneField = new JTextField(20);
                JButton searchButton = new JButton("Search");

                inputPanel.add(new JLabel("Name:"));
                inputPanel.add(nameField);
                inputPanel.add(new JLabel("Phone Number:"));
                inputPanel.add(phoneField);
                inputPanel.add(new JLabel("")); // Empty label for spacing
                inputPanel.add(searchButton);

                // Panel for displaying order details
                /**
                 * Creates a panel to display order details retrieved from the database.
                 * Configures the layout and spacing of the panel.
                 */
                JPanel orderDetailsPanel = new JPanel();
                orderDetailsPanel.setLayout(new BoxLayout(orderDetailsPanel, BoxLayout.Y_AXIS));
                orderDetailsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

                // Add components to the frame
                /**
                 * Adds the input panel and the order details panel to the frame.
                 * Positions the input panel at the top (NORTH) and the order details
                 * panel inside a scrollable pane at the center (CENTER) of the frame.
                 */
                trackFrame.add(inputPanel, BorderLayout.NORTH);
                trackFrame.add(new JScrollPane(orderDetailsPanel), BorderLayout.CENTER);

                // Search button functionality
                searchButton.addActionListener(new ActionListener() {
                    /**
                     * Handles the action when the "Search" button is clicked.
                     * Validates the user's input, retrieves order details from the database,
                     * and updates the order details panel with the retrieved information.
                     *
                     * @param e the action event triggered by clicking the "Search" button
                     */
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        orderDetailsPanel.removeAll();

                        // Retrieve and trim user inputs
                        String name = nameField.getText().trim();
                        String phone = phoneField.getText().trim();

                        // Validate inputs
                        if (name.isEmpty() || phone.isEmpty()) {
                            JOptionPane.showMessageDialog(trackFrame,
                                    "Please enter both name and phone number",
                                    "Missing Information",
                                    JOptionPane.WARNING_MESSAGE);
                            return;
                        }

                        // Search and display order status
                        displayOrderStatus(name, phone, orderDetailsPanel);
                        orderDetailsPanel.revalidate();
                        orderDetailsPanel.repaint();
                    }
                });

                // Frame properties
                /**
                 * Configures the "Track Order" frame size, location, and visibility.
                 */
                trackFrame.setSize(600, 400);
                trackFrame.setLocationRelativeTo(null);
                trackFrame.setVisible(true);
            }

            /**
             * Retrieves and displays the order status based on the customer's name and phone number.
             *
             * @param customerName the name of the customer
             * @param phone the phone number of the customer
             * @param panel the panel where order details will be displayed
             */
            private void displayOrderStatus(String customerName, String phone, JPanel panel) {
                try (Connection conn = DatabaseManager.getConnection()) {
                    String query = """
                SELECT o.id, o.status, o.delivery_type, o.delivery_address,
                       c.name, c.phone, GROUP_CONCAT(oi.quantity, 'x ', ca.name) as items,
                       o.total_amount, o.donation_amount
                FROM `Order` o
                JOIN Customer c ON o.customer_id = c.id
                JOIN orderitems oi ON o.id = oi.order_id
                JOIN cakes ca ON oi.cake_id = ca.id
                WHERE c.name = ? AND c.phone = ? 
                AND (o.status = 'preparing' OR o.status = 'ready')
                GROUP BY o.id
                ORDER BY o.id DESC
                LIMIT 1
            """;

                    try (PreparedStatement stmt = conn.prepareStatement(query)) {
                        stmt.setString(1, customerName);
                        stmt.setString(2, phone);
                        ResultSet rs = stmt.executeQuery();

                        if (rs.next()) {
                            // Order found, create a panel for each section
                            JPanel detailsPanel = new JPanel();
                            detailsPanel.setLayout(new GridLayout(0, 1, 0, 5));
                            detailsPanel.setBackground(new Color(255, 246, 243));

                            // Get order details
                            String status = rs.getString("status");
                            String deliveryType = rs.getString("delivery_type");
                            String address = rs.getString("delivery_address");
                            String items = rs.getString("items");
                            double totalAmount = rs.getDouble("total_amount");
                            double donationAmount = rs.getDouble("donation_amount");
                            int orderId = rs.getInt("id");

                            // Create and add all labels
                            JLabel orderIdLabel = new JLabel("Order #" + orderId);
                            orderIdLabel.setFont(new Font("SansSerif", Font.BOLD, 20));

                            JLabel statusLabel = new JLabel("Status: " + status.toUpperCase());
                            statusLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
                            statusLabel.setForeground(status.equals("ready") ?
                                    new Color(42,178,123) : new Color(255, 165, 0));

                            // Add details with consistent font
                            Font detailFont = new Font("SansSerif", Font.PLAIN, 14);

                            detailsPanel.add(orderIdLabel);
                            detailsPanel.add(statusLabel);
                            detailsPanel.add(createDetailLabel("Customer: " + customerName, detailFont));
                            detailsPanel.add(createDetailLabel("Phone: " + phone, detailFont));
                            detailsPanel.add(createDetailLabel("Delivery: " + deliveryType, detailFont));
                            detailsPanel.add(createDetailLabel("Address: " + address, detailFont));
                            detailsPanel.add(createDetailLabel("Items:", detailFont));

                            /**
                             * Splits the list of items from the order and displays each on a new line in the details panel.
                             */
                            String[] itemsList = items.split(",");
                            for (String item : itemsList) {
                                detailsPanel.add(createDetailLabel("   • " + item.trim(), detailFont));
                            }

                            // Add total and donation amounts to the details panel
                            detailsPanel.add(createDetailLabel(String.format("Total Amount: RM %.2f", totalAmount), detailFont));
                            detailsPanel.add(createDetailLabel(String.format("Donation Amount: RM %.2f", donationAmount), detailFont));

                            // Add the details panel to the main panel
                            panel.add(detailsPanel);

                        } else {
                            // No order found
                            JLabel notFoundLabel = new JLabel("No active orders found for " + customerName);
                            notFoundLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
                            notFoundLabel.setForeground(Color.RED);
                            panel.add(notFoundLabel);
                        }
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JLabel errorLabel = new JLabel("Error retrieving order information");
                    errorLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
                    errorLabel.setForeground(Color.RED);
                    panel.add(errorLabel);
                }
            }

            private JLabel createDetailLabel(String text, Font font) {
                JLabel label = new JLabel(text);
                label.setFont(font);
                return label;
            }
        });

        // Configure the home frame layout
        homeFrame.add(welcomePanel, BorderLayout.NORTH);
        homeFrame.add(logoPanel, BorderLayout.CENTER);
        homeFrame.add(buttonPanel, BorderLayout.SOUTH);

        /**
         * Listener for the "Menu" button that navigates to the SUC Bakery interface.
         */
        homeFrame.setSize(1100, 750);
        homeFrame.setResizable(true);
        homeFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        homeFrame.setVisible(true);
        homeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        menuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close the home frame
                homeFrame.dispose();

                // Create the main frame for the bakery shop
                JFrame frame = new JFrame("SUC Bakery");
                BakeryShop bk = new BakeryShop();

                // Initialize the panels for layout
                JPanel mainPanel = new JPanel();
                JPanel receiptPanel = new JPanel(new BorderLayout());
                JPanel receiptContentPanel = new JPanel(new BorderLayout());
                JPanel checkoutPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

                // Configure the receipt and checkout panels
                receiptPanel.setBorder(new LineBorder(Color.BLACK, 2));
                receiptPanel.setBackground(new Color(255, 246, 243));
                receiptContentPanel.setBackground(new Color(255, 246, 243));
                checkoutPanel.setBackground(new Color(255, 246, 243));
                checkoutPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

                // Configure the main panel for product layout
                mainPanel.setPreferredSize(new Dimension(700, 750));
                mainPanel.setLayout(new GridLayout(3,3, 45, 45));

                // Create and configure the checkout button
                JButton checkoutButton = new JButton("Checkout");
                checkoutButton.setPreferredSize(new Dimension(150, 70));
                checkoutButton.setFont(new Font("SansSerif", Font.BOLD, 20));
                checkoutButton.setBackground(new Color(42,178,123,255));
                checkoutButton.setBorderPainted(false);
                checkoutButton.setOpaque(true);
                checkoutButton.setForeground(new Color(245,248,250,255));
                checkoutButton.setFocusPainted(false);

                // Add the checkout button to the checkout panel
                checkoutPanel.add(checkoutButton);
                receiptPanel.add(receiptContentPanel, BorderLayout.CENTER);
                receiptPanel.add(checkoutPanel, BorderLayout.SOUTH);

                // Iterate through the product list and display each product
                for (Products product : bk.products) {
                    JPanel productPanel = new JPanel();
                    productPanel.setBackground(new Color(255, 246, 243));
                    productPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));

                    // Configure the product image
                    JLabel imageLabel = new JLabel();
                    ImageIcon imageIcon = new ImageIcon(product.getImage());
                    Image image = imageIcon.getImage();
                    Image scaledImg = image.getScaledInstance(150, 100, java.awt.Image.SCALE_SMOOTH);
                    imageIcon = new ImageIcon(scaledImg);
                    imageLabel.setIcon(imageIcon);

                    // Configure the product name label
                    JLabel productNameLabel = new JLabel(product.getProductName());
                    productNameLabel.setPreferredSize(new Dimension(150, 40));  // Set the width to control truncation
                    productNameLabel.setFont(new Font("Arial", Font.BOLD, 18));
                    productNameLabel.setHorizontalAlignment(SwingConstants.CENTER);

                    /**
                     * Truncates product names that exceed 20 characters and appends ellipsis.
                     * Sets a tooltip to display the full product name.
                     */
                    String displayText = product.getProductName().length() > 20 ? product.getProductName().substring(0, 17) + "..." : product.getProductName();
                    productNameLabel.setText(displayText);

                    // Set tooltip to show the full product name
                    productNameLabel.setToolTipText(product.getProductName());
                    /**
                     * Displays the product's price in RM format.
                     */
                    JLabel priceLabel = new JLabel("RM " + Double.toString(product.getPrice()));

                    /**
                     * Adds a stock label to show available product stock.
                     * The stock value is displayed below the product's price.
                     */
                    JLabel stockLabel = new JLabel("Stock: " + product.getStock());
                    stockLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                    stockLabel.setHorizontalAlignment(SwingConstants.CENTER);

                    /**
                     * Organizes product details (name, price, and stock) into a detail panel.
                     */
                    JPanel detailPanel = new JPanel();
                    detailPanel.add(productNameLabel);
                    detailPanel.add(priceLabel);
                    detailPanel.add(stockLabel);  // Add stock label below the price
                    detailPanel.setLayout(new GridLayout(3, 1));  // Ensure stock label fits without breaking layout
                    detailPanel.setBackground(new Color(255, 246, 243));

                    /**
                     * Creates a counter panel for adjusting product quantities.
                     * Contains buttons for incrementing and decrementing quantities and a label to display the current count.
                     */
                    JPanel counterPanel = new JPanel();

                    JButton counterAddBtn = new JButton("+");
                    JButton counterSubtractBtn = new JButton("-");

                    counterLabel = new JLabel("0");

                    counterLabels.add(counterLabel);

                    /**
                     * Configures the "+" and "-" buttons for quantity adjustment.
                     */
                    counterAddBtn.setBackground(new Color(42,178,123,255));
                    counterAddBtn.setBorderPainted(false);
                    counterAddBtn.setForeground(new Color(245,248,250,255));
                    counterAddBtn.setOpaque(true);
                    counterAddBtn.setFocusPainted(false);

                    counterSubtractBtn.setBackground(new Color(42,178,123,255));
                    counterSubtractBtn.setBorderPainted(false);
                    counterSubtractBtn.setForeground(new Color(245,248,250,255));
                    counterSubtractBtn.setOpaque(true);
                    counterSubtractBtn.setFocusPainted(false);

                    /**
                     * Configures the counter panel layout and adds increment/decrement buttons with the counter label.
                     */
                    counterPanel.setBackground(new Color(245,248,250,255));
                    counterPanel.setPreferredSize(new Dimension(150, 30));
                    counterPanel.setLayout(new GridLayout(1, 3, 5, 5));
                    counterPanel.add(counterSubtractBtn);
                    counterPanel.add(counterLabel);
                    counterPanel.add(counterAddBtn);

                    /**
                     * Adds the product image, detail panel, and counter panel to the product panel.
                     */
                    productPanel.add(imageLabel);
                    productPanel.add(detailPanel);
                    productPanel.add(counterPanel);

                    /**
                     * Configures the counter label's font and alignment.
                     */
                    counterLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    counterLabel.setFont(new Font("Arial", Font.BOLD, 20));

                    /**
                     * Configures the product name label's font and alignment.
                     */
                    productNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    productNameLabel.setFont(new Font("Arial", Font.BOLD, 18));

                    /**
                     * Configures the price label's font and alignment.
                     */
                    priceLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    priceLabel.setFont(new Font("Arial", Font.BOLD, 22));

                    /**
                     * Increments the product quantity when the "+" button is clicked,
                     * ensuring the count does not exceed available stock.
                     */
                    counterAddBtn.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            int currentCount = product.getCounter();
                            if (currentCount < product.getStock()) { // Ensure we don't exceed stock
                                product.setCounter(++currentCount);
                                int index = bk.products.indexOf(product);
                                counterLabels.get(index).setText(Integer.toString(product.getCounter()));
                            }
                        }
                    });

                    /**
                     * Increments the product quantity when the "+" button is clicked,
                     * ensuring the count does not exceed available stock.
                     */
                    counterSubtractBtn.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (product.getCounter() > 0) {
                                int currentCount = product.getCounter();
                                product.setCounter(--currentCount);
                                int index = bk.products.indexOf(product);
                                counterLabels.get(index).setText(Integer.toString(product.getCounter()));
                            }
                        }
                    });

                    /**
                     * Adds the configured product panel to the main panel.
                     */
                    mainPanel.add(productPanel);
                }

                /**
                 * Creates and configures a "Confirm" button for confirming the order.
                 */
                JButton confirmButton = new JButton("Confirm");
                confirmButton.setPreferredSize(new Dimension(150, 70));
                confirmButton.setFont(new Font("SansSerif", Font.BOLD, 20));
                confirmButton.setBackground(new Color(42,178,123,255));
                confirmButton.setBorderPainted(false);
                confirmButton.setOpaque(true);
                confirmButton.setForeground(new Color(245,248,250,255));
                confirmButton.setFocusPainted(false);

                /**
                 * Creates and configures a "Clear Order" button for resetting the order.
                 */
                JButton clearButton = new JButton("Clear Order");
                clearButton.setPreferredSize(new Dimension(150,70));
                clearButton.setFont(new Font("SansSerif", Font.BOLD, 20));
                clearButton.setBackground(new Color(42,178,123,255));
                clearButton.setBorderPainted(false);
                clearButton.setOpaque(true);
                clearButton.setForeground(new Color(245,248,250,255));
                clearButton.setFocusPainted(false);

                /**
                 * Adds the confirm and clear buttons to the button panel.
                 */
                JPanel btnPanel = new JPanel();
                btnPanel.setPreferredSize(new Dimension(150,60));
                btnPanel.add(confirmButton);
                btnPanel.add(clearButton);

                /**
                 * Handles the creation of the bottom button panel (`btnPanel`) and its actions, including receipt generation,
                 * order clearing, and donation handling.
                 */
                btnPanel.setBorder(BorderFactory.createEmptyBorder(100, 0, 0, 0));
                btnPanel.setBackground(Color.WHITE);
                mainPanel.setBackground(Color.WHITE);

                mainPanel.add(Box.createVerticalStrut(0));
                mainPanel.add(btnPanel);

                checkoutButton.setVisible(false);

                /**
                 * Generates the receipt when the "Confirm" button is clicked.
                 * Calculates the total cost of the products and displays the receipt in a table format.
                 */
                confirmButton.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        JLabel receiptLabel = new JLabel("Receipt");
                        receiptLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
                        receiptLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));

                        double total = 0;

                        String[] columnNames = {"Product", "Quantity", "Price"};
                        DefaultTableModel model = new DefaultTableModel(columnNames,0);
                        JTable table = new JTable(model);

                        boolean hasItems = false;
                        for (Products product : bk.products) {
                            int quantity = product.getCounter();
                            if (quantity > 0) {
                                hasItems = true;
                                double price = quantity * product.getPrice();
                                total += price;

                                String stringPrice = String.format("RM %.2f", price);
                                Object[] data = {product.getProductName(), quantity, stringPrice};
                                model.addRow(data);
                            }
                        }
                        checkoutButton.setVisible(hasItems);
                        String stringTotal = String.format("RM %.2f", total);
                        Object [] totalData = {"TOTAL:", "", stringTotal};
                        model.addRow(totalData);

                        table.setRowHeight(30);
                        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
                        table.setBackground(new Color(255, 246, 243));

                        JTableHeader header = table.getTableHeader();
                        header.setFont(new Font("SansSerif", Font.BOLD, 16));

                        receiptContentPanel.removeAll();
                        receiptContentPanel.add(receiptLabel, BorderLayout.NORTH);
                        receiptContentPanel.add(new JScrollPane(table));
                        checkoutButton.setVisible(hasItems);
                        receiptContentPanel.revalidate();
                        receiptContentPanel.repaint();
                    }
                });

                /**
                 * Clears all products' counters and resets the receipt content when the "Clear Order" button is clicked.
                 */
                clearButton.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        for (int i = 0; i < bk.products.size(); i++) {
                            bk.products.get(i).setCounter(0);
                            counterLabels.get(i).setText("0");
                        }
                        receiptContentPanel.removeAll();
                        checkoutButton.setVisible(false);
                        receiptContentPanel.revalidate();
                        receiptContentPanel.repaint();
                    }
                });

                /**
                 * Opens a donation frame and handles donation selection or cancellation when the "Checkout" button is clicked.
                 * Users can choose predefined amounts or enter a custom donation amount.
                 *
                 * @param e ActionEvent triggered when the "Checkout" button is clicked.
                 */
                checkoutButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Order order = new Order();
                        JFrame donationFrame = new JFrame("Donation");

                        JButton button1 = new JButton("RM 1.00");
                        JButton button2 = new JButton("RM 2.00");
                        JButton button5 = new JButton("RM 5.00");
                        JButton button10 = new JButton("RM 10.00");
                        JButton customButton = new JButton("Custom Amount");
                        JButton cancelButton = new JButton("Cancel");

                        JPanel outerPanel = new JPanel(new BorderLayout());
                        outerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

                        ImageIcon donateIcon = new ImageIcon("src/img/donation.png");
                        ImageIcon scaledIcon = new ImageIcon(
                                donateIcon.getImage().getScaledInstance(300, 200, Image.SCALE_SMOOTH)
                        );
                        JLabel imageLabel = new JLabel(scaledIcon);
                        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

                        JPanel imagePanel = new JPanel(new BorderLayout());
                        imagePanel.add(imageLabel, BorderLayout.CENTER);
                        imagePanel.setPreferredSize(new Dimension(400, 250));

                        JPanel donationPanel = new JPanel(new GridLayout(3, 2, 20, 20));
                        donationPanel.add(button1);
                        donationPanel.add(button2);
                        donationPanel.add(button5);
                        donationPanel.add(button10);
                        donationPanel.add(customButton);
                        donationPanel.add(cancelButton);

                        outerPanel.add(imagePanel, BorderLayout.NORTH);
                        outerPanel.add(donationPanel, BorderLayout.CENTER);
                        donationFrame.add(outerPanel);
                        donationFrame.setSize(400, 500);
                        donationFrame.setLocationRelativeTo(null);
                        donationFrame.setVisible(true);

                        // Variable to store donation amount
                        final double[] donationAmount = {0.0};

                        ActionListener donationListener = new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                if (e.getSource() == button1) {
                                    donationAmount[0] = 1.0;
                                } else if (e.getSource() == button2) {
                                    donationAmount[0] = 2.0;
                                } else if (e.getSource() == button5) {
                                    donationAmount[0] = 5.0;
                                } else if (e.getSource() == button10) {
                                    donationAmount[0] = 10.0;
                                } else if (e.getSource() == customButton) {
                                    String input = JOptionPane.showInputDialog(donationFrame, "Enter your donation amount (RM):");
                                    try {
                                        double customAmount = Double.parseDouble(input);
                                        if (customAmount < 0) {
                                            JOptionPane.showMessageDialog(donationFrame, "Please enter a valid positive amount.", "Invalid Donation", JOptionPane.WARNING_MESSAGE);
                                            return;
                                        }
                                        donationAmount[0] = customAmount;
                                    } catch (NumberFormatException ex) {
                                        JOptionPane.showMessageDialog(donationFrame, "Please enter a valid number.", "Invalid Donation", JOptionPane.WARNING_MESSAGE);
                                        return;
                                    }
                                }
                                donationFrame.dispose();
                                finalizeOrder(order, donationAmount[0]);
                            }
                        };

                        // Bind donationListener to buttons
                        button1.addActionListener(donationListener);
                        button2.addActionListener(donationListener);
                        button5.addActionListener(donationListener);
                        button10.addActionListener(donationListener);
                        customButton.addActionListener(donationListener);
                        cancelButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                donationFrame.dispose();
                                finalizeOrder(order, 0.0);
                            }
                        });
                    }

                    /**
                     * Displays the donation dialog, processes the donation amount, and finalizes the order.
                     *
                     * @param order The current {@link Order} object representing the customer's order.
                     * @param donationAmount The amount donated by the customer.
                     */
                    private void finalizeOrder(Order order, double donationAmount) {
                        // Code for collecting customer information, processing delivery type, and proceeding to order summary
                        // Create a form to input additional information
                        JFrame infoFrame = new JFrame("Customer Information");
                        infoFrame.setLayout(new BorderLayout());

                        // Information Panel for User Details and Delivery Information
                        JPanel infoPanel = new JPanel();
                        infoPanel.setLayout(new GridLayout(6, 2, 10, 10)); // Increased the rows to 6 to accommodate Pickup option

                        // Customer Info Fields
                        JLabel nameLabel = new JLabel("Name:");
                        JTextField nameField = new JTextField();
                        JLabel emailLabel = new JLabel("Email:");
                        JTextField emailField = new JTextField();
                        JLabel phoneLabel = new JLabel("Phone:");
                        JTextField phoneField = new JTextField();

                        // Delivery Info Fields
                        JLabel addressLabel = new JLabel("Delivery Address:");
                        JTextField addressField = new JTextField();
                        JLabel deliveryTypeLabel = new JLabel("Delivery Type:");
                        String[] deliveryOptions = {"Standard", "Express", "Pickup"}; // Added Pickup option
                        JComboBox<String> deliveryTypeComboBox = new JComboBox<>(deliveryOptions);

                        deliveryTypeComboBox.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                // Check the selected option
                                String selectedOption = (String) deliveryTypeComboBox.getSelectedItem();
                                if ("Pickup".equals(selectedOption)) {
                                    addressField.setText(""); // Clear the address field
                                    addressField.setEnabled(false); // Disable the address field
                                    addressField.setBackground(Color.LIGHT_GRAY);
                                } else {
                                    addressField.setEnabled(true); // Enable the address field
                                    addressField.setBackground(Color.WHITE);
                                }
                            }
                        });

                        infoPanel.add(nameLabel);
                        infoPanel.add(nameField);
                        infoPanel.add(emailLabel);
                        infoPanel.add(emailField);
                        infoPanel.add(phoneLabel);
                        infoPanel.add(phoneField);
                        infoPanel.add(deliveryTypeLabel);
                        infoPanel.add(deliveryTypeComboBox);
                        infoPanel.add(addressLabel);
                        infoPanel.add(addressField);

                        JPanel buttonPanel = new JPanel();
                        JButton nextButton = new JButton("Next");
                        nextButton.setFont(new Font("SansSerif", Font.BOLD, 16));
                        nextButton.setBackground(new Color(42, 178, 123));
                        nextButton.setForeground(Color.WHITE);

                        buttonPanel.add(nextButton);

                        // Add the information panel and button to the frame
                        infoFrame.add(infoPanel, BorderLayout.CENTER);
                        infoFrame.add(buttonPanel, BorderLayout.SOUTH);

                        infoFrame.setSize(400, 350);
                        infoFrame.setLocationRelativeTo(null);
                        infoFrame.setVisible(true);

                        // Proceed to order summary once the user inputs the info
                        nextButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                // Save the customer and delivery info
                                String customerName = nameField.getText();
                                String customerEmail = emailField.getText();
                                String customerPhone = phoneField.getText();
                                String deliveryAddress = addressField.getText();
                                String deliveryType = (String) deliveryTypeComboBox.getSelectedItem();

                                // Set donation amount for the order
                                order.setDonationAmount(donationAmount);

                                for (Products product : bk.products) {
                                    int quantity = product.getCounter();
                                    if (quantity > 0) {
                                        order.addItem(product, quantity);
                                    }
                                }

                                // Optional: You can print or store the customer and delivery info for future use
                                System.out.println("Customer Info: " + customerName + ", " + customerEmail + ", " + customerPhone);
                                System.out.println("Delivery Info: " + deliveryAddress + ", " + deliveryType);

                                // If the user selects Pickup, don't ask for a delivery address
                                if (deliveryType.equals("Pickup")) {
                                    deliveryAddress = "Pickup at Store"; // Set a default message for Pickup
                                }

                                // Close the information frame and display order summary
                                infoFrame.dispose();
                                displayOrderSummary(order, customerName, customerEmail, customerPhone, deliveryAddress, deliveryType, donationAmount);
                            }
                        });
                    }

                    /**
                     * Saves the completed order to the database, including customer details, donation, and order items.
                     *
                     * @param order The {@link Order} object containing all order details and items.
                     * @param customerName The name of the customer placing the order.
                     * @param customerEmail The email address of the customer.
                     * @param customerPhone The phone number of the customer.
                     * @param deliveryAddress The address to which the order is to be delivered.
                     * @param deliveryType The type of delivery selected (e.g., Standard, Express, Pickup).
                     * @param donationAmount The amount donated by the customer.
                     */
                    private void saveOrderToDatabase(Order order, String customerName, String customerEmail, String customerPhone,
                                                     String deliveryAddress, String deliveryType, double donationAmount) {
                        try (Connection connection = DatabaseManager.getConnection()) {

                            // Save customer data to the database
                            // Save order details to the database
                            // Save order items to the database
                            String customerQuery = "INSERT INTO Customer (name, email, phone) VALUES (?, ?, ?)";
                            try (PreparedStatement customerStmt = connection.prepareStatement(customerQuery, Statement.RETURN_GENERATED_KEYS)) {
                                customerStmt.setString(1, customerName);
                                customerStmt.setString(2, customerEmail);
                                customerStmt.setString(3, customerPhone);
                                customerStmt.executeUpdate();
                                ResultSet customerRs = customerStmt.getGeneratedKeys();
                                customerRs.next();
                                int customerId = customerRs.getInt(1);

                                // Insert order data into the database
                                String orderQuery = "INSERT INTO `Order` (customer_id, donation_amount, total_amount, delivery_address, delivery_type) " +
                                        "VALUES (?, ?, ?, ?, ?)";
                                try (PreparedStatement orderStmt = connection.prepareStatement(orderQuery, Statement.RETURN_GENERATED_KEYS)) {
                                    orderStmt.setInt(1, customerId);
                                    orderStmt.setDouble(2, donationAmount);
                                    orderStmt.setDouble(3, order.getTotalAmount());
                                    orderStmt.setString(4, deliveryAddress);
                                    orderStmt.setString(5, deliveryType);
                                    orderStmt.executeUpdate();
                                    ResultSet orderRs = orderStmt.getGeneratedKeys();
                                    orderRs.next();
                                    int orderId = orderRs.getInt(1);

                                    // Insert order items (now using Product ID instead of Cake ID) into the database
                                    String itemQuery = "INSERT INTO orderitems (order_id, cake_id, quantity, total_price) VALUES (?, ?, ?, ?)";
                                    try (PreparedStatement itemStmt = connection.prepareStatement(itemQuery)) {
                                        for (OrderItem item : order.getItems()) {
                                            itemStmt.setInt(1, orderId);
                                            itemStmt.setInt(2, item.getProductId());  // Using getProductId() to get the Product's ID
                                            itemStmt.setInt(3, item.getQuantity());
                                            itemStmt.setDouble(4, item.getTotalPrice());
                                            itemStmt.addBatch();
                                        }
                                        itemStmt.executeBatch();
                                    }

                                    // Insert donation data into the database
                                    String donationQuery = "INSERT INTO donation (order_id, amount) VALUES (?, ?)";
                                    try (PreparedStatement donationStmt = connection.prepareStatement(donationQuery)) {
                                        donationStmt.setInt(1, orderId);
                                        donationStmt.setDouble(2, donationAmount);
                                        donationStmt.executeUpdate();
                                    }
                                }
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }

                    /**
                     * Displays the order summary to the customer in a separate JFrame and handles payment confirmation.
                     *
                     * @param order The {@link Order} object containing the customer's order details.
                     * @param customerName The name of the customer.
                     * @param customerEmail The email address of the customer.
                     * @param customerPhone The phone number of the customer.
                     * @param deliveryAddress The delivery address for the order.
                     * @param deliveryType The type of delivery selected (Standard, Express, Pickup).
                     * @param donationAmount The amount donated by the customer.
                     */
                    private void displayOrderSummary(Order order, String customerName, String customerEmail, String customerPhone,
                                                     String deliveryAddress, String deliveryType, double donationAmount) {
                        // Display order summary and payment confirmation in a separate JFrame
                        JFrame summaryFrame = new JFrame("Order Summary");
                        summaryFrame.setLayout(new BorderLayout());

                        summaryFrame.setSize(400, 500);
                        summaryFrame.setLocationRelativeTo(null);
                        ImageIcon paymentIcon = new ImageIcon("src/img/payment.jpg");
                        ImageIcon scaledIcon1 = new ImageIcon(
                                paymentIcon.getImage().getScaledInstance(300, 250, Image.SCALE_SMOOTH)
                        );
                        JLabel paymentLabel = new JLabel(scaledIcon1);
                        paymentLabel.setHorizontalAlignment(SwingConstants.CENTER);

                        JPanel paymentPanel = new JPanel(new BorderLayout());
                        paymentPanel.add(paymentLabel, BorderLayout.CENTER);
                        paymentPanel.setPreferredSize(new Dimension(400, 250));

                        JTextArea summaryArea = new JTextArea();
                        summaryArea.setEditable(false);
                        summaryArea.setFont(new Font("SansSerif", Font.PLAIN, 16));

                        StringBuilder summary = new StringBuilder();
                        summary.append("Order Summary:\n\n");
                        for (OrderItem item : order.getItems()) {
                            summary.append(String.format("%s x%d - RM %.2f\n", item.getProductName(), item.getQuantity(), item.getTotalPrice()));
                        }
                        summary.append("\nDonation: RM ").append(String.format("%.2f", order.getDonationAmount())).append("\n");
                        summary.append("Total Amount: RM ").append(String.format("%.2f", order.getTotalAmount())).append("\n");
                        summary.append("\nDelivery Address: ").append(deliveryAddress).append("\n");
                        summary.append("Delivery Type: ").append(deliveryType).append("\n");

                        summaryArea.setText(summary.toString());
                        summaryFrame.add(new JScrollPane(summaryArea), BorderLayout.CENTER);

                        JButton confirmPaymentButton = new JButton("Confirm Payment");
                        confirmPaymentButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                // Save the order to the database
                                saveOrderToDatabase(order, customerName, customerEmail, customerPhone, deliveryAddress, deliveryType, donationAmount);

                                // Update stock based on the quantity of ordered items
                                try (Connection connection = DatabaseManager.getConnection()) {
                                    String updateStockQuery = "UPDATE cakes SET stock = stock - ? WHERE id = ?";
                                    try (PreparedStatement stmt = connection.prepareStatement(updateStockQuery)) {
                                        for (OrderItem item : order.getItems()) {
                                            stmt.setInt(1, item.getQuantity()); // Reduce stock by the ordered quantity
                                            stmt.setInt(2, item.getProductId()); // Identify the product by its ID
                                            stmt.addBatch(); // Add to batch for batch execution
                                        }
                                        stmt.executeBatch(); // Execute all the updates in batch
                                    }
                                } catch (SQLException ex) {
                                    ex.printStackTrace();
                                }

                                // Show success message and close the window
                                JOptionPane.showMessageDialog(summaryFrame, "Thank you for your order!", "Payment Successful", JOptionPane.INFORMATION_MESSAGE);
                                summaryFrame.dispose();
                                frame.dispose();
                            }
                        });

                        summaryFrame.add(confirmPaymentButton, BorderLayout.SOUTH);
                        summaryFrame.add(paymentPanel, BorderLayout.NORTH);
                        summaryFrame.setVisible(true);
                    }
                });

                JPanel headerPanel = new JPanel();
                JLabel brandName = new JLabel("SUC Bakery");
                JLabel pageSubtitle = new JLabel("Welcome to our bakery shop, choose and order the delicious cake from us and enjoy your day! Our baker is Crystel Boey...");
                brandName.setFont(new Font(Font.DIALOG, Font.BOLD, 30));
                pageSubtitle.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 18));

                brandName.setHorizontalAlignment(0);
                pageSubtitle.setHorizontalAlignment(0);

                headerPanel.setPreferredSize(new Dimension(1100, 80));
                headerPanel.setBackground(Color.WHITE);
                headerPanel.setLayout(new GridLayout(2, 1));
                headerPanel.add(brandName);
                headerPanel.add(pageSubtitle);

                frame.add(headerPanel, BorderLayout.NORTH);
                frame.add(mainPanel, BorderLayout.CENTER);
                frame.add(receiptPanel, BorderLayout.EAST);

                frame.setResizable(true);
                frame.setSize(1100, 750);
                frame.setLocationRelativeTo(null);
                frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                frame.setVisible(true);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });
    }
}