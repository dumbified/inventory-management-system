package components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import database.ProductDAO;
import models.Product;

// class for exporting the report to markdown file

public class InvReport extends JFrame {
    private JLabel lowestStock, mostExpensive, highestStock, mostTotalValue, totalStockValue, timeLabel, dateLabel, summaryLabel;
    private JLabel averagePrice, totalProducts, outOfStockCount, cheapestProduct, mostPopularCategory, highestValueCategory;
    private Timer timer;

    public InvReport() {
        setTitle("Inventory Report");
        setSize(600, 900);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // header panel
        JPanel headerPanel = new JPanel(new GridLayout(3, 1, 5, 5)); // Grid layout with spacing

        dateLabel = new JLabel("Date: " + getCurrentDate(), SwingConstants.LEFT);
        timeLabel = new JLabel("Time: " + getCurrentTime(), SwingConstants.LEFT);
        summaryLabel = new JLabel("Summary:", SwingConstants.LEFT);


        Font font = new Font("Monospaced", Font.PLAIN, 16);
        dateLabel.setFont(font);
        timeLabel.setFont(font);
        summaryLabel.setFont(font);

        // add labels to header panel
        headerPanel.add(dateLabel);
        headerPanel.add(timeLabel);
        headerPanel.add(summaryLabel);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        add(mainPanel);
        startClock(); // Start updating the time dynamically
        setVisible(true);

        // Statistics Panel
        JPanel statsPanel = new JPanel(new GridLayout(11, 1, 0, 3)); // 11 rows, 1 column, 5px vertical gap
        statsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Add padding
        
        lowestStock = new JLabel();
        mostExpensive = new JLabel();
        highestStock = new JLabel();
        mostTotalValue = new JLabel();
        totalStockValue = new JLabel();
        averagePrice = new JLabel();
        totalProducts = new JLabel();
        outOfStockCount = new JLabel();
        cheapestProduct = new JLabel();
        mostPopularCategory = new JLabel();
        highestValueCategory = new JLabel();
        
        Font statsfont = new Font("Monospaced", Font.PLAIN, 14);
        lowestStock.setFont(statsfont);
        mostExpensive.setFont(statsfont);
        highestStock.setFont(statsfont);
        mostTotalValue.setFont(statsfont);
        totalStockValue.setFont(statsfont);
        averagePrice.setFont(statsfont);
        totalProducts.setFont(statsfont);
        outOfStockCount.setFont(statsfont);
        cheapestProduct.setFont(statsfont);
        mostPopularCategory.setFont(statsfont);
        highestValueCategory.setFont(statsfont);
        
        statsPanel.add(lowestStock);
        statsPanel.add(mostExpensive);
        statsPanel.add(highestStock);
        statsPanel.add(mostTotalValue);
        statsPanel.add(totalStockValue);
        statsPanel.add(averagePrice);
        statsPanel.add(totalProducts);
        statsPanel.add(outOfStockCount);
        statsPanel.add(cheapestProduct);
        statsPanel.add(mostPopularCategory);
        statsPanel.add(highestValueCategory);

        mainPanel.add(statsPanel, BorderLayout.CENTER);

        // Export Button
        JButton exportButton = new JButton("Export");
        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportToMarkdown();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(exportButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        updateProductStatistics();
        
        setVisible(true);
    }

    private void updateProductStatistics() {
        ProductDAO productDAO = new ProductDAO();
    
        Product lowStockProduct = productDAO.getLowestStockProduct();
        Product expensiveProduct = productDAO.getMostExpensiveProduct();
        Product highStockProduct = productDAO.getHighestQuantityProduct();
        Product highestValueProduct = productDAO.getHighestTotalValueProduct();
        Product cheapProduct = productDAO.getCheapestProduct();
    
        // Update labels with product statistics
        lowestStock.setText("Lowest stock: " + (lowStockProduct != null ? lowStockProduct.getName() + " (" + lowStockProduct.getQuantity() + ")" : "N/A"));
        mostExpensive.setText("Most expensive: " + (expensiveProduct != null ? expensiveProduct.getName() + " (RM" + expensiveProduct.getPrice() + ")" : "N/A"));
        highestStock.setText("Highest stock: " + (highStockProduct != null ? highStockProduct.getName() + " (" + highStockProduct.getQuantity() + ")" : "N/A"));
        mostTotalValue.setText("Highest total value: " + (highestValueProduct != null ? highestValueProduct.getName() + " (RM" + (highestValueProduct.getPrice() * highestValueProduct.getQuantity()) + ")" : "N/A"));
        totalStockValue.setText(String.format("Total stock value: RM%.2f", productDAO.getTotalStockValue()));
        averagePrice.setText(String.format("Average price: RM%.2f", productDAO.getAverageProductPrice()));
        totalProducts.setText("Total products: " + productDAO.getTotalProductsCount());
        outOfStockCount.setText("Out of stock products: " + productDAO.getOutOfStockCount());
        cheapestProduct.setText("Cheapest product: " + (cheapProduct != null ? cheapProduct.getName() + " (RM" + cheapProduct.getPrice() + ")" : "N/A"));
        mostPopularCategory.setText("Most popular category: " + productDAO.getCategoryWithMostProducts());
        highestValueCategory.setText("Highest value category: " + productDAO.getHighestStockValueCategory());
    }

    private String getCurrentDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(new Date());
    }

    private String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        return formatter.format(new Date());
    }

    private void exportToMarkdown() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("inventory_report.md"))) {
            writer.write("# Inventory Report\n\n");
            writer.write(dateLabel.getText() + "\n\n");
            writer.write(timeLabel.getText() + "\n\n");
            writer.write(lowestStock.getText() + "\n\n");
            writer.write(mostExpensive.getText() + "\n\n");
            writer.write(highestStock.getText() + "\n\n");
            writer.write(mostTotalValue.getText() + "\n\n");
            writer.write(totalStockValue.getText() + "\n\n");
            writer.write(averagePrice.getText() + "\n\n");
            writer.write(totalProducts.getText() + "\n\n");
            writer.write(outOfStockCount.getText() + "\n\n");
            writer.write(cheapestProduct.getText() + "\n\n");
            writer.write(mostPopularCategory.getText() + "\n\n");
            writer.write(highestValueCategory.getText() + "\n\n");
            
            JOptionPane.showMessageDialog(this, "Export successful! Please check your file.", "Export Status", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error exporting file.", "Export Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void startClock() {
        timer = new Timer(1000, new ActionListener() { // Update every second
            @Override
            public void actionPerformed(ActionEvent e) {
                timeLabel.setText("Time: " + getCurrentTime());
            }
        });
        timer.start();
    }

    public static void main(String[] args) {
        new InvReport();
    }
}
