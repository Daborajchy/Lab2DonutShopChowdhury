



/**
 * Project: Lab2DonutShopChowdhury  Oak Donuts (OD) - Donut Shop Menu & Ordering System - Order
 * Purpose Details: // A customer order with its transaction ID, date/time, customer name, items, subtotal, 8% tax and total
 * Course: CMPSC 221
 * Author: Daboraj Chowdhury
 * Date Developed: 11/10/2026
 * Last Date Changed: 11/14/2026
 * Revision: 1.0
 */

package entity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order
{
    public static final double TAX_RATE = 0.08;

    private int orderId;
    private String transactionId;
    private LocalDateTime orderDateTime;
    private String customerName;
    private double subtotal;
    private double tax;
    private double orderTotal;
    private List<OrderItem> items = new ArrayList<>();

    public Order() {
    }

    public Order(int orderId, String transactionId, LocalDateTime orderDateTime, String customerName,
                 double subtotal, double tax, double orderTotal) {
        this.orderId = orderId;
        this.transactionId = transactionId;
        this.orderDateTime = orderDateTime;
        this.customerName = customerName;
        this.subtotal = subtotal;
        this.tax = tax;
        this.orderTotal = orderTotal;
    }

    public void calculateTotal() {
        double sum = 0;
        for (OrderItem item : items) {
            sum += item.getLineTotal();
        }
        subtotal = round(sum);
        tax = round(subtotal * TAX_RATE);
        orderTotal = round(subtotal + tax);
    }

    public static double round(double amount) {
        return Math.round(amount * 100.0) / 100.0;
    }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public LocalDateTime getOrderDateTime() { return orderDateTime; }
    public void setOrderDateTime(LocalDateTime orderDateTime) { this.orderDateTime = orderDateTime; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }

    public double getTax() { return tax; }
    public void setTax(double tax) { this.tax = tax; }

    public double getOrderTotal() { return orderTotal; }
    public void setOrderTotal(double orderTotal) { this.orderTotal = orderTotal; }

    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
}