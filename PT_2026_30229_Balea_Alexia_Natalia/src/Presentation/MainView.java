package Presentation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Fereasta principala a aplicatiei.
 *
 * MainView gestioneaza:
 *      cele 3 tab uri (Clients, Products si Orders)
 *      toate componentele UI necesare pentru introducerea datelor
 *      tabelele de afisare
 *      log urile de activiatte
 *      butoanele folosite de Controller
 *
 * Aceasta clasa NU contine logica de business, DOAR interfata grafica
 */
public class MainView extends JFrame {
    private JTabbedPane tabbedPane; /** TabbedPane-ul principal care contine cele trei sectiuni*/

    /** Culorile folosite in UI*/
    private final Color titleColor = new Color(76, 95, 77);
    private final Color resultsBackgroundColor = new Color(255, 255, 255);
    private final Color background = new Color(241, 243, 238);
    private final Color btnColor = new Color(142, 166, 133);

    private JTextField clientIdField, clientNameField, clientAddressField, clientEmailField;
    private JButton btnAddClient, btnEditClient, btnDeleteClient, btnSearchClient;
    private JTable clientTable;
    private JTextArea clientLog;

    private JTextField prodIdField, prodNameField, prodQuantField, prodPriceField;
    private JButton btnAddProd, btnEditProd, btnDeleteProd, btnSearchProd;
    private JTable prodTable;
    private JTextArea prodLog;

    private JTextField orderQuantityField, orderIdField;
    private JComboBox<String> orderClientCombo, orderProductCombo;
    private JButton btnAddToCart, btnPlaceOrder, btnSearchOrder, btnClearCart;
    private JTextArea orderLog, cartArea;
    private JTable orderTable;

    /**
     * Constructorul principal.
     * Initializeaza fereastra, tab urile si panourile
     */
    public MainView(){
        setTitle("Agra's");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);
        getContentPane().setBackground(background);

        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(btnColor);

        tabbedPane.addTab("Clients", createClientPanel());
        tabbedPane.addTab("Products", createProductPanel());
        tabbedPane.addTab("Orders", createOrdersPanel());

