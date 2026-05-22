package Presentation;

import BusinessLayer.ClientBLL;
import BusinessLayer.OrderBLL;
import BusinessLayer.OrderItemBLL;
import BusinessLayer.ProductBLL;
import Model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Controller ul principal al aplicatiei
 *
 * Gestioneaza:
 *      interactiunea dintre interfata grafica (MainView) si logica de business
 *      evenimentele generate deutilizator (butoane, selectii, campuri)
 *      actualizarea tabelelor, drop down-urilor si jurnalelor de activitati
 *      gestionarea cosului de cumparaturi si plasarea comenzilor
 *
 * Controller ul este responsabil pentru fluxul complet:
 *  Client -> Produs -> Cos -> Comanda -> Factura
 */
public class Controller {
    private MainView view; /** Referinta catre fereastra principala */
    private ClientBLL clientBLL; /** Logica de business pentru clienti*/
    private ProductBLL productBLL;/** Logica de business pentru produse*/
    private OrderBLL orderBLL;/** logica de business pentru comenzi*/
    private OrderItemBLL orderItemBLL; /** logica de business pentru elementele de comanda*/
    private DataAccesLayer.BillDAO billDAO; /** DAO pentru facturi*/
    private List<OrderItem> shoppingCart; /** Cosul de cumparaturi curent*/

    /**
     * Creeaza controller ul si initializeaza toate componentele necesare
     * In plus, ataseaza listener-ele si incarca datele initiale in UI
     * @param view (interfata grafica principala)
     */
    public Controller(MainView view) {
        this.view = view;
        this.clientBLL = new ClientBLL();
        this.productBLL = new ProductBLL();
        this.orderBLL = new OrderBLL();
        this.orderItemBLL = new OrderItemBLL();
        this.billDAO = new DataAccesLayer.BillDAO();
        this.shoppingCart = new ArrayList<>();

        initListeners();

        refreshClientTable();
        refreshProductTable();
        refreshDropdowns();
        refreshOrderTable();
    }

