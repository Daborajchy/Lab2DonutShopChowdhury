
/**
 * Project: Lab2DonutShopChowdhury - OrderHistoryDialog
 * Purpose Details: Runs everything
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
import entity.Filling;
import entity.MenuDAO;
import entity.Order;
import entity.OrderDAO;
import entity.OrderItem;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private JPanel mainPanel;
    private JLabel jLabelTitle;
    private JLabel jLabelSubtitle;
    private JComboBox<Object> jComboBoxCategory;
    private JTextField jTextFieldSearch;
    private JComboBox<DonutFlavors> jComboBoxIcing;
    private JComboBox<Filling> jComboBoxFilling;
    private JList<Donut> jListMenu;
    private JSpinner jSpinnerQty;
    private JLabel jLabelUnit;
    private JButton jButtonAddToOrder;
    private JTable jTableOrder;
    private JLabel jLabelSubtotal;
    private JLabel jLabelTax;
    private JLabel jLabelTotal;
    private JButton jButtonClear;
    private JButton jButtonCheckout;

    private final MenuDAO menuDAO = new MenuDAO();
    private final OrderDAO orderDAO = new OrderDAO();
    private final List<OrderItem> orderItems = new ArrayList<>();
    private final DefaultTableModel orderModel = new DefaultTableModel(
            new String[]{"Item", "Options", "Qty", "Price", "Total"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    public Main() {
        setUpComponents();
        addListeners();
        loadMenu();
        refreshOrder();
    }

    private void setUpComponents() {
        jLabelTitle.setText("Oak Donuts");
        jLabelTitle.setFont(new Font("SansSerif", Font.BOLD, 30));
        jLabelSubtitle.setText("Menu & Order");

        DefaultComboBoxModel<Object> categories = new DefaultComboBoxModel<>();
        categories.addElement("All");
        for (Category c : Category.values()) {
            categories.addElement(c);
        }
        jComboBoxCategory.setModel(categories);
        jComboBoxIcing.setModel(new DefaultComboBoxModel<>(DonutFlavors.values()));
        jComboBoxFilling.setModel(new DefaultComboBoxModel<>(Filling.values()));
        jSpinnerQty.setModel(new SpinnerNumberModel(1, 1, 99, 1));
        jListMenu.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTableOrder.setModel(orderModel);
        jTableOrder.setRowHeight(24);
        jTableOrder.setFillsViewportHeight(true);
        jTableOrder.getColumnModel().getColumn(1).setPreferredWidth(220);
        jLabelUnit.setText("Unit: $0.00");
        jLabelTotal.setFont(new Font("SansSerif", Font.BOLD, 20));
    }

    private void addListeners() {
        jComboBoxCategory.addActionListener(e -> loadMenu());
        jTextFieldSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { loadMenu(); }
            public void removeUpdate(DocumentEvent e) { loadMenu(); }
            public void changedUpdate(DocumentEvent e) { loadMenu(); }
        });
        jListMenu.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showSelectedItem();
            }
        });
        jButtonAddToOrder.addActionListener(e -> addToOrder());
        jButtonClear.addActionListener(e -> {
            orderItems.clear();
            refreshOrder();
        });
        jButtonCheckout.addActionListener(e -> checkout());
    }

    private void loadMenu() {
        Object category = jComboBoxCategory.getSelectedItem();
        String search = jTextFieldSearch.getText().trim().toLowerCase();
        DefaultListModel<Donut> model = new DefaultListModel<>();
        for (Donut item : menuDAO.getInStock()) {
            boolean categoryMatch = !(category instanceof Category) || item.getCategory() == category;
            boolean searchMatch = search.isEmpty() || item.getName().toLowerCase().contains(search);
            if (categoryMatch && searchMatch) {
                model.addElement(item);
            }
        }
        jListMenu.setModel(model);
        if (!model.isEmpty()) {
            jListMenu.setSelectedIndex(0);
        } else {
            showSelectedItem();
        }
    }

    private void showSelectedItem() {
        Donut item = jListMenu.getSelectedValue();
        if (item == null) {
            jLabelUnit.setText("Unit: $0.00");
            return;
        }
        jLabelUnit.setText("Unit: " + money(item.getPrice()));
        boolean isDonut = item.getCategory() == Category.DONUTS;
        jComboBoxIcing.setEnabled(isDonut);
        jComboBoxFilling.setEnabled(isDonut);
    }

    private void addToOrder() {
        Donut item = jListMenu.getSelectedValue();
        if (item == null) {
            message("Pick an item from the menu first.");
            return;
        }
        String options = optionsText(item);
        int qty = (Integer) jSpinnerQty.getValue();
        orderItems.add(new OrderItem(item.getName(), options, qty, item.getPrice()));
        refreshOrder();
    }

    private String optionsText(Donut item) {
        if (item.getCategory() != Category.DONUTS) {
            return "-";
        }
        List<String> parts = new ArrayList<>();
        DonutFlavors icing = (DonutFlavors) jComboBoxIcing.getSelectedItem();
        Filling filling = (Filling) jComboBoxFilling.getSelectedItem();
        if (icing != null && icing != DonutFlavors.NONE) {
            parts.add("Icing: " + icing);
        }
        if (filling != null && filling != Filling.NONE) {
            parts.add("Filling: " + filling);
        }
        return parts.isEmpty() ? "-" : String.join(", ", parts);
    }

    private void refreshOrder() {
        orderModel.setRowCount(0);
        for (OrderItem line : orderItems) {
            orderModel.addRow(new Object[]{line.getItemName(), line.getOptions(), line.getQuantity(),
                    money(line.getUnitPrice()), money(line.getLineTotal())});
        }
        Order preview = new Order();
        preview.setItems(orderItems);
        preview.calculateTotal();
        jLabelSubtotal.setText(money(preview.getSubtotal()));
        jLabelTax.setText(money(preview.getTax()));
        jLabelTotal.setText(money(preview.getOrderTotal()));
    }

    private void checkout() {
        if (orderItems.isEmpty()) {
            message("The order is empty.");
            return;
        }
        String name = JOptionPane.showInputDialog(mainPanel, "Name for the order (optional):", "Checkout",
                JOptionPane.QUESTION_MESSAGE);
        if (name == null) {
            return;
        }
        Order order = new Order();
        order.setCustomerName(name.isBlank() ? "Walk-in" : name.trim());
        order.setItems(new ArrayList<>(orderItems));
        orderDAO.insert(order);
        if (order.getTransactionId() == null) {
            message("The order could not be saved. Check the console for the error.");
            return;
        }
        JOptionPane.showMessageDialog(mainPanel, "Order placed!\nTransaction ID: " + order.getTransactionId()
                + "\nTotal: " + money(order.getOrderTotal()), "Checkout", JOptionPane.INFORMATION_MESSAGE);
        orderItems.clear();
        refreshOrder();
    }

    private JMenuBar buildMenuBar(JFrame frame) {
        JMenuBar bar = new JMenuBar();


        JMenu file = new JMenu("File");
        JMenuItem manage = new JMenuItem("Manage Menu...");
        manage.addActionListener(e -> {
            new ManageMenuDialog(frame).setVisible(true);
            loadMenu();
        });
        JMenuItem history = new JMenuItem("Order History...");
        history.addActionListener(e -> new OrderHistoryDialog(frame).setVisible(true));
        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(e -> frame.dispose());
        file.add(manage);
        file.add(history);
        file.addSeparator();
        file.add(exit);

        JMenu help = new JMenu("User Manual");
        JMenuItem about = new JMenuItem("How to Use");
        about.addActionListener(e -> JOptionPane.showMessageDialog(frame,
                "Oak Donuts (OD) Menu & Ordering System\n\n"
                        + "1. MAKE SURE TO THANK DABORAJ FOR THE DONUTS, HE IS A PRO AT MAKING DONUTS\n"
                        + "2. Pick a category or you can search the menu\n"
                        + "3. Choose if you would like icing and filling for the donuts you selected\n"
                        + "4. Choose how much you want and click Add to Order after\n"
                        + "5. Click the Checkout option to save the order\n"
                        + "6. You can go to File > Manage Menu to CREATE, READ, UPDATE and DELETE items of your choice\n"
                        + "7. You can go to File > Order History to VIEW, EDIT and DELETE orders",
                "Help", JOptionPane.INFORMATION_MESSAGE));
        help.add(about);


        bar.add(file);
        bar.add(help);
        return bar;
    }

    static String money(double amount) {
        return String.format("$%.2f", amount);
    }

    private void message(String text) {
        JOptionPane.showMessageDialog(mainPanel, text, "Oak Donuts", JOptionPane.WARNING_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                DatabaseSetup.createTables();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Could not set up the database:\n" + ex.getMessage(),
                        "Oak Donuts", JOptionPane.ERROR_MESSAGE);
                return;
            }
            JFrame frame = new JFrame("Oak Donuts – Ordering");
            Main app = new Main();
            frame.setContentPane(app.mainPanel);
            frame.setJMenuBar(app.buildMenuBar(frame));
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1150, 650);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });

    }

    private void createUIComponents() {

    }
}