        add(tabbedPane);
    }

    /**
     * Creeaza panoul pentru gestionarea clientilor
     * @return JPanel complet configurat
     */
    private JPanel createClientPanel(){
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(background);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel topContainer = createInputContainer();
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBackground(titleColor);

        clientIdField = new JTextField();
        clientNameField = new JTextField();
        clientAddressField = new JTextField();
        clientEmailField = new JTextField();

        addLabelAndComponent(inputPanel, "ID (edit/delete/search):", clientIdField);
        addLabelAndComponent(inputPanel, "Name:", clientNameField);
        addLabelAndComponent(inputPanel, "Address:", clientAddressField);
        addLabelAndComponent(inputPanel, "Email:", clientEmailField);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(titleColor);
        btnAddClient = createStyledButton("Add Client");
        btnEditClient = createStyledButton("Edit Client");
        btnDeleteClient = createStyledButton("Delete Client");
        btnSearchClient = createStyledButton("Search by ID");

        buttonPanel.add(btnAddClient);
        buttonPanel.add(btnEditClient);
        buttonPanel.add(btnDeleteClient);
        buttonPanel.add(btnSearchClient);

        topContainer.add(inputPanel, BorderLayout.NORTH);
        topContainer.add(buttonPanel, BorderLayout.SOUTH);

        clientTable = new JTable();
        clientTable.setBackground(resultsBackgroundColor);

        clientLog = createLogArea();

        panel.add(topContainer, BorderLayout.NORTH);
        panel.add(new JScrollPane(clientTable), BorderLayout.CENTER);
        panel.add(new JScrollPane(clientLog), BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Creeaza panoul pentru gestionarea produselor
     * @return JPanel complet configurat
     */
    private JPanel createProductPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(background);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel topContainer = createInputContainer();
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBackground(titleColor);

        prodIdField = new JTextField();
        prodNameField = new JTextField();
        prodQuantField = new JTextField();
        prodPriceField = new JTextField();

        addLabelAndComponent(inputPanel, "Product ID:", prodIdField);
        addLabelAndComponent(inputPanel, "Name:", prodNameField);
        addLabelAndComponent(inputPanel, "Quantity:", prodQuantField);
        addLabelAndComponent(inputPanel, "Price:", prodPriceField);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(titleColor);
        btnAddProd = createStyledButton("Add Product");
        btnEditProd = createStyledButton("Edit Product");
        btnDeleteProd = createStyledButton("Delete Product");
        btnSearchProd = createStyledButton("Search by ID");

        buttonPanel.add(btnAddProd);
        buttonPanel.add(btnEditProd);
        buttonPanel.add(btnDeleteProd);
        buttonPanel.add(btnSearchProd);

        topContainer.add(inputPanel, BorderLayout.NORTH);
        topContainer.add(buttonPanel, BorderLayout.SOUTH);

        prodTable = new JTable();
        prodTable.setBackground(resultsBackgroundColor);

        prodLog = createLogArea();

        panel.add(topContainer, BorderLayout.NORTH);
        panel.add(new JScrollPane(prodTable), BorderLayout.CENTER);
        panel.add(new JScrollPane(prodLog), BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Creeaza panoul pentru gestionarea comenzilor
     * @return JPanel complet configurat
     */
    private JPanel createOrdersPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(background);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel topContainer = createInputContainer();
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        inputPanel.setBackground(titleColor);

        orderIdField = new JTextField();
        orderClientCombo = new JComboBox<>();
        orderProductCombo = new JComboBox<>();
        orderQuantityField = new JTextField();

        addLabelAndComponent(inputPanel, "Order ID (Search):", orderIdField);
        addLabelAndComponent(inputPanel, "Select Client:", orderClientCombo);
        addLabelAndComponent(inputPanel, "Select Product:", orderProductCombo);
        addLabelAndComponent(inputPanel, "Quantity to Add:", orderQuantityField);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(titleColor);
        btnAddToCart = createStyledButton("Add Product to Cart");
        btnClearCart = createStyledButton("Clear Cart");
        btnPlaceOrder = createStyledButton("Place Final Order");
        btnSearchOrder = createStyledButton("Search Order");
        buttonPanel.add(btnAddToCart);
        buttonPanel.add(btnClearCart);
        buttonPanel.add(btnPlaceOrder);
        buttonPanel.add(btnSearchOrder);

        topContainer.add(inputPanel, BorderLayout.NORTH);
        topContainer.add(buttonPanel, BorderLayout.SOUTH);

        orderTable = new JTable();
        orderTable.setBackground(resultsBackgroundColor);

        JPanel centerPanel = new JPanel(new BorderLayout());
        cartArea = createLogArea();
        cartArea.setBorder(BorderFactory.createTitledBorder("Current Cart"));
        centerPanel.add(new JScrollPane(orderTable), BorderLayout.CENTER);
        centerPanel.add(new JScrollPane(cartArea), BorderLayout.WEST);

        orderLog = createLogArea();

        panel.add(topContainer, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(new JScrollPane(orderLog), BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Creeaza o zona de log needitabila
     * @return JTextArea configurata
     */
    private JTextArea createLogArea() {
        JTextArea log = new JTextArea(3, 50);
        log.setEditable(false);
        log.setBackground(btnColor);
        log.setFont(new Font("Monospaced", Font.PLAIN, 12));
        return log;
    }

    /**
     * creeaza un container pentru sectiunile de input
     * @return JPanel configurat
     */
    private JPanel createInputContainer() {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(titleColor);
        return container;
    }

    /**
     * Creeaza un container pentru sectiunile de input
     * @param panel (panoul in care se adauga)
     * @param text (textul etichetei)
     * @param comp (componenta asociata)
     */
    private void addLabelAndComponent(JPanel panel, String text, JComponent comp) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        panel.add(label);
        panel.add(comp);
    }

    /**
     * creeaza un buton stilizat
     * @param text (textul butonului)
     * @return JButton configurat
     */
    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(btnColor);
        return btn;
    }

    /**
     * afiseaza o fereastra de eroare
     * @param message (mesajul de afisat)
     */
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public String getClientId() { return clientIdField.getText(); }
    public String getClientName() { return clientNameField.getText(); }
    public String getClientAddress() { return clientAddressField.getText(); }
    public String getClientEmail() { return clientEmailField.getText(); }
    public JButton getBtnAddClient() { return btnAddClient; }
    public JButton getBtnEditClient() { return btnEditClient; }
    public JButton getBtnDeleteClient() { return btnDeleteClient; }
    public JButton getBtnSearchClient() { return btnSearchClient; }
    public void setClientTable(JTable table) { clientTable.setModel(table.getModel()); }
    public void setClientLog(String msg) { clientLog.append(msg + "\n"); }

    public String getProductId() { return prodIdField.getText(); }
    public String getProductName() { return prodNameField.getText(); }
    public String getProductQuantity() { return prodQuantField.getText(); }
    public String getProductPrice() { return prodPriceField.getText(); }
    public JButton getBtnAddProduct() { return btnAddProd; }
    public JButton getBtnEditProduct() { return btnEditProd; }
    public JButton getBtnDeleteProduct() { return btnDeleteProd; }
    public JButton getBtnSearchProduct() { return btnSearchProd; }
    public void setProductTable(JTable table) { prodTable.setModel(table.getModel()); }
    public void setProductLog(String msg) { prodLog.append(msg + "\n"); }

    public String getOrderId() { return orderIdField.getText(); }
    public String getOrderClient() { return (String) orderClientCombo.getSelectedItem(); }
    public String getOrderProduct() { return (String) orderProductCombo.getSelectedItem(); }
    public String getOrderQuantity() { return orderQuantityField.getText(); }
    public JButton getBtnAddToCart() { return btnAddToCart; }
    public JButton getBtnPlaceOrder() { return btnPlaceOrder; }
    public JButton getBtnSearchOrder() { return btnSearchOrder; }
    public void setOrderTable(JTable table) { orderTable.setModel(table.getModel());}
    public void setOrderLog(String msg) { orderLog.append(msg + "\n"); }
    public void appendCart(String msg) { cartArea.append(msg + "\n"); }
    public void clearCartArea() { cartArea.setText(""); }
    public JButton getBtnClearCart() { return btnClearCart; }

    public void setClientDropdown(String[] clients) {
        orderClientCombo.setModel(new DefaultComboBoxModel<>(clients));
    }

    public void setProductDropdown(String[] products) {
        orderProductCombo.setModel(new DefaultComboBoxModel<>(products));
    }
}