    /**
     * initializeaza toate listener ele pentru butoanele din interfata
     * Fiecare listener gestioneaza un flux specific: CRUD pentru clienti,
     * CRUD pentru produse, gestioneaza cosul si plasarea comenzilor
     */
    private void initListeners() {
        view.getBtnAddClient().addActionListener(e -> {
            try {
                String name = view.getClientName();
                String address = view.getClientAddress();
                String email = view.getClientEmail();
                Client client = new Client(0, name, address, email);
                clientBLL.insertClient(client);
                refreshClientTable();
                refreshDropdowns();
                view.setClientLog("SUCCESS: Added client " + name);
            } catch (Exception ex) {
                view.showError(ex.getMessage());
            }
        });

        view.getBtnEditClient().addActionListener(e -> {
            try {
                int id = Integer.parseInt(view.getClientId());
                String name = view.getClientName();
                String address = view.getClientAddress();
                String email = view.getClientEmail();
                Client client = new Client(id, name, address, email);
                clientBLL.updateClient(client);
                refreshClientTable();
                refreshDropdowns();
                view.setClientLog("SUCCESS: Updated client ID " + id);
            } catch (NumberFormatException ex) {
                view.showError("Invalid Client ID format!");
            } catch (Exception ex) {
                view.showError(ex.getMessage());
            }
        });

        view.getBtnDeleteClient().addActionListener(e -> {
            try {
                int id = Integer.parseInt(view.getClientId());
                clientBLL.deleteClient(id);
                refreshClientTable();
                refreshDropdowns();
                view.setClientLog("SUCCESS: Deleted client ID " + id);
            } catch (NumberFormatException ex) {
                view.showError("Invalid Client ID format!");
            } catch (Exception ex) {
                view.showError(ex.getMessage());
            }
        });

        view.getBtnSearchClient().addActionListener(e -> {
            try {
                int id = Integer.parseInt(view.getClientId());
                Client c = clientBLL.findClientById(id);
                String msg = "ID: " + c.getId() +
                        "\nName: " + c.getName() +
                        "\nAddress: " + c.getAddress() +
                        "\nEmail: " + c.getEmail();
                JOptionPane.showMessageDialog(view, msg, "Client Found", JOptionPane.INFORMATION_MESSAGE);
                view.setClientLog("SEARCH: Found client " + c.getName());
            } catch (Exception ex) {
                view.showError(ex.getMessage());
            }
        });

        view.getBtnAddProduct().addActionListener(e -> {
            try {
                String name = view.getProductName();
                int quantity = Integer.parseInt(view.getProductQuantity());
                double price = Double.parseDouble(view.getProductPrice());
                Product product = new Product(0, name, price, quantity);
                productBLL.insertProduct(product);
                refreshProductTable();
                refreshDropdowns();
                view.setProductLog("SUCCESS: Added product " + name);
            } catch (Exception ex) {
                view.showError(ex.getMessage());
            }
        });

        view.getBtnEditProduct().addActionListener(e -> {
            try {
                int id = Integer.parseInt(view.getProductId());
                String name = view.getProductName();
                int quantity = Integer.parseInt(view.getProductQuantity());
                double price = Double.parseDouble(view.getProductPrice());
                Product product = new Product(id, name, price, quantity);
                productBLL.updateProduct(product);
                refreshProductTable();
                refreshDropdowns();
                view.setProductLog("SUCCESS: Updated product ID " + id);
            } catch (Exception ex) {
                view.showError(ex.getMessage());
            }
        });

        view.getBtnDeleteProduct().addActionListener(e -> {
            try {
                int id = Integer.parseInt(view.getProductId());
                productBLL.deleteProduct(id);
                refreshProductTable();
                refreshDropdowns();
                view.setProductLog("SUCCESS: Deleted product ID " + id);
            } catch (Exception ex) {
                view.showError(ex.getMessage());
            }
        });

        view.getBtnSearchProduct().addActionListener(e -> {
            try {
                int id = Integer.parseInt(view.getProductId());
                Product p = productBLL.findProductById(id);
                String msg = "ID: " + p.getId() +
                        "\nName: " + p.getName() +
                        "\nStock: " + p.getStock() +
                        "\nPrice: " + p.getPrice();
                JOptionPane.showMessageDialog(view, msg, "Product Found", JOptionPane.INFORMATION_MESSAGE);
                view.setProductLog("SEARCH: Found product " + p.getName());
            } catch (Exception ex) {
                view.showError(ex.getMessage());
            }
        });

        view.getBtnAddToCart().addActionListener(e -> {
            try {
                String selectedProduct = view.getOrderProduct();
                if (selectedProduct == null) throw new Exception("No product selected.");

                int productId = Integer.parseInt(selectedProduct.split(" - ")[0]);
                String productName = selectedProduct.split(" - ")[1].split(" ")[0];
                int quantity = Integer.parseInt(view.getOrderQuantity());

                OrderItem item = new OrderItem(0, 0, productId, quantity);
                shoppingCart.add(item);
                view.appendCart("- " + productName + " (x" + quantity + ")");
                view.setOrderLog("Added " + quantity + " " + productName + "(s) to cart.");
            } catch (Exception ex) {
                view.showError(ex.getMessage());
            }
        });

        view.getBtnClearCart().addActionListener(e -> {
            shoppingCart.clear();
            view.clearCartArea();
            view.setOrderLog("Cart has been cleared.");
        });

        view.getBtnPlaceOrder().addActionListener(e -> {
            try {
                String selectedClient = view.getOrderClient();
                String selectedProduct = view.getOrderProduct();
                String quantityStr = view.getOrderQuantity();

                if (selectedClient == null) throw new Exception("Please select a client.");
                if (selectedProduct == null) throw new Exception("Please select a product.");
                if (quantityStr.isEmpty()) throw new Exception("Please enter quantity.");

                int clientId = Integer.parseInt(selectedClient.split(" - ")[0]);
                int productId = Integer.parseInt(selectedProduct.split(" - ")[0]);
                int quantity = Integer.parseInt(quantityStr);

                Bill bill = orderBLL.createOrder(clientId, shoppingCart, productId, quantity);

                view.setOrderLog("ORDER SUCCESS: Bill #" + bill.id() + " | Total: " + bill.totalPrice());
                shoppingCart.clear();
                view.clearCartArea();

                refreshProductTable();
                refreshDropdowns();
                refreshOrderTable();
            } catch (NumberFormatException ex) {
                view.showError("Invalid number format in quantity!");
            } catch (Exception ex) {
                view.showError(ex.getMessage());
            }
        });

        view.getBtnSearchOrder().addActionListener(e -> {
            try {
                int id = Integer.parseInt(view.getOrderId());

                Orders o = orderBLL.findOrderById(id);
                Client c = clientBLL.findClientById(o.getClientId());

                StringBuilder info = new StringBuilder();
                info.append("Order ID: ").append(o.getId()).append("\n");
                info.append("Placed by: ").append(c.getName()).append("\n");
                info.append("----------------------------------\n");
                info.append("Products in this Order:\n");

                List<OrderItem> items = orderItemBLL.findItemsByOrderId(o.getId());

                if (items.isEmpty()) {
                    info.append("No products found.\n");
                } else {
                    for (OrderItem item : items) {
                        Product p = productBLL.findProductById(item.getProductId());
                        info.append("- ").append(p.getName())
                                .append(" (").append(p.getPrice()).append(") x ")
                                .append(item.getQuantity()).append("\n");
                    }
                }

                JOptionPane.showMessageDialog(view, info.toString(), "Order Details", JOptionPane.INFORMATION_MESSAGE);
                view.setOrderLog("SEARCH: Found Order ID " + id);

            } catch (Exception ex) {
                view.showError(ex.getMessage());
            }
        });
    }

