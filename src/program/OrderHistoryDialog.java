

/**
 * Project: Lab2DonutShopChowdhury  Oak Donuts (OD) - Donut Shop Menu & Ordering System - OrderHistoryDialog
 * Purpose Details: File > Order History window for viewing past orders and their items, editing the customer name, and deleting orders
 * Course: CMPSC 221
 * Author: Daboraj Chowdhury
 * Date Developed: 11/10/2026
 * Last Date Changed: 11/14/2026
 * Revision: 1.0
 */




// File > Order History window for viewing past orders and their items, editing the customer name, and deleting orders

package program;

import entity.Order;
import entity.OrderDAO;
import entity.OrderItem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class OrderHistoryDialog extends JDialog {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final OrderDAO orderDAO = new OrderDAO();
    private List<Order> orders;

    private final DefaultTableModel ordersModel = new DefaultTableModel(
            new String[]{"Transaction ID", "Date / Time", "Customer", "Total"}, 0);
    private final JTable ordersTable = new JTable(ordersModel);
    private final DefaultTableModel itemsModel = new DefaultTableModel(
            new String[]{"Item", "Options", "Qty", "Price"}, 0);
    private final JTable itemsTable = new JTable(itemsModel);

    public OrderHistoryDialog(Frame owner) {
        super(owner, "Order History", true);

        ordersTable.setDefaultEditor(Object.class, null);
        itemsTable.setDefaultEditor(Object.class, null);
        ordersTable.getSelectionModel().addListSelectionListener(e -> loadItems());

        JButton editCustomer = new JButton("Edit Customer");
        JButton delete = new JButton("Delete Order");
        editCustomer.addActionListener(e -> editCustomer());
        delete.addActionListener(e -> deleteOrder());

        JPanel buttons = new JPanel();
        buttons.add(editCustomer);
        buttons.add(delete);

        JPanel tables = new JPanel(new GridLayout(2, 1, 0, 8));
        tables.add(new JScrollPane(ordersTable));
        tables.add(new JScrollPane(itemsTable));

        setLayout(new BorderLayout());
        add(tables, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
        setSize(800, 500);
        setLocationRelativeTo(owner);
        loadOrders();
    }

    private void loadOrders() {
        orders = orderDAO.getAll();
        ordersModel.setRowCount(0);
        for (Order o : orders) {
            ordersModel.addRow(new Object[]{o.getTransactionId(), o.getOrderDateTime().format(DATE_FORMAT),
                    o.getCustomerName(), Main.money(o.getOrderTotal())});
        }
        itemsModel.setRowCount(0);
    }

    private void loadItems() {
        itemsModel.setRowCount(0);
        int row = ordersTable.getSelectedRow();
        if (row < 0) {
            return;
        }
        for (OrderItem item : orderDAO.getItems(orders.get(row).getTransactionId())) {
            itemsModel.addRow(new Object[]{item.getItemName(), item.getOptions(), item.getQuantity(),
                    Main.money(item.getLineTotal())});
        }
    }

    private void editCustomer() {
        Order order = selectedOrder();
        if (order == null) {
            return;
        }
        String name = JOptionPane.showInputDialog(this, "Customer name:", order.getCustomerName());
        if (name != null && !name.isBlank()) {
            order.setCustomerName(name.trim());
            orderDAO.update(order);
            loadOrders();
        }
    }

    private void deleteOrder() {
        Order order = selectedOrder();
        if (order != null && JOptionPane.showConfirmDialog(this, "Delete order " + order.getTransactionId() + "?",
                "Delete", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            orderDAO.delete(order);
            loadOrders();
        }
    }

    private Order selectedOrder() {
        int row = ordersTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select an order first please.");
            return null;
        }
        return orderDAO.get(orders.get(row).getOrderId()).orElse(null);
    }
}