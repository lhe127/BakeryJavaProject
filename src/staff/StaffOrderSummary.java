package staff;

import bakery.Order;
import bakery.OrderItem;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class StaffOrderSummary {

    private Order order;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String deliveryAddress;
    private String deliveryType;

    public StaffOrderSummary(Order order, String customerName, String customerEmail, String customerPhone,
                             String deliveryAddress, String deliveryType) {
        this.order = order;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;
        this.deliveryAddress = deliveryAddress;
        this.deliveryType = deliveryType;
    }

    // Method to display the staff order summary
    public void display() {
        JFrame staffFrame = new JFrame("Order Summary - Staff");
        staffFrame.setLayout(new BorderLayout());

        // Order Summary Panel for Staff
        JPanel staffPanel = new JPanel();
        staffPanel.setLayout(new GridLayout(order.getItems().size() + 6, 2, 10, 10));

        // Display customer info
        staffPanel.add(new JLabel("Customer Name:"));
        staffPanel.add(new JLabel(customerName));
        staffPanel.add(new JLabel("Email:"));
        staffPanel.add(new JLabel(customerEmail));
        staffPanel.add(new JLabel("Phone:"));
        staffPanel.add(new JLabel(customerPhone));
        staffPanel.add(new JLabel("Delivery Address:"));
        staffPanel.add(new JLabel(deliveryAddress));
        staffPanel.add(new JLabel("Delivery Type:"));
        staffPanel.add(new JLabel(deliveryType));

        // Display items ordered
        staffPanel.add(new JLabel("Items Ordered:"));
        for (OrderItem item : order.getItems()) {
            staffPanel.add(new JLabel(item.getProductName() + " x " + item.getQuantity()));
        }

        // Add donation info
        staffPanel.add(new JLabel("Donation Amount:"));
        staffPanel.add(new JLabel(String.format("$%.2f", order.getDonationAmount())));

        // Add total price info
        staffPanel.add(new JLabel("Total Amount:"));
        staffPanel.add(new JLabel(String.format("$%.2f", order.getTotalAmount())));

        // Add a button to close the order summary (for staff)
        JPanel buttonPanel = new JPanel();
        JButton closeButton = new JButton("Close");
        closeButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        closeButton.setBackground(new Color(42, 178, 123));
        closeButton.setForeground(Color.WHITE);
        buttonPanel.add(closeButton);

        staffFrame.add(staffPanel, BorderLayout.CENTER);
        staffFrame.add(buttonPanel, BorderLayout.SOUTH);

        staffFrame.setSize(400, 400);
        staffFrame.setLocationRelativeTo(null);
        staffFrame.setVisible(true);

        closeButton.addActionListener(e -> staffFrame.dispose());  // Close the staff view frame
    }
}

