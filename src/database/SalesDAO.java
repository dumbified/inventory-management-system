package database;

// this DAO is for handling all the database operations related to the Sales model.
// Big shoutout to chatgpt and online forums for the code snippet.

import models.Sales;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalesDAO {

    // Add a sale
    public boolean addSale(Sales sale) {
        String query = "INSERT INTO sales (customer, product_id, quantity, total_price, payment_method, date) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, sale.getCustomer());
            stmt.setInt(2, sale.getProductId());
            stmt.setInt(3, sale.getQuantity());
            stmt.setDouble(4, sale.getTotalPrice());
            stmt.setString(5, sale.getPaymentMethod());
            stmt.setTimestamp(6, new Timestamp(sale.getDate().getTime()));

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;  // Return true if a sale was inserted
        } catch (SQLException e) {
            System.err.println("Error adding sale: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Get all sales
    public List<Sales> getAllSales() {
        List<Sales> salesList = new ArrayList<>();
        String query = "SELECT * FROM sales ORDER BY date DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Sales sale = new Sales(
                    rs.getInt("id"),
                    rs.getString("customer"),
                    rs.getInt("product_id"),  // Correctly fetching product ID
                    rs.getInt("quantity"),
                    rs.getDouble("total_price"),
                    rs.getString("payment_method"),
                    rs.getTimestamp("date")
                );
                salesList.add(sale);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving sales: " + e.getMessage());
            e.printStackTrace();
        }
        return salesList;
    }
}