    /**
     * reincarca tabelul cu clienti in interfata
     */
    private void refreshClientTable() {
        List<Client> clients = clientBLL.findAllClients();
        view.setClientTable(createTableFromObjects(clients));
    }

    /**
     * reincarca tabelul cu produse in interfata
     */
    private void refreshProductTable() {
        List<Product> products = productBLL.findAllProducts();
        view.setProductTable(createTableFromObjects(products));
    }

    /**
     * reincarca tabelul cu comenzi in interfata
     */
    private void refreshOrderTable() {
        List<Bill> bills = billDAO.findAll();
        view.setOrderTable(createTableFromObjects(bills));
    }

    /**
     * reincarca dropdown-urile pentru clienti si produse
     */
    private void refreshDropdowns() {
        List<Client> clients = clientBLL.findAllClients();
        String[] clientItems = clients.stream()
                .map(c -> c.getId() + " - " + c.getName())
                .toArray(String[]::new);
        view.setClientDropdown(clientItems);

        List<Product> products = productBLL.findAvailableProducts();
        String[] productItems = products.stream()
                .map(p -> p.getId() + " - " + p.getName() + " (" + p.getPrice() + ")")
                .toArray(String[]::new);
        view.setProductDropdown(productItems);
    }

    /**
     * creeaza un JTable pe baza unei liste de obiecte folosind reflectia
     * @param objects (lista de obiecte de afisat)
     * @return un JTable complet populat
     * @param <T> (tipul obiectelor)
     */
    private <T> JTable createTableFromObjects(List<T> objects) {
        if (objects == null || objects.isEmpty()) {
            return new JTable(new DefaultTableModel());
        }

        Class<?> type = objects.get(0).getClass();
        Field[] fields = type.getDeclaredFields();
        String[] columnNames = Arrays.stream(fields).map(Field::getName).toArray(String[]::new);

        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (T obj : objects) {
            Object[] row = new Object[fields.length];
            for (int i = 0; i < fields.length; i++) {
                try {
                    fields[i].setAccessible(true);
                    row[i] = fields[i].get(obj);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            model.addRow(row);
        }
        return new JTable(model);
    }

}
