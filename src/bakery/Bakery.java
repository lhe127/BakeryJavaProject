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

                //Handle button click
                JFrame frame = new JFrame("SUC Bakery");
                BakeryShop bk = new BakeryShop();

                JPanel mainPanel = new JPanel();
                JPanel receiptPanel = new JPanel(new BorderLayout());
                receiptPanel.setBorder(new LineBorder(Color.BLACK, 2));

                mainPanel.setPreferredSize(new Dimension(700, 750));
                mainPanel.setLayout(new GridLayout(3,3, 45, 45));

                // Receipt Panel
                receiptPanel.setBackground(new Color(255, 246, 243));

                for(Products product : bk.products){
                    // Product Panel
                    JPanel productPanel = new JPanel();
                    productPanel.setBackground(new Color(255, 246, 243));
                    productPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));

                    // Image Define Start
                    JLabel imageLabel = new JLabel();
                    ImageIcon imageIcon = new ImageIcon(product.getImage());
                    Image image = imageIcon.getImage();
                    Image scaledImg = image.getScaledInstance(150, 100,  java.awt.Image.SCALE_SMOOTH);
                    imageIcon = new ImageIcon(scaledImg);
                    imageLabel.setIcon(imageIcon);
                    // Image Define End

                    // Detail Panel Section
                    JLabel productNameLabel = new JLabel(product.getProductName());
                    JLabel priceLabel = new JLabel("RM " + Double.toString(product.getPrice()));

                    JPanel detailPanel = new JPanel();
                    detailPanel.add(productNameLabel);
                    detailPanel.add(priceLabel);
                    detailPanel.setLayout(new GridLayout(2, 1));
                    detailPanel.setBackground(new Color(255, 246, 243));

                    // Counter Panel
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
                            product.setCounter(++currentCount);
                            int index = bk.products.indexOf(product);
                            counterLabels.get(index).setText(Integer.toString(product.getCounter()));
                        }
                    });

                    counterSubtractBtn.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if(product.getCounter() > 0){
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

                confirmButton.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        JLabel receiptLabel = new JLabel("Receipt");
                        receiptLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
                        receiptLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));

                        double total = 0;

                        String[] columnNames = {"Product", "Quantity", "Price"};
                        DefaultTableModel model = new DefaultTableModel(columnNames,0);
                        JTable table = new JTable(model);

                        for (Products product : bk.products) {
                            int quantity = product.getCounter();
                            if (quantity > 0) {
                                double price = quantity * product.getPrice();
                                total+=price;

                                String stringPrice = String.format("RM %.2f", price);
                                Object[] data = {product.getProductName(), quantity, stringPrice};
                                model.addRow(data);
                            }
                        }

                        String stringTotal = String.format("RM %.2f", total);
                        Object [] totalData = {"TOTAL:", "", stringTotal};
                        model.addRow(totalData);

                        table.setRowHeight(30);
                        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
                        table.setBackground(new Color(255, 246, 243));

                        JTableHeader header = table.getTableHeader();
                        header.setFont(new Font("SansSerif", Font.BOLD, 16));

                        receiptPanel.removeAll();
                        receiptPanel.add(receiptLabel,BorderLayout.NORTH);
                        receiptPanel.add(new JScrollPane(table));
                        receiptPanel.revalidate();
                        receiptPanel.repaint();
                    }
                });

                clearButton.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        for (int i = 0; i < bk.products.size(); i++) {
                            bk.products.get(i).setCounter(0);
                            counterLabels.get(i).setText("0");
                        }
                        receiptPanel.removeAll();
                        receiptPanel.revalidate();
                        receiptPanel.repaint();
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
