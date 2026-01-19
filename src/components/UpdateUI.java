package components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import database.ProductDAO;
import models.Product;
import java.util.List;

public class UpdateUI extends JFrame {
    private JPanel panel;
    private JTextField nameField;
    private JTextField quantityField;
    private JTextField priceField;
    private JComboBox<String> category;
    private JButton saveButton;
    private JButton cancelButton;
    private int productId;
    private ViewAll parent; // reference to refresh table (inherited from ViewAll)

    public UpdateUI(int productId, String productName, String categoryName, double price, int quantity, ViewAll parent) {
        this.productId = productId;
        this.parent = parent;

        setTitle("Update Product");
        setSize(280, 370);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        panel = new JPanel();
        add(panel, BorderLayout.CENTER);
        updateComponents(productName, categoryName, price, quantity);

        setResizable(false);
        setVisible(true);
    }

    private void updateComponents(String productName, String categoryName, double price, int quantity) {
        panel.setLayout(null);

        JLabel productIDLabel = new JLabel("ID: " + productId);
        productIDLabel.setBounds(30, 30, 200, 25);
        panel.add(productIDLabel);

        JLabel nameLabel = new JLabel("Name: ");
        nameLabel.setBounds(30, 70, 80, 25);
        panel.add(nameLabel);

        nameField = new JTextField(productName);
        nameField.setBounds(90, 70, 160, 25);
        panel.add(nameField);

        JLabel quantityLabel = new JLabel("Quantity: ");
        quantityLabel.setBounds(30, 110, 80, 25);
        panel.add(quantityLabel);

        quantityField = new JTextField(String.valueOf(quantity));
        quantityField.setBounds(90, 110, 160, 25);
        panel.add(quantityField);

        JLabel priceLabel = new JLabel("Price: ");
        priceLabel.setBounds(30, 150, 80, 25);
        panel.add(priceLabel);

        priceField = new JTextField(String.valueOf(price));
        priceField.setBounds(90, 150, 160, 25);
        panel.add(priceField);

        JLabel categoryLabel = new JLabel("Category: ");
        categoryLabel.setBounds(30, 190, 80, 25);
        panel.add(categoryLabel);

        category = new JComboBox<>(getCategories());
        category.setSelectedItem(categoryName);
        category.setBounds(90, 192, 160, 25);
        panel.add(category);

        saveButton = new JButton("Save");
        saveButton.setBounds(30, 270, 90, 25);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveUpdatedProduct();
            }
        });
        panel.add(saveButton);

        cancelButton = new JButton("Cancel");
        cancelButton.setBounds(150, 270, 90, 25);
        cancelButton.addActionListener(e -> dispose());
        panel.add(cancelButton);
    }

    private String[] getCategories() {
        ProductDAO productDAO = new ProductDAO();
        List<String> categories = productDAO.getAllCategories();
        categories.remove("All"); // Remove "All" from the list
        return categories.toArray(new String[0]);
    }

    private void saveUpdatedProduct() {
        try {
            String newName = nameField.getText();
            String newCategory = (String) category.getSelectedItem();
            double newPrice = Double.parseDouble(priceField.getText());
            int newQuantity = Integer.parseInt(quantityField.getText());

            Product updatedProduct = new Product(productId, newName, newCategory, newPrice, newQuantity);
            ProductDAO productDAO = new ProductDAO();
            boolean updated = productDAO.updateProduct(updatedProduct);

            if (updated) {
                JOptionPane.showMessageDialog(this, "Product updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                parent.loadData("All"); // Refresh the table
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update product.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
