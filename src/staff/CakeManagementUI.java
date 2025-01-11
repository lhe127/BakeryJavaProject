package staff;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class CakeManagementUI {
    private static final int MAX_CAKES = 6;

    static void createAndShowGUI() {
        JFrame frame = new JFrame("Cake Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        // Custom Panel for Table
        JPanel tablePanel = new JPanel(new BorderLayout());
        DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"Name", "Price", "Stock", "Image Path"}, 0);
        JTable cakeTable = new JTable(tableModel);
        cakeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(cakeTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        frame.add(tablePanel, BorderLayout.CENTER);

        // Customizing Table Appearance
        cakeTable.setFillsViewportHeight(true);
        cakeTable.getTableHeader().setReorderingAllowed(false);
        cakeTable.setGridColor(Color.GRAY);
        cakeTable.setSelectionBackground(new Color(230, 230, 255));

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(240, 240, 240)); // Light gray background

        // Customize buttons with icons and colors
        JButton insertButton = new JButton("Insert Cake");
        insertButton.setBackground(new Color(100, 200, 100));
        insertButton.setForeground(Color.WHITE);
        insertButton.setFocusPainted(false);
        JButton updateButton = new JButton("Update Cake");
        updateButton.setBackground(new Color(100, 150, 255));
        updateButton.setForeground(Color.WHITE);
        updateButton.setFocusPainted(false);
        JButton deleteButton = new JButton("Delete Cake");
        deleteButton.setBackground(new Color(255, 100, 100));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);

        // Add buttons to panel
        buttonPanel.add(insertButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Load cakes into the table
        loadCakes(tableModel);

        // Insert Cake Action
        insertButton.addActionListener(e -> {
            if (DatabaseManager.getCakesCount() < MAX_CAKES) {
                String name = JOptionPane.showInputDialog(frame, "Enter Cake Name:");
                String priceInput = JOptionPane.showInputDialog(frame, "Enter Cake Price:");
                String stockInput = JOptionPane.showInputDialog(frame, "Enter Cake Stock:");

                // Use JFileChooser to select image
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Select Cake Image");
                fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png"));
                int result = fileChooser.showOpenDialog(frame);
                String imagePath = null;
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    imagePath = selectedFile.getAbsolutePath();
                }

                if (imagePath != null && !imagePath.trim().isEmpty()) {
                    try {
                        // Process the file upload
                        File sourceImage = new File(imagePath);
                        String destinationPath = "src/img/" + sourceImage.getName();
                        File destinationFile = new File(destinationPath);

                        // Copy the image to the 'src/img' directory
                        Files.copy(sourceImage.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                        double price = Double.parseDouble(priceInput);
                        int stock = Integer.parseInt(stockInput);

                        if (DatabaseManager.insertCake(name, price, stock, destinationPath)) {
                            JOptionPane.showMessageDialog(frame, "Cake inserted successfully!");
                            loadCakes(tableModel);
                        } else {
                            JOptionPane.showMessageDialog(frame, "Failed to insert cake.");
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame, "Invalid price or stock input.", "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(frame, "Failed to save the image file.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Please select an image for the cake.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Maximum number of cakes reached.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Update Cake Action
        updateButton.addActionListener(e -> {
            int selectedRow = cakeTable.getSelectedRow();
            if (selectedRow != -1) {
                String oldName = (String) tableModel.getValueAt(selectedRow, 0);
                String newName = JOptionPane.showInputDialog(frame, "Enter New Cake Name:", oldName);
                String priceInput = JOptionPane.showInputDialog(frame, "Enter New Cake Price:", tableModel.getValueAt(selectedRow, 1));
                String stockInput = JOptionPane.showInputDialog(frame, "Enter New Cake Stock:", tableModel.getValueAt(selectedRow, 2));

                String existingImagePath = (String) tableModel.getValueAt(selectedRow, 3); // Assuming the image path is stored in column 3
                String newImagePath = existingImagePath; // Default to the existing image path

                // Ask the user if they want to update the image
                int updateImageOption = JOptionPane.showConfirmDialog(frame, "Do you want to update the cake image?", "Update Image", JOptionPane.YES_NO_OPTION);

                if (updateImageOption == JOptionPane.YES_OPTION) {
                    // Use JFileChooser for image update
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setDialogTitle("Select New Cake Image");
                    fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png"));
                    int result = fileChooser.showOpenDialog(frame);

                    if (result == JFileChooser.APPROVE_OPTION) {
                        File selectedFile = fileChooser.getSelectedFile();
                        newImagePath = selectedFile.getAbsolutePath();
                    }
                }

                try {
                    double newPrice = Double.parseDouble(priceInput);
                    int newStock = Integer.parseInt(stockInput);

                    // If a new image was selected, copy it to the 'src/img' directory
                    if (!newImagePath.equals(existingImagePath)) {
                        File sourceImage = new File(newImagePath);
                        String destinationPath = "src/img/" + sourceImage.getName();
                        File destinationFile = new File(destinationPath);

                        Files.copy(sourceImage.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        newImagePath = destinationPath; // Update the image path for the database
                    }

                    if (DatabaseManager.updateCake(oldName, newName, newPrice, newStock, newImagePath)) {
                        JOptionPane.showMessageDialog(frame, "Cake updated successfully!");
                        loadCakes(tableModel);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Failed to update cake.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid price or stock input.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame, "Failed to save the image file.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a cake to update.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Delete Cake Action
        deleteButton.addActionListener(e -> {
            int selectedRow = cakeTable.getSelectedRow();
            if (selectedRow != -1) {
                String name = (String) tableModel.getValueAt(selectedRow, 0);
                String imagePath = (String) tableModel.getValueAt(selectedRow, 3); // Get the image path for deletion
                int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete the cake?", "Confirm", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    // Delete the cake from the database
                    if (DatabaseManager.deleteCake(name)) {
                        // Delete the image file from src/img
                        File imageFile = new File(imagePath);
                        if (imageFile.exists() && imageFile.isFile()) {
                            imageFile.delete();
                        }

                        JOptionPane.showMessageDialog(frame, "Cake deleted successfully!");
                        loadCakes(tableModel);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Failed to delete cake.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a cake to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        frame.setLocationRelativeTo(null); // Center the frame on the screen
        frame.setVisible(true);
    }

    private static void loadCakes(DefaultTableModel tableModel) {
        tableModel.setRowCount(0); // Clear existing rows
        List<Object[]> cakes = DatabaseManager.getCakes();
        for (Object[] cake : cakes) {
            tableModel.addRow(cake); // Directly add the cake data to the table
        }
    }
}
