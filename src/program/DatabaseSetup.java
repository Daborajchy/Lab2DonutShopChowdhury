
/**
 * Project: Lab2DonutShopChowdhury  Oak Donuts (OD) - Donut Shop Menu & Ordering System - DatabaseSetup
 * Purpose Details: Creates the Oak Donuts database tables the first time the app runs and adds the starter menu
 * Course: CMPSC 221
 * Author: Daboraj Chowdhury
 * Date Developed: 11/10/2026
 * Last Date Changed: 11/14/2026
 * Revision: 1.0
 */






package program;

import core.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseSetup {

    public static void createTables() throws SQLException {
        Connection conn = DB.getInstance().getPreparedStatement("VALUES 1").getConnection();

        if (!tableExists(conn, "OD_MENU_ITEM")) {
            run("CREATE TABLE OD_Menu_Item ("
                    + "Item_ID INT NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY, "
                    + "Item_Name VARCHAR(60) NOT NULL, "
                    + "Category VARCHAR(20) NOT NULL, "
                    + "Flavor VARCHAR(30) NOT NULL, "
                    + "Price DECIMAL(6,2) NOT NULL, "
                    + "In_Stock BOOLEAN NOT NULL)");
            addStarterMenu();
        }
        if (!tableExists(conn, "OD_ORDERS")) {
            run("CREATE TABLE OD_Orders ("
                    + "Order_ID INT NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY, "
                    + "Transaction_ID VARCHAR(20) NOT NULL UNIQUE, "
                    + "Order_Date_Time TIMESTAMP NOT NULL, "
                    + "Customer_Name VARCHAR(60), "
                    + "Subtotal DECIMAL(8,2) NOT NULL, "
                    + "Tax DECIMAL(8,2) NOT NULL, "
                    + "Order_Total DECIMAL(8,2) NOT NULL)");
        }
        if (!tableExists(conn, "OD_ORDER_LINE")) {
            run("CREATE TABLE OD_Order_Line ("
                    + "Line_ID INT NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY, "
                    + "Transaction_ID VARCHAR(20) NOT NULL, "
                    + "Item_Name VARCHAR(60) NOT NULL, "
                    + "Options VARCHAR(120), "
                    + "Quantity INT NOT NULL, "
                    + "Unit_Price DECIMAL(6,2) NOT NULL, "
                    + "FOREIGN KEY (Transaction_ID) REFERENCES OD_Orders(Transaction_ID) ON DELETE CASCADE)");
        }
    }

    private static void addStarterMenu() throws SQLException {
        String[][] items = {
                {"Glazed Donut", "DONUTS", "GLAZED", "1.39"},
                {"Chocolate Sprinkles", "DONUTS", "CHOCOLATE", "1.69"},
                {"Boston Creme", "DONUTS", "BOSTON_CREAM_PIE", "2.29"},
                {"Strawberry Frosted", "DONUTS", "STRAWBERRY", "1.69"},
                {"Blueberry Cake", "DONUTS", "BLUEBERRY", "2.00"},
                {"Nittany Lion Donut", "DONUTS", "PENN_STATE_NITANNY_LION", "2.49"},
                {"House Coffee", "DRINKS", "NONE", "1.89"},
                {"Latte", "DRINKS", "NONE", "3.50"},
                {"Hot Chocolate", "DRINKS", "NONE", "2.59"},
                {"Iced Tea", "DRINKS", "NONE", "2.19"},

        };
        for (String[] item : items) {
            PreparedStatement ps = DB.getInstance().getPreparedStatement(
                    "INSERT INTO OD_Menu_Item(Item_Name, Category, Flavor, Price, In_Stock) VALUES (?, ?, ?, ?, TRUE)");
            ps.setString(1, item[0]);
            ps.setString(2, item[1]);
            ps.setString(3, item[2]);
            ps.setDouble(4, Double.parseDouble(item[3]));
            ps.executeUpdate();
        }
    }

    private static boolean tableExists(Connection conn, String name) throws SQLException {
        try (ResultSet rs = conn.getMetaData().getTables(null, null, name, new String[]{"TABLE"})) {
            return rs.next();
        }
    }

    private static void run(String sql) throws SQLException {
        DB.getInstance().getPreparedStatement(sql).executeUpdate();
    }
}