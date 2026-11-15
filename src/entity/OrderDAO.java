

/**
 * Project: Lab2DonutShopChowdhury  Oak Donuts (OD) - Donut Shop Menu & Ordering System - OrderDAO
 * Purpose Details: Saves, loads, updates and deletes orders and their items in the OD_Orders and OD_Order_Line tables
 * Course: CMPSC 221
 * Author: Daboraj Chowdhury
 * Date Developed: 11/10/2026
 * Last Date Changed: 11/14/2026
 * Revision: 1.0
 */






package entity;

import core.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderDAO implements DAO<Order>
{
    public OrderDAO() {
    }

    @Override
    public Optional<Order> get(int id) {
        DB db = DB.getInstance();
        try {
            PreparedStatement stmt = db.getPreparedStatement("SELECT * FROM OD_Orders WHERE Order_ID = ?");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            Order order = null;
            if (rs.next()) {
                order = mapRow(rs);
                order.setItems(getItems(order.getTransactionId()));
            }
            return Optional.ofNullable(order);
        } catch (SQLException ex) {
            System.err.println(ex.toString());
            return Optional.empty();
        }
    }

    @Override
    public List<Order> getAll() {
        DB db = DB.getInstance();
        List<Order> orders = new ArrayList<>();
        try {
            ResultSet rs = db.executeQuery("SELECT * FROM OD_Orders ORDER BY Order_Date_Time DESC, Order_ID DESC");
            while (rs.next()) {
                orders.add(mapRow(rs));
            }
        } catch (SQLException ex) {
            System.err.println(ex.toString());
        }
        return orders;
    }

    public List<OrderItem> getItems(String transactionId) {
        DB db = DB.getInstance();
        List<OrderItem> items = new ArrayList<>();
        try {
            PreparedStatement stmt = db.getPreparedStatement(
                    "SELECT * FROM OD_Order_Line WHERE Transaction_ID = ? ORDER BY Line_ID");
            stmt.setString(1, transactionId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                items.add(new OrderItem(rs.getInt("Line_ID"), rs.getString("Transaction_ID"), rs.getString("Item_Name"),
                        rs.getString("Options"), rs.getInt("Quantity"), rs.getDouble("Unit_Price")));
            }
        } catch (SQLException ex) {
            System.err.println(ex.toString());
        }
        return items;
    }

    @Override
    public void insert(Order order) {
        DB db = DB.getInstance();
        try {
            order.setOrderDateTime(LocalDateTime.now().withNano(0));
            order.setTransactionId(nextTransactionId());
            order.calculateTotal();
            PreparedStatement stmt = db.getPreparedStatement(
                    "INSERT INTO OD_Orders(Transaction_ID, Order_Date_Time, Customer_Name, Subtotal, Tax, Order_Total) VALUES (?, ?, ?, ?, ?, ?)");
            stmt.setString(1, order.getTransactionId());
            stmt.setTimestamp(2, Timestamp.valueOf(order.getOrderDateTime()));
            stmt.setString(3, order.getCustomerName());
            stmt.setDouble(4, order.getSubtotal());
            stmt.setDouble(5, order.getTax());
            stmt.setDouble(6, order.getOrderTotal());
            if (stmt.executeUpdate() > 0) {
                insertItems(order);
                System.out.println("Order " + order.getTransactionId() + " was inserted successfully!");
            }
        } catch (SQLException ex) {
            System.err.println(ex.toString());
        }
    }

    @Override
    public void update(Order order) {
        DB db = DB.getInstance();
        try {
            order.calculateTotal();
            PreparedStatement stmt = db.getPreparedStatement(
                    "UPDATE OD_Orders SET Customer_Name=?, Subtotal=?, Tax=?, Order_Total=? WHERE Order_ID=?");
            stmt.setString(1, order.getCustomerName());
            stmt.setDouble(2, order.getSubtotal());
            stmt.setDouble(3, order.getTax());
            stmt.setDouble(4, order.getOrderTotal());
            stmt.setInt(5, order.getOrderId());
            if (stmt.executeUpdate() > 0) {
                PreparedStatement del = db.getPreparedStatement("DELETE FROM OD_Order_Line WHERE Transaction_ID = ?");
                del.setString(1, order.getTransactionId());
                del.executeUpdate();
                insertItems(order);
                System.out.println("Order " + order.getTransactionId() + " was updated successfully!");
            }
        } catch (SQLException ex) {
            System.err.println(ex.toString());
        }
    }

    @Override
    public void delete(Order order) {
        DB db = DB.getInstance();
        try {
            PreparedStatement stmt = db.getPreparedStatement("DELETE FROM OD_Orders WHERE Order_ID = ?");
            stmt.setInt(1, order.getOrderId());
            if (stmt.executeUpdate() > 0) {
                System.out.println("Order " + order.getTransactionId() + " was deleted successfully!");
            }
        } catch (SQLException ex) {
            System.err.println(ex.toString());
        }
    }

    @Override
    public List<String> getColumnNames() {
        DB db = DB.getInstance();
        List<String> headers = new ArrayList<>();
        try {
            ResultSet rs = db.executeQuery("SELECT * FROM OD_Orders WHERE Order_ID = -1");
            ResultSetMetaData rsmd = rs.getMetaData();
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                headers.add(rsmd.getColumnLabel(i));
            }
        } catch (SQLException ex) {
            System.err.println(ex.toString());
        }
        return headers;
    }

    private String nextTransactionId() throws SQLException {
        String prefix = "OD-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "-";
        PreparedStatement stmt = DB.getInstance().getPreparedStatement(
                "SELECT MAX(Transaction_ID) FROM OD_Orders WHERE Transaction_ID LIKE ?");
        stmt.setString(1, prefix + "%");
        ResultSet rs = stmt.executeQuery();
        int next = 1;
        if (rs.next() && rs.getString(1) != null) {
            next = Integer.parseInt(rs.getString(1).substring(prefix.length())) + 1;
        }
        return prefix + String.format("%04d", next);
    }

    private void insertItems(Order order) throws SQLException {
        PreparedStatement stmt = DB.getInstance().getPreparedStatement(
                "INSERT INTO OD_Order_Line(Transaction_ID, Item_Name, Options, Quantity, Unit_Price) VALUES (?, ?, ?, ?, ?)");
        for (OrderItem item : order.getItems()) {
            stmt.setString(1, order.getTransactionId());
            stmt.setString(2, item.getItemName());
            stmt.setString(3, item.getOptions());
            stmt.setInt(4, item.getQuantity());
            stmt.setDouble(5, item.getUnitPrice());
            stmt.executeUpdate();
        }
    }

    private Order mapRow(ResultSet rs) throws SQLException {
        return new Order(rs.getInt("Order_ID"), rs.getString("Transaction_ID"),
                rs.getTimestamp("Order_Date_Time").toLocalDateTime(),
                rs.getString("Customer_Name"), rs.getDouble("Subtotal"), rs.getDouble("Tax"), rs.getDouble("Order_Total"));
    }
}