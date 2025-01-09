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
        tableModel = new DefaultTableModel(new String[]{"Order ID", "Customer Name", "Cake", "Total Items", "Total Price", "Status"}, 0);

        // Populate the table with orders
        populateOrderTable();

        // Create the JTable and apply custom styles
        orderTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Only make the Status column editable
            }
        };

        orderTable.setFont(new Font("Arial", Font.PLAIN, 14)); // Set a clean font
        orderTable.setRowHeight(60); // Increase row height for better readability

        // Add custom colors to rows
        orderTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (row % 2 == 0) {
                    component.setBackground(new Color(240, 240, 240)); // Light gray for even rows
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
        headerPanel.setBackground(new Color(100, 149, 237)); // Cornflower blue background
        JLabel titleLabel = new JLabel("Order Summary");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        // Add the header panel to the top of the frame
        frame.add(headerPanel, BorderLayout.NORTH);

        // Make the window visible
        frame.setVisible(true);
    }

    private void populateOrderTable() {
        List<Object[]> orders = DatabaseManager.getOrders(); // Fetch orders from the database
        for (Object[] order : orders) {
            String status = (String) order[5]; // Assuming the status is the 6th column (index 5)
            if ("Preparing".equalsIgnoreCase(status)) { // Check if the status is "Preparing"
                Object[] row = new Object[6];
                System.arraycopy(order, 0, row, 0, 5);
                row[5] = "Mark as Ready"; // Default button text for the status column
                tableModel.addRow(row); // Add each order's information to the table
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StaffOrderSummary::new);
    }

    // Button Renderer for the Status column
    private class StatusButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public StatusButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value == null ? "" : value.toString());
            return this;
        }
    }

    // Button Editor for the Status column
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
            label = value == null ? "" : value.toString();
            button.setText(label);
            clicked = true;
            this.row = row;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (clicked) {
                // Update status to "Ready" in the database
                int orderId = (int) tableModel.getValueAt(row, 0);
                DatabaseManager.updateOrderStatus(orderId, "Ready");

                // Remove the order from the table
                tableModel.removeRow(row);

                JOptionPane.showMessageDialog(frame, "Order ID " + orderId + " marked as Ready.");
            }
            clicked = false;
            return label;
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
