package BusinessLayer;

import DataAccesLayer.OrderItmDAO;
import Model.OrderItem;

import java.util.List;

/**
 * clasa responsabila pentru logica de business asociata elementelor de
 * comanda
 * Gestioneaza operatiile CRUD si intermediaza comunicarea dintre interfata grafica
 * si nivelul de acces la date
 *
 * Un OrderItem reprezinta un produs si cantitatea comandata din acel produs,
 * fiind parte componenta a unei comenzi
 */
public class OrderItemBLL {
    private OrderItmDAO orderItemDAO; /** DAO utilizata pentru operatiile asupra tabelei OrderItem*/

    /**
     * initializeaza logica de business si creeaza instanta DAO necesara
     */
    public OrderItemBLL(){
        this.orderItemDAO = new OrderItmDAO();
    }

    /**
     * cauta un element de comanda dupa id
     * @param orderId (id ul OrderItem ului cautat)
     * @return obiectul OrderItem gasit
     */
    public List<OrderItem> findItemsByOrderId(int orderId) {
        return orderItemDAO.findItemsByOrderId(orderId);
    }

}
