package components;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import database.ProductDAO;
import models.Product;

public class ViewAll extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JTextField itemNameField;

    public ViewAll() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(780, 600));

        JPanel contentPanel = new JPanel(new BorderLayout(0, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        contentPanel.add(createSearchPanel(), BorderLayout.NORTH);
        contentPanel.add(createTablePanel(), BorderLayout.CENTER);
        contentPanel.add(createButtonPanel(), BorderLayout.SOUTH);

        add(contentPanel, BorderLayout.CENTER);
        loadData("All");
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new GridBagLayout());
        searchPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), 
            "Products Filter",
            TitledBorder.DEFAULT_JUSTIFICATION,
            TitledBorder.DEFAULT_POSITION
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // align the label
        Dimension labelSize = new Dimension(100, 20);

        // product name
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel nameLabel = new JLabel("Product Name:");
        nameLabel.setPreferredSize(labelSize);
        nameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        searchPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        itemNameField = new JTextField(20);
        searchPanel.add(itemNameField, gbc);

        // category filter
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0.0;
        JLabel filterLabel = new JLabel("Category:");
        filterLabel.setPreferredSize(labelSize);
        filterLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        searchPanel.add(filterLabel, gbc);

        gbc.gridx = 1;
        JComboBox<String> filterCategoryBox = new JComboBox<>(getCategories());
        searchPanel.add(filterCategoryBox, gbc);
        filterCategoryBox.addActionListener(e -> loadData(filterCategoryBox.getSelectedItem().toString()));

        return searchPanel;
    }

    private JPanel createTablePanel() {
        String[] columns = {"ID", "Product Name", "Category", "Price (RM)", "Quantity", "Total Value (RM)"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Makes all cells non-editable
            }
        };
        table = new JTable(model);
        table.setPreferredScrollableViewportSize(new Dimension(750, 400));
        table.setFillsViewportHeight(true);
        table.getTableHeader().setReorderingAllowed(false);
        table.setRowHeight(16);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        adjustColumnWidths();

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        return tablePanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));

        addButton(buttonPanel, "Add", e -> openAddProductDialog());
        addButton(buttonPanel, "Delete", e -> deleteProduct());
        addButton(buttonPanel, "Update", e -> updateProduct());
        addButton(buttonPanel, "Search", e -> searchProduct());

        return buttonPanel;
    }

    private void openAddProductDialog() {
        AddProduct addProductDialog = new AddProduct();
        addProductDialog.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                loadData("All");
            }
        });
    }

    private void deleteProduct() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this product?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int productId = (int) model.getValueAt(selectedRow, 0);
            boolean deleted = new ProductDAO().deleteProduct(productId);

            if (deleted) {
                JOptionPane.showMessageDialog(this, "Product deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadData("All");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete product.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateProduct() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product to update.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int productId = (int) model.getValueAt(selectedRow, 0);
        String productName = model.getValueAt(selectedRow, 1).toString();
        String category = model.getValueAt(selectedRow, 2).toString();
        double price = Double.parseDouble(model.getValueAt(selectedRow, 3).toString());
        int quantity = Integer.parseInt(model.getValueAt(selectedRow, 4).toString());

        new UpdateUI(productId, productName, category, price, quantity, this);
    }

    private void adjustColumnWidths() {
        table.getColumnModel().getColumn(0).setPreferredWidth(50);   // ID column (shorter)
        table.getColumnModel().getColumn(1).setPreferredWidth(200);  // Product Name (wider)
        table.getColumnModel().getColumn(2).setPreferredWidth(120);  // Category
        table.getColumnModel().getColumn(3).setPreferredWidth(80);   // Price
        table.getColumnModel().getColumn(4).setPreferredWidth(80);   // Quantity
        table.getColumnModel().getColumn(5).setPreferredWidth(100);  // Total Value
    }

    private String[] getCategories() {
        ProductDAO productDAO = new ProductDAO();
        List<String> categories = productDAO.getAllCategories();
        if (!categories.contains("All")) {// Ensure "All" is not in the list
            categories.add(0, "All");// Add "All" at the beginning
        }
        return categories.toArray(new String[0]);
    }

    public void loadData(String selectedCategory) {
        model.setRowCount(0);
        ProductDAO productDAO = new ProductDAO();
        List<Product> products = "All".equals(selectedCategory) ? productDAO.getAllProducts() : productDAO.getProductsByCategory(selectedCategory);

        for (Product product : products) {
            model.addRow(new Object[]{
                product.getId(),
                product.getName(),
                product.getCategory(),
                product.getPrice(),
                product.getQuantity(),
                String.format("%.2f", product.getPrice() * product.getQuantity())
            });
        }
    }

    private void searchProduct() {
        model.setRowCount(0);
        String search = itemNameField.getText();
        List<Product> products = new ProductDAO().searchProduct(search);

        for (Product product : products) {
            model.addRow(new Object[]{
                product.getId(),
                product.getName(),
                product.getCategory(),
                product.getPrice(),
                product.getQuantity(),
                String.format("%.2f", product.getPrice() * product.getQuantity())
            });
        }
    }

    private void addButton(JPanel panel, String text, java.awt.event.ActionListener action) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(100, 30));
        button.addActionListener(action);
        panel.add(button);
    }

    public static void main(String[] args) {
        new ViewAll();
    }
}
