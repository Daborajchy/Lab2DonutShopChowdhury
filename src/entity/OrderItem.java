

/**
 * Project: Lab2DonutShopChowdhury  Oak Donuts (OD) - Donut Shop Menu & Ordering System - OrderItem
 * Purpose Details:  One line in an order, like "5 x Boston Creme, Icing: Chocolate" with the quantity and the price included
 * Course: CMPSC 221
 * Author: Daboraj Chowdhury
 * Date Developed: 11/10/2026
 * Last Date Changed: 11/14/2026
 * Revision: 1.0
 */






package entity;

public class OrderItem {
    private int itemId;
    private String transactionId;
    private String itemName;
    private String options;
    private int quantity;
    private double unitPrice;

    public OrderItem(int itemId, String transactionId, String itemName, String options, int quantity, double unitPrice) {
        this.itemId = itemId;
        this.transactionId = transactionId;
        this.itemName = itemName;
        this.options = options;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public OrderItem(String itemName, String options, int quantity, double unitPrice) {
        this(0, null, itemName, options, quantity, unitPrice);
    }

    public double getLineTotal() {
        return unitPrice * quantity;
    }

    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public String getOptions() { return options; }
    public void setOptions(String options) { this.options = options; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }
}