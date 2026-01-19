package database;

//this DAO is for handling all the database operations related to the Product model.
//Big shoutout to chatgpt and online forums for the code snippet.


import models.Product;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    //----------------------------- CRUD Operations -----------------------------

    // Get all products
    public List<Product> getAllProducts() {
        return getProductsFromQuery("SELECT * FROM products");
    }

    // Get products by category
    public List<Product> getProductsByCategory(String category) {
        return getProductsFromQuery("SELECT * FROM products WHERE category = ?", category);
    }

    // Get low stock products
    public List<Product> getLowStockProducts() {
        return getProductsFromQuery("SELECT * FROM products WHERE quantity <= 10");
    }

    // Search product by name
    public List<Product> searchProduct(String productName) {
        return getProductsFromQuery("SELECT * FROM products WHERE name LIKE ?", "%" + productName + "%");
    }

    // Add a new product
    public void addProduct(Product product) {
        int nextId = getNextProductId(); // Find the next available ID (including gaps)
        String query = "INSERT INTO products (id, name, category, price, quantity) VALUES (?, ?, ?, ?, ?)";
        executeUpdate(query, nextId, product.getName(), product.getCategory(), product.getPrice(), product.getQuantity());
    }

    // Update product details
    public boolean updateProduct(Product product) {
        String query = "UPDATE products SET name = ?, category = ?, price = ?, quantity = ? WHERE id = ?";
        return executeUpdate(query, product.getName(), product.getCategory(), product.getPrice(), product.getQuantity(), product.getId());
    }

    // Delete a product
    public boolean deleteProduct(int id) {
        return executeUpdate("DELETE FROM products WHERE id = ?", id);
    }

    // Restock product
    public void restockProduct(int productId, int quantityToAdd) {
        executeUpdate("UPDATE products SET quantity = quantity + ? WHERE id = ?", quantityToAdd, productId);
    }

    public Product getProductById(int productId) {
        String query = "SELECT * FROM products WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, productId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getDouble("price"),
                        rs.getInt("quantity")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching product by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null; // Return null if product not found
    }

    //----------------------------- Helper Methods -----------------------------

    private List<Product> getProductsFromQuery(String query, Object... params) {
        List<Product> products = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            setParameters(stmt, params);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    private Product getProductFromQuery(String query, Object... params) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            setParameters(stmt, params);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToProduct(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean executeUpdate(String query, Object... params) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            setParameters(stmt, params);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void setParameters(PreparedStatement stmt, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }
    }

    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        return new Product(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("category"),
            rs.getDouble("price"),
            rs.getInt("quantity")
        );
    }

    //----------------------------- Statistics & ID Handling -----------------------------

    // Get next available product ID
    public int getNextProductId() {
        int nextId = 1001; // Starting ID
        try {
            Connection conn = DatabaseConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id FROM products ORDER BY id ASC");
    
            while (rs.next()) {
                if (rs.getInt("id") == nextId) {
                    nextId++; // Increment if ID exists
                } else {
                    break; // Found a gap, use this ID
                }
            }
    
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nextId;
    }

    // Get all categories
    public List<String> getAllCategories() {
        List<String> categories = new ArrayList<>();
        categories.add("All"); // Default option
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT DISTINCT category FROM products ORDER BY category ASC")) {
            while (rs.next()) {
                categories.add(rs.getString("category"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    // Fetch various statistics
    public Product getLowestStockProduct() {
        return getProductFromQuery("SELECT * FROM products ORDER BY quantity ASC LIMIT 1");
    }

    public Product getMostExpensiveProduct() {
        return getProductFromQuery("SELECT * FROM products ORDER BY price DESC LIMIT 1");
    }

    public Product getHighestQuantityProduct() {
        return getProductFromQuery("SELECT * FROM products ORDER BY quantity DESC LIMIT 1");
    }

    public Product getHighestTotalValueProduct() {
        return getProductFromQuery("SELECT *, (price * quantity) AS total_value FROM products ORDER BY total_value DESC LIMIT 1");
    }

    public Product getCheapestProduct() {
        return getProductFromQuery("SELECT * FROM products ORDER BY price ASC LIMIT 1");
    }

    public double getAverageProductPrice() {
        return getDoubleFromQuery("SELECT AVG(price) AS avg_price FROM products");
    }

    public double getTotalStockValue() {
        return getDoubleFromQuery("SELECT SUM(price * quantity) AS total_stock_value FROM products");
    }

    public int getTotalProductsCount() {
        return getIntFromQuery("SELECT COUNT(*) AS total_products FROM products");
    }

    public int getOutOfStockCount() {
        return getIntFromQuery("SELECT COUNT(*) AS out_of_stock FROM products WHERE quantity = 0");
    }

    public String getCategoryWithMostProducts() {
        return getStringFromQuery("SELECT category FROM products GROUP BY category ORDER BY COUNT(*) DESC LIMIT 1");
    }

    public String getHighestStockValueCategory() {
        return getStringFromQuery("SELECT category FROM products GROUP BY category ORDER BY SUM(price * quantity) DESC LIMIT 1");
    }

    private double getDoubleFromQuery(String query) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    private int getIntFromQuery(String query) {
        return (int) getDoubleFromQuery(query);
    }

    private String getStringFromQuery(String query) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) return rs.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "N/A";
    }
}