package components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import database.ProductDAO;
import models.Product;

public class AddProduct extends JFrame {
    private JPanel panel;
    private JComboBox<String> category;
    private JTextField nameField;
    private JTextField quantityField;
    private JTextField priceField;

    public AddProduct() {
        setTitle("Add Product");
        setSize(280, 370);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        panel = new JPanel();
        add(panel, BorderLayout.CENTER);
        addProductComponents(panel);

        setResizable(false);
        setVisible(true);
    }

    //components setup
    private void addProductComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel productIDLabel = new JLabel("ID: ");
        productIDLabel.setBounds(30, 30, 280, 25);
        panel.add(productIDLabel);

        int newProductId = new ProductDAO().getNextProductId(); // Fetch next ID from database
        JLabel itemIDLabelValue = new JLabel(String.valueOf(newProductId));
        itemIDLabelValue.setBounds(90, 30, 55, 25);
        panel.add(itemIDLabelValue);

        JLabel nameLabel = new JLabel("Name: ");
        nameLabel.setBounds(30, 70, 80, 25);
        panel.add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(90, 70, 160, 25);
        panel.add(nameField);

        JLabel quantityLabel = new JLabel("Quantity: ");
        quantityLabel.setBounds(30, 110, 80, 25);
        panel.add(quantityLabel);

        quantityField = new JTextField();
        quantityField.setBounds(90, 110, 160, 25);
        panel.add(quantityField);

        JLabel priceLabel = new JLabel("Price: ");
        priceLabel.setBounds(30, 150, 80, 25);
        panel.add(priceLabel);

        priceField = new JTextField();
        priceField.setBounds(90, 150, 160, 25);
        panel.add(priceField);

        JLabel categoryLabel = new JLabel("Category: ");
        categoryLabel.setBounds(30, 190, 80, 25);
        panel.add(categoryLabel);

        category = new JComboBox<>(getCategories());
        category.setBounds(90, 192, 160, 25);
        panel.add(category);

        JButton addCategoryButton = new JButton("Add Category");
        addCategoryButton.setBounds(90, 220, 160, 25);
        addCategoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNewCategory();
            }
        });
        panel.add(addCategoryButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBounds(30, 270, 90, 25);
        cancelButton.addActionListener(e -> dispose());
        panel.add(cancelButton);

        JButton addButton = new JButton("Add");
        addButton.setBounds(150, 270, 90, 25);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addProductToDatabase();
            }
        });
        panel.add(addButton);
    }

    private void addNewCategory() {
        String newCategory = JOptionPane.showInputDialog(this, "Enter new category:");
        if (newCategory != null && !newCategory.trim().isEmpty()) {
            category.addItem(newCategory);
            category.setSelectedItem(newCategory);
        }
    }

    private void addProductToDatabase() {
        try {
            int id = new ProductDAO().getNextProductId();// Auto-generate ID
            String name = nameField.getText();
            String categoryValue = (String) category.getSelectedItem();
            double price = Double.parseDouble(priceField.getText());
            int quantity = Integer.parseInt(quantityField.getText());

            Product product = new Product(id, name, categoryValue, price, quantity);
            ProductDAO productDAO = new ProductDAO();
            productDAO.addProduct(product);

            JOptionPane.showMessageDialog(this, "Product added successfully!");
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter valid data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String[] getCategories() {
        ProductDAO productDAO = new ProductDAO();
        List<String> categories = productDAO.getAllCategories();
        categories.remove("All"); // Remove "All" from the list
        return categories.toArray(new String[0]);
    }

    public static void main(String[] args) {
        new AddProduct();
    }
}