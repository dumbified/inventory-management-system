package components;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import database.ProductDAO;
import models.Product;

public class Dashboard extends JPanel {
    private JTable lowStockTable;
    private DefaultTableModel lowStockTableModel;

    public Dashboard() {
        setLayout(null); // Absolute positioning to adjust sizes manually
    
        createUserInfoPanel();
        createLowStockPanel();
        createQuickActionsPanel();
        createOverviewPanel();
        createRefreshBtn();
    
        // Load data when the window opens
        loadLowStockData();
    }

    private void createUserInfoPanel() {
        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.Y_AXIS));
        userInfoPanel.setBorder(BorderFactory.createTitledBorder("User Info"));
        userInfoPanel.setBounds(20, 20, 200, 250);
    
        JLabel welcomeLabel = new JLabel("Welcome: Admin");
        JLabel roleLabel = new JLabel("Role permissions:");
        JLabel permissionLabel = new JLabel("Add, Delete, Update, View");
        JButton logoutButton = new JButton("Log out");
        logoutButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to log out?", 
                "Logout Confirmation", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                SwingUtilities.getWindowAncestor(this).dispose(); // Close the dashboard window
                new Login(); // Show login screen again
            }
        });
    
        userInfoPanel.add(welcomeLabel);
        userInfoPanel.add(Box.createVerticalStrut(10)); 
        userInfoPanel.add(roleLabel);
        userInfoPanel.add(Box.createVerticalStrut(2)); 
        userInfoPanel.add(permissionLabel);
        userInfoPanel.add(Box.createVerticalStrut(10));
        userInfoPanel.add(logoutButton);

        add(userInfoPanel);
    }

    private void createLowStockPanel() {
        JPanel lowStockPanel = new JPanel();
        lowStockPanel.setLayout(new BorderLayout());
        lowStockPanel.setBorder(BorderFactory.createTitledBorder("Low Stock Alert"));
        lowStockPanel.setBounds(250, 20, 300, 250);
    
        JLabel lowStockLabel = new JLabel("⚠︎ Products requiring restock!", JLabel.CENTER);
        lowStockLabel.setForeground(Color.RED);
    
        String[] stockColumns = { "Product Name", "Stock", "Category" };
        lowStockTableModel = new DefaultTableModel(stockColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Disable editing for all cells
            }
        };
    
        lowStockTable = new JTable(lowStockTableModel);
        lowStockTable.setRowHeight(20); // Set row height
    
        // Adjust column widths
        lowStockTable.getColumnModel().getColumn(0).setPreferredWidth(200); // Product (Wider)
        lowStockTable.getColumnModel().getColumn(1).setPreferredWidth(100); // Stock (Medium)
        lowStockTable.getColumnModel().getColumn(2).setPreferredWidth(150); // Category (Medium)
        lowStockTable.getTableHeader().setReorderingAllowed(false); // Disable column reordering
    
        JPanel restockPanel = new JPanel();
    
        lowStockPanel.add(lowStockLabel, BorderLayout.NORTH);
        lowStockPanel.add(new JScrollPane(lowStockTable), BorderLayout.CENTER);
        lowStockPanel.add(restockPanel, BorderLayout.SOUTH);

        add(lowStockPanel);
    }

    private void createQuickActionsPanel() {
        JPanel quickActionsPanel = new JPanel();
        quickActionsPanel.setLayout(null); // Use null layout to manually set button sizes
        quickActionsPanel.setBorder(BorderFactory.createTitledBorder("Quick Actions"));
        quickActionsPanel.setBounds(580, 20, 180, 250);
    
        // Button dimensions
        int buttonWidth = 160;
        int buttonHeight = 30;
        int buttonX = 10; // Fixed x position for alignment
        int buttonY = 20; // Initial y position
    
        JButton addButton = new JButton("Add");
        addButton.setBounds(buttonX, buttonY, buttonWidth, buttonHeight);
        addButton.addActionListener(e -> {
            AddProduct addProductDialog = new AddProduct();
            addProductDialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                    loadLowStockData(); // Refresh low stock table after adding
                }
            });
        });
    
        //easter egg >:)
        JButton deleteButton = new JButton("Delete");
        deleteButton.setBounds(buttonX, buttonY + 40, buttonWidth, buttonHeight);
        deleteButton.addActionListener(e -> {
            JDialog loadingDialog = new JDialog();
            loadingDialog.setTitle("Delete");
            loadingDialog.setSize(200, 100);
            loadingDialog.setLayout(new BorderLayout());
            
            JLabel loadingLabel = new JLabel("Deleting... 🚮", JLabel.CENTER);
            loadingDialog.add(loadingLabel, BorderLayout.CENTER);
            
            loadingDialog.setLocationRelativeTo(null);
            loadingDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
            loadingDialog.setModal(true);
            
            new Timer(2000, event -> { 
                loadingLabel.setText("Your brain is deleted. 💀");
                
                // Optional: Add a "Close" button after the message updates
                JButton closeButton = new JButton("Close");
                closeButton.addActionListener(closeEvent -> loadingDialog.dispose());
                loadingDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                
                JPanel panel = new JPanel();
                panel.add(closeButton);
                loadingDialog.add(panel, BorderLayout.SOUTH);
                
                loadingDialog.validate();
            }).start();
            
            loadingDialog.setVisible(true);
        });

        // Restock button
        JButton restockButton = new JButton("Restock");
        restockButton.setBounds(buttonX, buttonY + 80, buttonWidth, buttonHeight);
        restockButton.addActionListener(e -> restockProduct());
    
        // Generate report button
        JButton generateReportButton = new JButton("Generate Report 🧾");
        generateReportButton.setBounds(buttonX, buttonY + 120, buttonWidth, buttonHeight);
        generateReportButton.addActionListener(e -> new InvReport());
    
        quickActionsPanel.add(addButton);
        quickActionsPanel.add(deleteButton);
        quickActionsPanel.add(restockButton);
        quickActionsPanel.add(generateReportButton);

        add(quickActionsPanel);
    }

    private void createOverviewPanel() {
        JPanel productsOverviewPanel = new JPanel(new GridLayout(5, 1));
        productsOverviewPanel.setBorder(BorderFactory.createTitledBorder("Inventory Statistics Overview"));
        productsOverviewPanel.setBounds(20, 280, 740, 250);
        
        // Labels for product statistics  
        JLabel totalStockValueLabel = new JLabel(" ");
        JLabel mostTotalValueLabel = new JLabel(" ");
        JLabel highestStockLabel = new JLabel(" ");
        JLabel lowestStockLabel = new JLabel(" ");
        JLabel mostExpensiveLabel = new JLabel(" ");

        //set font
        Font statsfont = new Font("Monospaced", Font.PLAIN, 14);
        totalStockValueLabel.setFont(statsfont);
        mostTotalValueLabel.setFont(statsfont);
        highestStockLabel.setFont(statsfont);
        lowestStockLabel.setFont(statsfont);
        mostExpensiveLabel.setFont(statsfont);

        productsOverviewPanel.add(totalStockValueLabel);
        productsOverviewPanel.add(mostTotalValueLabel);
        productsOverviewPanel.add(highestStockLabel);
        productsOverviewPanel.add(lowestStockLabel);
        productsOverviewPanel.add(mostExpensiveLabel);

        updateProductStatistics(totalStockValueLabel, mostExpensiveLabel, highestStockLabel, mostTotalValueLabel, lowestStockLabel);

        add(productsOverviewPanel);
    }

    private void createRefreshBtn() {
        JButton refreshButton = new JButton(" Refresh");
        refreshButton.setBounds(680, 530, 100, 30);
        refreshButton.addActionListener(e -> {
            lowStockTableModel.setRowCount(0); // Clear previous data before reloading
            loadLowStockData();

            // Refresh product statistics
            createOverviewPanel();
        });
    
        add(refreshButton);
    }

    // update the product statistics labels
    private void updateProductStatistics(JLabel lowestStock, JLabel mostExpensive, JLabel highestStock, JLabel mostTotalValue, JLabel totalStockValue) {
        ProductDAO productDAO = new ProductDAO();
    
        Product lowStockProduct = productDAO.getLowestStockProduct();
        Product expensiveProduct = productDAO.getMostExpensiveProduct();
        Product highStockProduct = productDAO.getHighestQuantityProduct();
        Product highestValueProduct = productDAO.getHighestTotalValueProduct();
    
        lowestStock.setText("<html><b>Lowest stock:</b> " + (lowStockProduct != null ? lowStockProduct.getName() + " (" + lowStockProduct.getQuantity() + ")" : "N/A") + "</html>");
        mostExpensive.setText("<html><b>Most expensive product:</b> " + (expensiveProduct != null ? expensiveProduct.getName() + " (RM" + expensiveProduct.getPrice() + ")" : "N/A") + "</html>");
        highestStock.setText("<html><b>Highest stock:</b> " + (highStockProduct != null ? highStockProduct.getName() + " (" + highStockProduct.getQuantity() + ")" : "N/A") + "</html>");
        mostTotalValue.setText("<html><b>Most total value product:</b> " + (highestValueProduct != null ? highestValueProduct.getName() + " (RM" + highestValueProduct.getPrice() * highestValueProduct.getQuantity() + ")" : "N/A") + "</html>");
        totalStockValue.setText(String.format("<html><b>Total stock value:</b> RM%.2f</html>", productDAO.getTotalStockValue()));
    }

    // load low stock data into the table
    private void loadLowStockData() {
        lowStockTableModel.setRowCount(0); // Clear table before reloading data
        ProductDAO productDAO = new ProductDAO();
        List<Product> lowStockProducts = productDAO.getLowStockProducts();
    
        for (int i = 0; i < lowStockProducts.size(); i++) {
            Product product = lowStockProducts.get(i);
            lowStockTableModel.addRow(new Object[]{
                product.getName(),
                product.getQuantity(),
                product.getCategory()
            });
        }
    }

    // restock a product by increasing the amount input
    private void restockProduct() {
        int selectedRow = lowStockTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product in table to restock.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        String productName = (String) lowStockTableModel.getValueAt(selectedRow, 0);
        int currentStock = (int) lowStockTableModel.getValueAt(selectedRow, 1);
        
        // Fetch product ID from the main product table
        ProductDAO productDAO = new ProductDAO();
        List<Product> allProducts = productDAO.getAllProducts();
        int productId = -1;
    
        for (int i = 0; i < allProducts.size(); i++) {
            Product product = allProducts.get(i);
            if (product.getName().equals(productName)) {
                productId = product.getId();
                break;
            }
        }
    
        if (productId == -1) {
            JOptionPane.showMessageDialog(this, "Error: Product ID not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
    
        JLabel label = new JLabel("Restock quantity for " + productName + ": ");
        JTextField quantityField = new JTextField(5);
    
        panel.add(label);
        panel.add(quantityField);
    
        int result = JOptionPane.showConfirmDialog(this, panel, "Restock Product", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int restockAmount = Integer.parseInt(quantityField.getText());
                if (restockAmount <= 0) {
                    JOptionPane.showMessageDialog(this, "Enter a valid quantity!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
    
                // Update stock in the database
                productDAO.restockProduct(productId, restockAmount);
    
                // Update UI table
                int newStock = currentStock + restockAmount;
                lowStockTableModel.setValueAt(newStock, selectedRow, 1);
                
                JOptionPane.showMessageDialog(this, "Stock updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

                loadLowStockData(); // Refresh low stock table
    
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid number entered!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }    
}
