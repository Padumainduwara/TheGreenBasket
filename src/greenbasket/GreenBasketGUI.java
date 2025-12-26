package greenbasket;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class GreenBasketGUI extends JFrame {

    // --- COLOR PALETTE ---
    final Color PRIMARY_COLOR = new Color(39, 174, 96); // Green
    final Color ACCENT_COLOR = new Color(46, 204, 113); // Light Green
    final Color BG_COLOR = new Color(245, 245, 245); // Background
    final Color TEXT_COLOR = new Color(50, 50, 50); // Text
    final Color ALERT_COLOR = new Color(231, 76, 60); // Red
    final Color EDIT_COLOR = new Color(52, 152, 219); // Blue
    final Font MAIN_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 14);

    // --- DATA ---
    static ArrayList<Product> productList = new ArrayList<>();
    static ArrayList<User> userList = new ArrayList<>();
    static File productFile = new File("products.txt");
    static File userFile = new File("users.txt");

    // --- GUI COMPONENTS ---
    private CardLayout cardLayout = new CardLayout();
    private JPanel mainPanel = new JPanel(cardLayout);
    private JTabbedPane tabbedPane = new JTabbedPane();
    private DefaultTableModel tableModel; 
    private DefaultTableModel userModel;
    
    // **FIXED:** Dashboard Title Label made global to change text dynamically
    private JLabel lblBrand = new JLabel("The Green Basket"); 

    // --- MAIN ---
    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
            UIManager.put("nimbusBase", new Color(39, 174, 96));
            UIManager.put("nimbusBlueGrey", new Color(240, 240, 240));
            UIManager.put("control", new Color(245, 245, 245));
        } catch (Exception e) {}

        loadData();
        initDefaultUsers();
        
        SwingUtilities.invokeLater(() -> new GreenBasketGUI().setVisible(true));
    }

    // --- CONSTRUCTOR ---
    public GreenBasketGUI() {
        setTitle("The Green Basket - Management System");
        setSize(1100, 750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel loginPanel = createLoginPanel();
        JPanel dashboardPanel = createDashboardPanel();

        mainPanel.add(loginPanel, "Login");
        mainPanel.add(dashboardPanel, "Dashboard");
        add(mainPanel);
    }

    // --- HELPER: BUTTONS ---
    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // --- HELPER: TABLE ---
    private void styleTable(JTable table) {
        table.setRowHeight(30);
        table.setFont(MAIN_FONT);
        table.getTableHeader().setFont(HEADER_FONT);
        table.getTableHeader().setBackground(new Color(230, 230, 230));
        table.setShowGrid(true);
        table.setGridColor(new Color(220, 220, 220));
    }

    // --- 1. LOGIN SCREEN ---
    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(40, 40, 40, 40)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("THE GREEN BASKET");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(PRIMARY_COLOR);
        title.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel lblSub = new JLabel("System Login");
        lblSub.setFont(MAIN_FONT);
        lblSub.setForeground(Color.GRAY);
        lblSub.setHorizontalAlignment(SwingConstants.CENTER);

        JTextField txtUser = new JTextField(20);
        JPasswordField txtPass = new JPasswordField(20);
        JButton btnLogin = createStyledButton("LOGIN", PRIMARY_COLOR);

        gbc.gridx = 0; gbc.gridy = 0; card.add(title, gbc);
        gbc.gridy = 1; card.add(lblSub, gbc);
        gbc.gridy = 2; card.add(new JLabel("Username"), gbc);
        gbc.gridy = 3; card.add(txtUser, gbc);
        gbc.gridy = 4; card.add(new JLabel("Password"), gbc);
        gbc.gridy = 5; card.add(txtPass, gbc);
        gbc.gridy = 6; gbc.insets = new Insets(20, 10, 10, 10); card.add(btnLogin, gbc);

        btnLogin.addActionListener(e -> {
            User user = authenticate(txtUser.getText(), new String(txtPass.getPassword()));
            if (user != null) {
                setupDashboard(user);
                cardLayout.show(mainPanel, "Dashboard");
                txtUser.setText(""); txtPass.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Credentials!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(card);
        return panel;
    }

    // --- 2. DASHBOARD FRAME ---
    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PRIMARY_COLOR);
        header.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        // **FIXED:** Use the global label variable
        lblBrand.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblBrand.setForeground(Color.WHITE);
        
        JButton btnLogout = createStyledButton("Logout", ALERT_COLOR);
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLogout.addActionListener(e -> {
            cardLayout.show(mainPanel, "Login");
            tabbedPane.removeAll();
        });

        header.add(lblBrand, BorderLayout.WEST);
        header.add(btnLogout, BorderLayout.EAST);
        
        tabbedPane.setFont(MAIN_FONT);
        panel.add(header, BorderLayout.NORTH);
        panel.add(tabbedPane, BorderLayout.CENTER);
        
        return panel;
    }

    private void setupDashboard(User user) {
        // **FIXED:** Update Title based on User Role
        lblBrand.setText("The Green Basket - " + user.getRole());
        
        tabbedPane.removeAll();
        tabbedPane.addTab("View Products", createViewProductsPanel());
        tabbedPane.addTab("Add Product", createAddProductPanel());
        tabbedPane.addTab("Search", createSearchPanel());

        if (user instanceof StoreManager) {
            tabbedPane.addTab("Manage Users", createCreateUserPanel());
            tabbedPane.addTab("Inventory", createInventoryCheckPanel());
        }
    }

    // --- TAB: VIEW PRODUCTS (CRUD) ---
    private JPanel createViewProductsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(BG_COLOR);

        String[] cols = {"ID", "Name", "Category", "Supplier", "Price", "Qty"};
        tableModel = new DefaultTableModel(cols, 0);
        JTable table = new JTable(tableModel);
        styleTable(table);
        refreshTable();
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(BG_COLOR);

        JButton btnQty = createStyledButton("Update Qty", ACCENT_COLOR); 
        JButton btnEdit = createStyledButton("Edit", EDIT_COLOR); 
        JButton btnDelete = createStyledButton("Delete", ALERT_COLOR); 
        JButton btnRefresh = createStyledButton("Refresh", PRIMARY_COLOR);
        
        btnQty.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) { JOptionPane.showMessageDialog(this, "Select a product!"); return; }
            String id = (String) tableModel.getValueAt(row, 0);
            Product p = findProduct(id);
            if (p != null) {
                String in = JOptionPane.showInputDialog(this, "New Quantity:", p.getQuantity());
                if (in != null) {
                    try { p.setQuantity(Integer.parseInt(in)); saveData(); refreshTable(); } 
                    catch (Exception ex) { JOptionPane.showMessageDialog(this, "Invalid Number!"); }
                }
            }
        });

        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) { JOptionPane.showMessageDialog(this, "Select a product!"); return; }
            String id = (String) tableModel.getValueAt(row, 0);
            Product p = findProduct(id);
            if (p != null) {
                JTextField txtName = new JTextField(p.getName());
                JTextField txtPrice = new JTextField(String.valueOf(p.getPrice()));
                JTextField txtSup = new JTextField(p.getSupplier());
                Object[] msg = { "Name:", txtName, "Price:", txtPrice, "Supplier:", txtSup };
                if (JOptionPane.showConfirmDialog(null, msg, "Edit", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                    try {
                        Product newP = new Product(p.getProductID(), txtName.getText(), p.getCategory(), txtSup.getText(), Double.parseDouble(txtPrice.getText()), p.getQuantity());
                        productList.set(productList.indexOf(p), newP);
                        saveData(); refreshTable();
                    } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error in inputs!"); }
                }
            }
        });

        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) { JOptionPane.showMessageDialog(this, "Select a product!"); return; }
            String id = (String) tableModel.getValueAt(row, 0);
            Product p = findProduct(id);
            if (JOptionPane.showConfirmDialog(this, "Delete " + p.getName() + "?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                productList.remove(p); saveData(); refreshTable();
            }
        });

        btnRefresh.addActionListener(e -> refreshTable());
        btnPanel.add(btnQty); btnPanel.add(btnEdit); btnPanel.add(btnDelete); btnPanel.add(btnRefresh);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);
        return panel;
    }

    // --- TAB: ADD PRODUCT ---
    private JPanel createAddProductPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BG_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JTextField txtID = new JTextField(20);
        JTextField txtName = new JTextField(20);
        String[] cats = {"Fresh Produce", "Dairy", "Beverages", "Snacks", "Cleaning Supplies"};
        JComboBox<String> cmbCat = new JComboBox<>(cats);
        JTextField txtSupplier = new JTextField(20);
        JTextField txtPrice = new JTextField(20);
        JTextField txtQty = new JTextField(20);
        JButton btnAdd = createStyledButton("Save Product", PRIMARY_COLOR);

        addFormRow(panel, gbc, 0, "ID:", txtID);
        addFormRow(panel, gbc, 1, "Name:", txtName);
        addFormRow(panel, gbc, 2, "Category:", cmbCat);
        addFormRow(panel, gbc, 3, "Supplier:", txtSupplier);
        addFormRow(panel, gbc, 4, "Price:", txtPrice);
        addFormRow(panel, gbc, 5, "Qty:", txtQty);
        gbc.gridx = 1; gbc.gridy = 6; gbc.fill = GridBagConstraints.HORIZONTAL; panel.add(btnAdd, gbc);

        btnAdd.addActionListener(e -> {
            if (txtID.getText().isEmpty() || txtName.getText().isEmpty()) { JOptionPane.showMessageDialog(this, "Empty Fields!"); return; }
            for (Product p : productList) if (p.getProductID().equals(txtID.getText())) { JOptionPane.showMessageDialog(this, "Duplicate ID!"); return; }
            try {
                productList.add(new Product(txtID.getText(), txtName.getText(), (String)cmbCat.getSelectedItem(), txtSupplier.getText(), Double.parseDouble(txtPrice.getText()), Integer.parseInt(txtQty.getText())));
                saveData(); refreshTable(); JOptionPane.showMessageDialog(this, "Saved!");
                txtID.setText(""); txtName.setText(""); txtSupplier.setText("");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Invalid Numbers!"); }
        });
        return panel;
    }

    private void addFormRow(JPanel p, GridBagConstraints g, int y, String lbl, JComponent comp) {
        g.gridx = 0; g.gridy = y; p.add(new JLabel(lbl), g); g.gridx = 1; p.add(comp, g);
    }

    // --- TAB: SEARCH ---
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_COLOR);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel top = new JPanel(new FlowLayout());
        top.setBackground(BG_COLOR);
        String[] cats = {"Fresh Produce", "Dairy", "Beverages", "Snacks", "Cleaning Supplies"};
        JComboBox<String> cmbSearch = new JComboBox<>(cats);
        JButton btnSearch = createStyledButton("Search", ACCENT_COLOR);

        top.add(new JLabel("Category: ")); top.add(cmbSearch); top.add(btnSearch);

        DefaultTableModel searchModel = new DefaultTableModel(new String[]{"ID", "Name", "Category", "Supplier", "Price"}, 0);
        JTable searchTable = new JTable(searchModel);
        styleTable(searchTable);

        btnSearch.addActionListener(e -> {
            searchModel.setRowCount(0);
            boolean found = false;
            for (Product p : productList) {
                if (p.getCategory().equalsIgnoreCase((String)cmbSearch.getSelectedItem())) {
                    searchModel.addRow(p.toRowData()); found = true;
                }
            }
            if (!found) JOptionPane.showMessageDialog(this, "No items found!");
        });

        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(searchTable), BorderLayout.CENTER);
        return panel;
    }

    // --- TAB: MANAGE USERS ---
    private JPanel createCreateUserPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_COLOR);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(BG_COLOR);
        formPanel.setBorder(BorderFactory.createTitledBorder("Create New Assistant"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        JTextField txtU = new JTextField(15);
        JTextField txtP = new JTextField(15);
        JButton btnCreate = createStyledButton("Create", PRIMARY_COLOR);
        
        addFormRow(formPanel, gbc, 0, "Username:", txtU);
        addFormRow(formPanel, gbc, 1, "Password:", txtP);
        gbc.gridx = 1; gbc.gridy = 2; formPanel.add(btnCreate, gbc);

        String[] cols = {"Username", "Role"};
        userModel = new DefaultTableModel(cols, 0);
        JTable userTable = new JTable(userModel);
        styleTable(userTable);
        
        Runnable refreshUsers = () -> {
            userModel.setRowCount(0);
            for (User u : userList) userModel.addRow(new Object[]{u.getUsername(), u.getRole()});
        };
        refreshUsers.run();

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(BG_COLOR);
        JButton btnDeleteUser = createStyledButton("Delete Selected User", ALERT_COLOR);
        btnPanel.add(btnDeleteUser);

        btnCreate.addActionListener(e -> {
            if(txtU.getText().isEmpty() || txtP.getText().isEmpty()) { JOptionPane.showMessageDialog(this, "Empty!"); return; }
            for (User u : userList) if(u.getUsername().equals(txtU.getText())) { JOptionPane.showMessageDialog(this, "Exists!"); return; }
            userList.add(new SalesAssistant(txtU.getText(), txtP.getText()));
            saveData(); refreshUsers.run(); JOptionPane.showMessageDialog(this, "User Created!");
            txtU.setText(""); txtP.setText("");
        });

        btnDeleteUser.addActionListener(e -> {
            int row = userTable.getSelectedRow();
            if(row == -1) { JOptionPane.showMessageDialog(this, "Select a user!"); return; }
            String uName = (String) userModel.getValueAt(row, 0);
            String uRole = (String) userModel.getValueAt(row, 1);
            
            if (uRole.equals("Store Manager")) { JOptionPane.showMessageDialog(this, "Cannot delete Manager!"); return; }
            
            if(JOptionPane.showConfirmDialog(this, "Delete " + uName + "?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                userList.removeIf(u -> u.getUsername().equals(uName));
                saveData(); refreshUsers.run(); JOptionPane.showMessageDialog(this, "Deleted!");
            }
        });

        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(userTable), BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);
        return panel;
    }

    // --- TAB: INVENTORY ---
    private JPanel createInventoryCheckPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_COLOR);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JButton btnCheck = createStyledButton("Run Inventory Scan", ALERT_COLOR);
        DefaultTableModel alertModel = new DefaultTableModel(new String[]{"ID", "Name", "Qty", "Status"}, 0);
        JTable alertTable = new JTable(alertModel);
        styleTable(alertTable);
        
        alertTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v, boolean isSel, boolean hasFoc, int r, int c) {
                Component comp = super.getTableCellRendererComponent(t, v, isSel, hasFoc, r, c);
                comp.setForeground(Color.RED); comp.setFont(new Font("Segoe UI", Font.BOLD, 14)); return comp;
            }
        });

        btnCheck.addActionListener(e -> {
            alertModel.setRowCount(0);
            boolean low = false;
            for (Product p : productList) {
                if (p.getQuantity() < 5) {
                    alertModel.addRow(new Object[]{p.getProductID(), p.getName(), p.getQuantity(), "LOW STOCK!"});
                    low = true;
                }
            }
            if (!low) JOptionPane.showMessageDialog(this, "All Stock Levels Good! ✅");
        });

        panel.add(btnCheck, BorderLayout.NORTH);
        panel.add(new JScrollPane(alertTable), BorderLayout.CENTER);
        return panel;
    }

    // --- HELPERS ---
    private Product findProduct(String id) { for (Product p : productList) if (p.getProductID().equals(id)) return p; return null; }
    private User authenticate(String u, String p) { for (User user : userList) if (user.login(u, p)) return user; return null; }
    private void refreshTable() { if(tableModel!=null) { tableModel.setRowCount(0); for (Product p : productList) tableModel.addRow(p.toRowData()); } }
    private static void saveData() { try { new ObjectOutputStream(new FileOutputStream(productFile)).writeObject(productList); new ObjectOutputStream(new FileOutputStream(userFile)).writeObject(userList); } catch (Exception e) {} }
    private static void loadData() { try { if (productFile.exists()) productList = (ArrayList<Product>) new ObjectInputStream(new FileInputStream(productFile)).readObject(); if (userFile.exists()) userList = (ArrayList<User>) new ObjectInputStream(new FileInputStream(userFile)).readObject(); } catch (Exception e) {} }
    private static void initDefaultUsers() { if (userList.isEmpty()) { userList.add(new StoreManager("admin", "123")); userList.add(new SalesAssistant("user", "123")); } }
}