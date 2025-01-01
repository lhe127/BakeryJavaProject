package bakery;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.awt.event.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class Bakery {
    ArrayList<JLabel> counterLabels = new ArrayList<>();
    JLabel counterLabel;

    public Bakery(){
        JFrame homeFrame = new JFrame("SUC Bakery");
        homeFrame.setLayout(new BorderLayout());

        JPanel welcomePanel = new JPanel();
        welcomePanel.setPreferredSize(new Dimension(1100,150));
        welcomePanel.setBackground(new Color(255, 246, 243));

        JLabel label1 = new JLabel ("WELCOME TO SUC BAKERY");
        welcomePanel.add(label1);
        label1.setFont(new Font("Dialog", Font.BOLD, 50));

        JPanel logoPanel = new JPanel();
        logoPanel.setPreferredSize(new Dimension(1100,250));

        JLabel image1 = new JLabel (new ImageIcon("src/img/logo.png"));
        logoPanel.add(image1);
        logoPanel.setBackground(new Color(255, 246, 243));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setPreferredSize(new Dimension(150, 150));
        buttonPanel.setBackground(new Color(255, 246, 243));

        JButton menuButton = new JButton("ORDER NOW");
        buttonPanel.add(menuButton);

        menuButton.setPreferredSize(new Dimension(200, 100));
        menuButton.setFont(new Font("SansSerif", Font.BOLD, 20));
        menuButton.setForeground(Color.red);

        homeFrame.add(welcomePanel, BorderLayout.NORTH);
        homeFrame.add(logoPanel, BorderLayout.CENTER);
        homeFrame.add(buttonPanel, BorderLayout.SOUTH);

        homeFrame.setSize(1100, 750);
        homeFrame.setResizable(true);
        homeFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        homeFrame.setVisible(true);
        homeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        menuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                homeFrame.dispose();

                JFrame frame = new JFrame("SUC Bakery");
                BakeryShop bk = new BakeryShop();

                JPanel mainPanel = new JPanel();
                JPanel receiptPanel = new JPanel(new BorderLayout());
                JPanel receiptContentPanel = new JPanel(new BorderLayout());
                JPanel checkoutPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

                receiptPanel.setBorder(new LineBorder(Color.BLACK, 2));
                receiptPanel.setBackground(new Color(255, 246, 243));
                receiptContentPanel.setBackground(new Color(255, 246, 243));
                checkoutPanel.setBackground(new Color(255, 246, 243));
                checkoutPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

                mainPanel.setPreferredSize(new Dimension(700, 750));
                mainPanel.setLayout(new GridLayout(3,3, 45, 45));

                JButton checkoutButton = new JButton("Checkout");
                checkoutButton.setPreferredSize(new Dimension(150, 70));
                checkoutButton.setFont(new Font("SansSerif", Font.BOLD, 20));
                checkoutButton.setBackground(new Color(42,178,123,255));
                checkoutButton.setBorderPainted(false);
                checkoutButton.setOpaque(true);
                checkoutButton.setForeground(new Color(245,248,250,255));
                checkoutButton.setFocusPainted(false);

                checkoutPanel.add(checkoutButton);
                receiptPanel.add(receiptContentPanel, BorderLayout.CENTER);
                receiptPanel.add(checkoutPanel, BorderLayout.SOUTH);

                for (Products product : bk.products) {
                    JPanel productPanel = new JPanel();
                    productPanel.setBackground(new Color(255, 246, 243));
                    productPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));

                    JLabel imageLabel = new JLabel();
                    ImageIcon imageIcon = new ImageIcon(product.getImage());
                    Image image = imageIcon.getImage();
                    Image scaledImg = image.getScaledInstance(150, 100, java.awt.Image.SCALE_SMOOTH);
                    imageIcon = new ImageIcon(scaledImg);
                    imageLabel.setIcon(imageIcon);

                    JLabel productNameLabel = new JLabel(product.getProductName());
                    productNameLabel.setPreferredSize(new Dimension(150, 40));  // Set the width to control truncation
                    productNameLabel.setFont(new Font("Arial", Font.BOLD, 18));
                    productNameLabel.setHorizontalAlignment(SwingConstants.CENTER);

                    // Truncate the text with ellipsis if the text is too long
                    String displayText = product.getProductName().length() > 20 ? product.getProductName().substring(0, 17) + "..." : product.getProductName();
                    productNameLabel.setText(displayText);

                    // Set tooltip to show the full product name
                    productNameLabel.setToolTipText(product.getProductName());
                    JLabel priceLabel = new JLabel("RM " + Double.toString(product.getPrice()));

                    // Add a stock label
                    JLabel stockLabel = new JLabel("Stock: " + product.getStock());
                    stockLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                    stockLabel.setHorizontalAlignment(SwingConstants.CENTER);

                    JPanel detailPanel = new JPanel();
                    detailPanel.add(productNameLabel);
                    detailPanel.add(priceLabel);
                    detailPanel.add(stockLabel);  // Add stock label below the price
                    detailPanel.setLayout(new GridLayout(3, 1));  // Ensure stock label fits without breaking layout
                    detailPanel.setBackground(new Color(255, 246, 243));

                    JPanel counterPanel = new JPanel();

                    JButton counterAddBtn = new JButton("+");
                    JButton counterSubtractBtn = new JButton("-");

                    counterLabel = new JLabel("0");

                    counterLabels.add(counterLabel);

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

                    counterPanel.setBackground(new Color(245,248,250,255));
                    counterPanel.setPreferredSize(new Dimension(150, 30));
                    counterPanel.setLayout(new GridLayout(1, 3, 5, 5));
                    counterPanel.add(counterSubtractBtn);
                    counterPanel.add(counterLabel);
                    counterPanel.add(counterAddBtn);

                    productPanel.add(imageLabel);
                    productPanel.add(detailPanel);
                    productPanel.add(counterPanel);

                    counterLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    counterLabel.setFont(new Font("Arial", Font.BOLD, 20));

                    productNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    productNameLabel.setFont(new Font("Arial", Font.BOLD, 18));

                    priceLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    priceLabel.setFont(new Font("Arial", Font.BOLD, 22));

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

                    mainPanel.add(productPanel);
                }

                JButton confirmButton = new JButton("Confirm");
                confirmButton.setPreferredSize(new Dimension(150, 70));
                confirmButton.setFont(new Font("SansSerif", Font.BOLD, 20));
                confirmButton.setBackground(new Color(42,178,123,255));
                confirmButton.setBorderPainted(false);
                confirmButton.setOpaque(true);
                confirmButton.setForeground(new Color(245,248,250,255));
                confirmButton.setFocusPainted(false);

                JButton clearButton = new JButton("Clear Order");
                clearButton.setPreferredSize(new Dimension(150,70));
                clearButton.setFont(new Font("SansSerif", Font.BOLD, 20));
                clearButton.setBackground(new Color(42,178,123,255));
                clearButton.setBorderPainted(false);
                clearButton.setOpaque(true);
                clearButton.setForeground(new Color(245,248,250,255));
                clearButton.setFocusPainted(false);

                JPanel btnPanel = new JPanel();
                btnPanel.setPreferredSize(new Dimension(150,60));
                btnPanel.add(confirmButton);
                btnPanel.add(clearButton);

                btnPanel.setBorder(BorderFactory.createEmptyBorder(100, 0, 0, 0));
                btnPanel.setBackground(Color.WHITE);
                mainPanel.setBackground(Color.WHITE);

                mainPanel.add(Box.createVerticalStrut(0));
                mainPanel.add(btnPanel);

                checkoutButton.setVisible(false);

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

// Inside clearButton's ActionListener, add:
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

                        // 变量存储捐赠金额
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

                        // 为所有按钮绑定事件监听器
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

                    private void finalizeOrder(Order order, double donationAmount) {
                        order.setDonationAmount(donationAmount);

                        for (Products product : bk.products) {
                            int quantity = product.getCounter();
                            if (quantity > 0) {
                                order.addItem(product, quantity);
                            }
                        }

                        // 显示订单总结
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

                        summaryArea.setText(summary.toString());
                        summaryFrame.add(new JScrollPane(summaryArea), BorderLayout.CENTER);

                        JButton confirmPaymentButton = new JButton("Confirm Payment");
                        confirmPaymentButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                JOptionPane.showMessageDialog(summaryFrame, "Thank you for your order! Please pay the amount at the counter.", "Payment Successful", JOptionPane.INFORMATION_MESSAGE);
                                summaryFrame.dispose();
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