package components;

import database.ProductDAO;
import database.SalesDAO;
import models.Product;
import models.Sales;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Date;
import java.util.List;

public class SalesUI extends JPanel {

    private JTextField txtCustomer;
    private JComboBox<String> cbProduct;
    private JTextField txtQuantity;
    private JComboBox<String> cbPayment;
    private DefaultTableModel tableModel;
    private JTable table;

    private ProductDAO productDAO;
    private SalesDAO salesDAO;

    public SalesUI() {
        productDAO = new ProductDAO();
        salesDAO = new SalesDAO();

        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel salesPanel = createSalesUI();
        JScrollPane salesTable = createSalesTable();


        add(salesPanel, BorderLayout.NORTH);
        add(salesTable, BorderLayout.CENTER);
        adjustColumnWidths();

        loadSales();
    }

    //sales panel ui
    private JPanel createSalesUI() {
        JPanel salesPanel = new JPanel(new GridBagLayout());
        salesPanel.setBorder(BorderFactory.createTitledBorder("New Sale"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel customerLabel = new JLabel("Customer Name:");
        txtCustomer = new JTextField();

        JLabel productLabel = new JLabel("Product:");
        cbProduct = new JComboBox<>();
        loadProducts();

        JLabel quantityLabel = new JLabel("Quantity:");
        JPanel quantityPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        JButton btnMinus = new JButton("-");
        txtQuantity = new JTextField("1", 3);
        txtQuantity.setHorizontalAlignment(JTextField.CENTER);
        JButton btnPlus = new JButton("+");
        quantityPanel.add(btnMinus);
        quantityPanel.add(txtQuantity);
        quantityPanel.add(btnPlus);

        JLabel paymentLabel = new JLabel("Payment Method:");
        cbPayment = new JComboBox<>(new String[]{"Cash", "Card", "Online Banking"});

        JButton btnAddSale = new JButton("Add Sale");
        btnAddSale.addActionListener(e -> addSale());

        gbc.gridx = 0; gbc.gridy = 0;
        salesPanel.add(customerLabel, gbc);
        gbc.gridx = 1; salesPanel.add(txtCustomer, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        salesPanel.add(productLabel, gbc);
        gbc.gridx = 1; salesPanel.add(cbProduct, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        salesPanel.add(quantityLabel, gbc);
        gbc.gridx = 1; salesPanel.add(quantityPanel, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        salesPanel.add(paymentLabel, gbc);
        gbc.gridx = 1; salesPanel.add(cbPayment, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        salesPanel.add(btnAddSale, gbc);

        // Set button actions
        btnMinus.addActionListener(e -> updateQuantity(-1));
        btnPlus.addActionListener(e -> updateQuantity(1));

        return salesPanel;
    }

    //sales table ui
    private JScrollPane createSalesTable() {
        String[] columnNames = {"Sale ID", "Customer", "Product", "Single Price", "Quantity", "Total Price", "Payment Method", "Date"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Makes all cells non-editable
            }
        };
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        table.getTableHeader().setReorderingAllowed(false);
        table.setRowHeight(16);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Sales History"));

        adjustColumnWidths();
        return scrollPane;
    }

    private void adjustColumnWidths() {
        table.getColumnModel().getColumn(0).setPreferredWidth(50);   // Sale ID
        table.getColumnModel().getColumn(1).setPreferredWidth(110);  // Customer
        table.getColumnModel().getColumn(2).setPreferredWidth(180);  // Product
        table.getColumnModel().getColumn(3).setPreferredWidth(90);  // Single Price
        table.getColumnModel().getColumn(4).setPreferredWidth(60);   // Quantity
        table.getColumnModel().getColumn(5).setPreferredWidth(90);  // Total Price
        table.getColumnModel().getColumn(6).setPreferredWidth(120);  // Payment Method
        table.getColumnModel().getColumn(7).setPreferredWidth(180);  // Date
    }

    private void updateQuantity(int change) {
        int quantity = Integer.parseInt(txtQuantity.getText());
        quantity = Math.max(1, quantity + change);
        txtQuantity.setText(String.valueOf(quantity));
    }

    private void loadProducts() {
        cbProduct.removeAllItems();
        List<Product> products = productDAO.getAllProducts();
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            if (product.getQuantity() > 0) { // Only add products that are in stock
                cbProduct.addItem(product.getId() + " - " + product.getName());
            }
        }
    }

    private void addSale() {
        String customer = txtCustomer.getText();
        String product = (String) cbProduct.getSelectedItem();
        if (product == null || customer.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a product and enter customer name.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int productId = Integer.parseInt(product.split(" - ")[0]); // Extract ID
        
         // Validate quantity input
        int quantity;
        try {
            quantity = Integer.parseInt(txtQuantity.getText().trim());
            if (quantity <= 0) {
                throw new NumberFormatException(); // Treat zero or negative values as invalid
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid number entered!", "Error", JOptionPane.ERROR_MESSAGE);
            txtQuantity.setText("1"); // Reset to a valid default value
            return;
        }

        // Get product details
        Product selectedProduct = productDAO.getProductById(productId);
        if (selectedProduct == null) {
            JOptionPane.showMessageDialog(this, "Product not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check stock availability
        if (selectedProduct.getQuantity() < quantity) {
            JOptionPane.showMessageDialog(this, "Insufficient stock available!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Calculate total price
        double totalPrice = selectedProduct.getPrice() * quantity;
        String paymentMethod = (String) cbPayment.getSelectedItem();
        Date date = new Date();

        // Add sale to database
        Sales newSale = new Sales(customer, productId, quantity, totalPrice, paymentMethod, date);
        boolean saleAdded = salesDAO.addSale(newSale);

        if (saleAdded) {
            // Deduct quantity from inventory
            selectedProduct.setQuantity(selectedProduct.getQuantity() - quantity);
            productDAO.updateProduct(selectedProduct);

            JOptionPane.showMessageDialog(this, "Sale added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadSales();
            loadProducts(); // Refresh product list to reflect updated stock
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add sale.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadSales() {
        tableModel.setRowCount(0);
        List<Sales> salesList = salesDAO.getAllSales();
        for (int i = 0; i < salesList.size(); i++) {
            Sales sale = salesList.get(i);
            Product product = productDAO.getProductById(sale.getProductId());
            if (product != null) {
                tableModel.addRow(new Object[]{
                    sale.getId(),
                    sale.getCustomer(),
                    product.getName(),
                    product.getPrice(),
                    sale.getQuantity(),
                    sale.getTotalPrice(),
                    sale.getPaymentMethod(),
                    sale.getDate()
                });
            }
        }
    }
}
