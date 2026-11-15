

/**
 * Project: Lab2DonutShopChowdhury  Oak Donuts (OD) - Donut Shop Menu & Ordering System - MenuDAO
 * Purpose Details: Saves, loads, updates and deletes menu items in the OD_Menu_Item table of the Derby database
 * Course: CMPSC 221
 * Author: Daboraj Chowdhury
 * Date Developed: 11/10/2026
 * Last Date Changed: 11/14/2026
 * Revision: 1.0
 */



// Saves, loads, updates and deletes menu items in the OD_Menu_Item table of the Derby database



package entity;

import core.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MenuDAO implements DAO<Donut>
{
    public MenuDAO() {
    }

    @Override
    public Optional<Donut> get(int id) {
        DB db = DB.getInstance();
        try {
            PreparedStatement stmt = db.getPreparedStatement("SELECT * FROM OD_Menu_Item WHERE Item_ID = ?");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            Donut donut = null;
            if (rs.next()) {
                donut = mapRow(rs);
            }
            return Optional.ofNullable(donut);
        } catch (SQLException ex) {
            System.err.println(ex.toString());
            return Optional.empty();
        }
    }

    @Override
    public List<Donut> getAll() {
        return query("SELECT * FROM OD_Menu_Item ORDER BY Item_ID");
    }

    public List<Donut> getInStock() {
        return query("SELECT * FROM OD_Menu_Item WHERE In_Stock = TRUE ORDER BY Category, Item_ID");
    }

    @Override
    public void insert(Donut donut) {
        DB db = DB.getInstance();
        try {
            PreparedStatement stmt = db.getPreparedStatement(
                    "INSERT INTO OD_Menu_Item(Item_Name, Category, Flavor, Price, In_Stock) VALUES (?, ?, ?, ?, ?)");
            stmt.setString(1, donut.getName());
            stmt.setString(2, donut.getCategory().name());
            stmt.setString(3, donut.getDonutFlavors().name());
            stmt.setDouble(4, donut.getPrice());
            stmt.setBoolean(5, donut.isInStock());
            if (stmt.executeUpdate() > 0) {
                System.out.println("A new menu item was inserted successfully!");
            }
        } catch (SQLException ex) {
            System.err.println(ex.toString());
        }
    }

    @Override
    public void update(Donut donut) {
        DB db = DB.getInstance();
        try {
            PreparedStatement stmt = db.getPreparedStatement(
                    "UPDATE OD_Menu_Item SET Item_Name=?, Category=?, Flavor=?, Price=?, In_Stock=? WHERE Item_ID=?");
            stmt.setString(1, donut.getName());
            stmt.setString(2, donut.getCategory().name());
            stmt.setString(3, donut.getDonutFlavors().name());
            stmt.setDouble(4, donut.getPrice());
            stmt.setBoolean(5, donut.isInStock());
            stmt.setInt(6, donut.getId());
            if (stmt.executeUpdate() > 0) {
                System.out.println("A menu item was updated successfully!");
            }
        } catch (SQLException ex) {
            System.err.println(ex.toString());
        }
    }

    @Override
    public void delete(Donut donut) {
        DB db = DB.getInstance();
        try {
            PreparedStatement stmt = db.getPreparedStatement("DELETE FROM OD_Menu_Item WHERE Item_ID = ?");
            stmt.setInt(1, donut.getId());
            if (stmt.executeUpdate() > 0) {
                System.out.println("A menu item was deleted successfully!");
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
            ResultSet rs = db.executeQuery("SELECT * FROM OD_Menu_Item WHERE Item_ID = -1");
            ResultSetMetaData rsmd = rs.getMetaData();
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                headers.add(rsmd.getColumnLabel(i));
            }
        } catch (SQLException ex) {
            System.err.println(ex.toString());
        }
        return headers;
    }

    private List<Donut> query(String sql) {
        DB db = DB.getInstance();
        List<Donut> donuts = new ArrayList<>();
        try {
            ResultSet rs = db.executeQuery(sql);
            while (rs.next()) {
                donuts.add(mapRow(rs));
            }
        } catch (SQLException ex) {
            System.err.println(ex.toString());
        }
        return donuts;
    }

    private Donut mapRow(ResultSet rs) throws SQLException {
        return new Donut(rs.getInt("Item_ID"), rs.getString("Item_Name"), Category.valueOf(rs.getString("Category")),
                DonutFlavors.valueOf(rs.getString("Flavor")), rs.getDouble("Price"), rs.getBoolean("In_Stock"));
    }
}