

/**
 * Project: Lab2DonutShopChowdhury  Oak Donuts (OD) - Donut Shop Menu & Ordering System - ManageMenuDialog
 * Purpose Details: File > Manage Menu window for adding, updating, deleting and marking menu items in or out of
 * Course: CMPSC 221
 * Author: Daboraj Chowdhury
 * Date Developed: 11/10/2026
 * Last Date Changed: 11/14/2026
 * Revision: 1.0
 */


package program;

import entity.Category;
import entity.Donut;
import entity.DonutFlavors;
import entity.MenuDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ManageMenuDialog extends JDialog {

    private final MenuDAO menuDAO = new MenuDAO();
    private List<Donut> items = new ArrayList<>();

    private final JTextField idField = new JTextField(12);
    private final JTextField nameField = new JTextField(12);
    private final JComboBox<Category> categoryBox = new JComboBox<>(Category.values());
    private final JComboBox<DonutFlavors> flavorBox = new JComboBox<>(DonutFlavors.values());
    private final JTextField priceField = new JTextField(12);
    private final JCheckBox inStockBox = new JCheckBox("In Stock", true);
    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"ID", "Item", "Category", "Icing", "Price", "In Stock"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = new JTable(model);

    public ManageMenuDialog(Frame owner) {
        super(owner, "Manage Menu", true);
        idField.setEditable(false);

        JPanel form = new JPanel(new GridLayout(0, 2, 6, 6));
        form.setBorder(BorderFactory.createTitledBorder("Menu Item"));
        form.add(new JLabel("ID:"));
        form.add(idField);
        form.add(new JLabel("Name:"));
        form.add(nameField);
        form.add(new JLabel("Category:"));
        form.add(categoryBox);
        form.add(new JLabel("Default Icing:"));
        form.add(flavorBox);
        form.add(new JLabel("Price ($):"));
        form.add(priceField);
        form.add(new JLabel(""));
        form.add(inStockBox);

        JButton add = new JButton("Add");
        JButton update = new JButton("Update");
        JButton delete = new JButton("Delete");
        JButton clear = new JButton("Clear");
        add.addActionListener(e -> addItem());
        update.addActionListener(e -> updateItem());
        delete.addActionListener(e -> deleteItem());
        clear.addActionListener(e -> clearForm());
        JPanel buttons = new JPanel(new GridLayout(2, 2, 6, 6));
        buttons.add(add);
        buttons.add(update);
        buttons.add(delete);
        buttons.add(clear);

        JPanel left = new JPanel(new BorderLayout(0, 10));
        left.add(form, BorderLayout.NORTH);
        left.add(buttons, BorderLayout.CENTER);

        table.setRowHeight(24);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                fillForm();
            }
        });
        categoryBox.addActionListener(e -> flavorBox.setEnabled(categoryBox.getSelectedItem() == Category.DONUTS));

        JPanel content = new JPanel(new BorderLayout(10, 10));
        content.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        content.add(left, BorderLayout.WEST);
        content.add(new JScrollPane(table), BorderLayout.CENTER);
        setContentPane(content);
        setSize(900, 450);
        setLocationRelativeTo(owner);
        loadTable();
    }

    private void loadTable() {
        items = menuDAO.getAll();
        model.setRowCount(0);
        for (Donut d : items) {
            model.addRow(new Object[]{d.getId(), d.getName(), d.getCategory(), d.getDonutFlavors(),
                    Main.money(d.getPrice()), d.isInStock() ? "Yes" : "Sold out"});
        }
    }

    private void addItem() {
        Donut item = readForm();
        if (item != null) {
            menuDAO.insert(item);
            clearForm();
            loadTable();
        }
    }

    private void updateItem() {
        if (idField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select an item in the table first.");
            return;
        }
        Donut item = readForm();
        if (item != null) {
            item.setId(Integer.parseInt(idField.getText()));
            menuDAO.update(item);
            loadTable();
        }
    }

    private void deleteItem() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select an item in the table first.");
            return;
        }
        Donut item = items.get(row);
        int answer = JOptionPane.showConfirmDialog(this, "Delete " + item.getName() + " from the menu?",
                "Delete", JOptionPane.YES_NO_OPTION);
        if (answer == JOptionPane.YES_OPTION) {
            menuDAO.delete(item);
            clearForm();
            loadTable();
        }
    }

    private Donut readForm() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a name.");
            return null;
        }
        double price;
        try {
            price = Double.parseDouble(priceField.getText().trim().replace("$", ""));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Price must be a number, like 1.99");
            return null;
        }
        if (price < 0) {
            JOptionPane.showMessageDialog(this, "Price can't be negative.");
            return null;
        }
        Category category = (Category) categoryBox.getSelectedItem();
        DonutFlavors flavor = category == Category.DONUTS ? (DonutFlavors) flavorBox.getSelectedItem() : DonutFlavors.NONE;
        return new Donut(name, category, flavor, price, inStockBox.isSelected());
    }

    private void fillForm() {
        int row = table.getSelectedRow();
        if (row < 0) {
            return;
        }
        Donut d = items.get(row);
        idField.setText(String.valueOf(d.getId()));
        nameField.setText(d.getName());
        categoryBox.setSelectedItem(d.getCategory());
        flavorBox.setSelectedItem(d.getDonutFlavors());
        priceField.setText(String.format("%.2f", d.getPrice()));
        inStockBox.setSelected(d.isInStock());
    }

    private void clearForm() {
        table.clearSelection();
        idField.setText("");
        nameField.setText("");
        categoryBox.setSelectedIndex(0);
        flavorBox.setSelectedIndex(0);
        priceField.setText("");
        inStockBox.setSelected(true);
    }
}