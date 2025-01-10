package staff;

import staff.DatabaseManager;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class StaffOrderSummary {
    private JFrame frame;
    private JTable orderTable;
    private DefaultTableModel tableModel;

    public StaffOrderSummary() {
        // Initialize the frame
        frame = new JFrame("Order Summary");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLocationRelativeTo(null); // Center the window

        // Create table model with column names
        tableModel = new DefaultTableModel(new String[] {
                "Order ID", "Customer Name", "Cake", "Total Items", "Total Price", "Donation", "Delivery Address", "Completion Time", "Status"
        }, 0);

        // Create the JTable and apply custom styles
        orderTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Only make the Status column editable for "Preparing" and "Ready" status
                String status = (String) tableModel.getValueAt(row, 8);
                return (column == 8) && ("Preparing".equalsIgnoreCase(status) || "Ready".equalsIgnoreCase(status));
            }
        };

        // Improved font and row height for better readability
        orderTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        orderTable.setRowHeight(40);
        orderTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Add custom colors to rows
        orderTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (row % 2 == 0) {
                    component.setBackground(new Color(245, 245, 245)); // Light gray for even rows
                } else {
                    component.setBackground(Color.WHITE); // White for odd rows
                }
                return component;
            }
        });

        // Add a button renderer/editor for the Status column
        orderTable.getColumn("Status").setCellRenderer(new StatusButtonRenderer());
        orderTable.getColumn("Status").setCellEditor(new StatusButtonEditor(new JCheckBox()));

        // Create a JScrollPane and add the table
        JScrollPane scrollPane = new JScrollPane(orderTable);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Add title and panel for aesthetics
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(70, 130, 180)); // Steel blue background
        JLabel titleLabel = new JLabel("Order Summary");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        // Add buttons for filtering the orders by status with enhanced styles
        JPanel buttonPanel = new JPanel();
        JButton preparingButton = createStyledButton("Preparing", new Color(34, 139, 34)); // Green
        JButton readyButton = createStyledButton("Ready", new Color(255, 140, 0)); // Orange
        JButton completeButton = createStyledButton("Complete", new Color(70, 130, 180)); // Steel blue

        // Add ActionListeners to buttons
        preparingButton.addActionListener(e -> filterOrders("Preparing"));
        readyButton.addActionListener(e -> filterOrders("Ready"));
        completeButton.addActionListener(e -> filterOrders("Complete"));

        buttonPanel.add(preparingButton);
        buttonPanel.add(readyButton);
        buttonPanel.add(completeButton);

        // Add the header panel and button panel to the frame
        frame.add(headerPanel, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Populate the table with orders
        populateOrderTable();

        // Make the window visible
        frame.setVisible(true);
    }

    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(120, 40));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(button.getBackground().darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor);
            }
        });

        return button;
    }

    private void populateOrderTable() {
        List<Object[]> orders = DatabaseManager.getOrders(); // Fetch orders from the database
        for (Object[] order : orders) {
            String status = (String) order[8]; // Assuming the status is the 9th column (index 8)
            Object[] row = new Object[9]; // Adjusted for new columns
            System.arraycopy(order, 0, row, 0, 8); // Copy all data from the order
            row[8] = status;
            tableModel.addRow(row); // Add each order's information to the table
        }

        // Default display of "Preparing" orders
        filterOrders("Preparing");
    }

    private void filterOrders(String status) {
        // Clear the existing rows
        tableModel.setRowCount(0);

        // Fetch and display orders with the selected status
        List<Object[]> orders = DatabaseManager.getOrders(); // Fetch orders from the database
        for (Object[] order : orders) {
            String orderStatus = (String) order[8]; // Assuming status is at index 8
            if (orderStatus.equalsIgnoreCase(status)) {
                Object[] row = new Object[9]; // Adjusted for new columns
                System.arraycopy(order, 0, row, 0, 8); // Copy all data from the order
                row[8] = orderStatus;
                tableModel.addRow(row); // Add each matching order's information to the table
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StaffOrderSummary::new);
    }

    private class StatusButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public StatusButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            String status = (String) table.getValueAt(row, 8); // Get status from the 8th column (Status)
            setText(status == null ? "" : getButtonLabel(status)); // Set button label based on status
            return this;
        }

        private String getButtonLabel(String status) {
            if ("Preparing".equalsIgnoreCase(status)) {
                return "Mark as Ready";
            } else if ("Ready".equalsIgnoreCase(status)) {
                return "Mark as Complete";
            } else if ("Complete".equalsIgnoreCase(status)) {
                return "Completed";
            }
            return "";
        }
    }

    private class StatusButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String label;
        private boolean clicked;
        private int row;

        public StatusButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            String status = (String) table.getValueAt(row, 8); // Get the current status
            label = getButtonLabel(status); // Set button label based on status
            button.setText(label);
            clicked = true;
            this.row = row;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (clicked) {
                String newStatus = (String) tableModel.getValueAt(row, 8); // Get the current status
                String orderID = tableModel.getValueAt(row, 0).toString(); // Get the Order ID

                if ("Preparing".equalsIgnoreCase(newStatus)) {
                    DatabaseManager.updateOrderStatus(Integer.parseInt(orderID), "Ready",null);
                    tableModel.setValueAt("Ready", row, 8);
                    JOptionPane.showMessageDialog(frame, "Order ID " + orderID + " marked as Ready.");
                } else if ("Ready".equalsIgnoreCase(newStatus)) {
                    DatabaseManager.updateOrderStatus(Integer.parseInt(orderID), "Complete", new java.sql.Timestamp(System.currentTimeMillis()));
                    tableModel.setValueAt("Complete", row, 8);
                    JOptionPane.showMessageDialog(frame, "Order ID " + orderID + " marked as Complete.");
                }

                // Reset the flag
                clicked = false;
            }
            return label; // Return the button label after the status change
        }

        private String getButtonLabel(String status) {
            if ("Preparing".equalsIgnoreCase(status)) {
                return "Mark as Ready";
            } else if ("Ready".equalsIgnoreCase(status)) {
                return "Mark as Complete";
            } else if ("Complete".equalsIgnoreCase(status)) {
                return "Completed";
            }
            return "";
        }

        @Override
        public boolean stopCellEditing() {
            clicked = false;
            return super.stopCellEditing();
        }

        @Override
        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }
}
