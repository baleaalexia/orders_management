package BusinessLayer;

import DataAccesLayer.*;
import Model.*;

import java.util.Date;
import java.util.List;

/**
 * clasa responsabila pentru logica de business asociata comenzilor
 * Aceasta gestioneaza:
 *      crearea comenzilor
 *      verificarea stocului
 *      inserarea elementelor de comanda
 *      generarea facturii
 * aceasta clasa reprezinta nivelul intermediar dintre interfata grafica si
 * nivelul de acces la date
 */
public class OrderBLL {
    private ProductDAO productDAO; /** DAO pentru tabela Orders */
    private OrdersDAO orderDAO; /** DAO pentru tabela OrderItem*/
    private OrderItmDAO orderItemDAO; /** DAO pentru tabela Bill*/
    private DataAccesLayer.BillDAO billDAO; /** Logica de business pentru produse (folosita la verificarea stocului)*/
    private ClientDAO clientDAO; /** logica de business pentru clienti*/

    /**
     * initializeaza toate componentele necesare pentru procesarea comenzilor
     */
    public OrderBLL(){
        productDAO = new ProductDAO();
        orderDAO = new OrdersDAO();
        orderItemDAO = new OrderItmDAO();
        billDAO = new DataAccesLayer.BillDAO();
        clientDAO = new ClientDAO();
    }

    /**
     * creeaza o comanda noua, verifica stocul, insereaza OrderItem-urile
     * si genereaza factura corespunzatoare
     * @param clientId (id ul clientului care plaseaza comanda)
     * @param cart (lisat de produse din cos)
     * @param productId (id ul produsului din cos)
     * @param quantity (cantitatea comandata)
     * @return factura generata pentru aceasta comanda
     * @throws Exception (daca stocul este insuficient sau datele sunt invalide)
     */
    public Bill createOrder(int clientId, List<OrderItem> cart, int productId, int quantity) throws Exception {
        if (cart == null || cart.isEmpty()) {
            throw new Exception("The cart is empty!");
        }

        Client client = clientDAO.findById(clientId);
        if (client == null) {
            throw new Exception("Client not found!");
        }

        double totalPrice = 0;
        Date currentDate = new Date();

        Orders newOrder = new Orders(0, clientId, productId, quantity, currentDate);
        Orders insertedOrder = orderDAO.insert(newOrder);

        int generatedOrderId = insertedOrder.getId();

        for (OrderItem item : cart) {
            Product p = productDAO.findById(item.getProductId());
            if (p == null) {
                throw new Exception("Product ID " + item.getProductId() + " not found!");
            }
            if (item.getQuantity() > p.getStock()) {
                throw new Exception("Not enough stock for " + p.getName() + "!");
            }

            p.setStock(p.getStock() - item.getQuantity());
            productDAO.update(p);

            item.setOrderId(generatedOrderId);
            orderItemDAO.insert(item);

            totalPrice += (item.getQuantity() * p.getPrice());
        }

        Bill newBill = new Bill(generatedOrderId, clientId, totalPrice, currentDate, client.getAddress());
        newBill = billDAO.insert(newBill);

        return newBill;
    }

    /**
     * cauta o comanda dupa ID
     * @param id (id ul comenzii)
     * @return obiectul Orders gasit
     */
    public Orders findOrderById(int id) {
        Orders order = orderDAO.findById(id);
        if (order == null) {
            throw new java.util.NoSuchElementException("Order with id " + id + " was not found!");
        }
        return order;
    }
}
