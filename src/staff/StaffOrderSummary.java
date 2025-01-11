package staff;

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

    // Constructor no longer creates JFrame
    public StaffOrderSummary() {
        // Initialize components but do not create the JFrame yet
        tableModel = new DefaultTableModel(new String[] {
                "Order ID", "Customer Name", "Cake", "Total Items", "Total Price", "Donation", "Delivery Address", "Completion Time", "Status"
        }, 0);
    }

    public void createAndShowGUI() {
        // Initialize the frame here (it won't be initialized in constructor)
        frame = new JFrame("Order Summary");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLocationRelativeTo(null); // Center the window

        // Create the JTable and apply custom styles
        orderTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Only make the Status column editable for "Preparing" and "Ready" status
                String status = (String) tableModel.getValueAt(row, 8);
                return (column == 8) && ("Preparing".equalsIgnoreCase(status) || "Ready".equalsIgnoreCase(status));
            }
        };

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

        JScrollPane scrollPane = new JScrollPane(orderTable);
        frame.add(scrollPane, BorderLayout.CENTER);

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(70, 130, 180));
        JLabel titleLabel = new JLabel("Order Summary");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        JPanel buttonPanel = new JPanel();
        JButton preparingButton = createStyledButton("Preparing", new Color(34, 139, 34));
        JButton readyButton = createStyledButton("Ready", new Color(255, 140, 0));
        JButton completeButton = createStyledButton("Complete", new Color(70, 130, 180));

        preparingButton.addActionListener(e -> filterOrders("Preparing"));
        readyButton.addActionListener(e -> filterOrders("Ready"));
        completeButton.addActionListener(e -> filterOrders("Complete"));

        buttonPanel.add(preparingButton);
        buttonPanel.add(readyButton);
        buttonPanel.add(completeButton);

        frame.add(headerPanel, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        populateOrderTable();
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
        List<Object[]> orders = DatabaseManager.getOrders();
        for (Object[] order : orders) {
            String status = (String) order[8];
            Object[] row = new Object[9];
            System.arraycopy(order, 0, row, 0, 8);
            row[8] = status;
            tableModel.addRow(row);
        }

        filterOrders("Preparing");
    }

    private void filterOrders(String status) {
        tableModel.setRowCount(0);
        List<Object[]> orders = DatabaseManager.getOrders();
        for (Object[] order : orders) {
            String orderStatus = (String) order[8];
            if (orderStatus.equalsIgnoreCase(status)) {
                Object[] row = new Object[9];
                System.arraycopy(order, 0, row, 0, 8);
                row[8] = orderStatus;
                tableModel.addRow(row);
            }
        }
    }

    private class StatusButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public StatusButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            String status = (String) table.getValueAt(row, 8);
            setText(status == null ? "" : getButtonLabel(status));
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
            String status = (String) table.getValueAt(row, 8);
            label = getButtonLabel(status);
            button.setText(label);
            clicked = true;
            this.row = row;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (clicked) {
                String newStatus = (String) tableModel.getValueAt(row, 8);
                String orderID = tableModel.getValueAt(row, 0).toString();

                if ("Preparing".equalsIgnoreCase(newStatus)) {
                    DatabaseManager.updateOrderStatus(Integer.parseInt(orderID), "Ready", null);
                    tableModel.setValueAt("Ready", row, 8);
                    JOptionPane.showMessageDialog(frame, "Order ID " + orderID + " marked as Ready.");
                } else if ("Ready".equalsIgnoreCase(newStatus)) {
                    DatabaseManager.updateOrderStatus(Integer.parseInt(orderID), "Complete", new java.sql.Timestamp(System.currentTimeMillis()));
                    tableModel.setValueAt("Complete", row, 8);
                    JOptionPane.showMessageDialog(frame, "Order ID " + orderID + " marked as Complete.");
                }
                clicked = false;
            }
            return label;
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
