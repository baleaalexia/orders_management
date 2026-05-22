package DataAccesLayer;

import Model.OrderItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import Connection.ConnectionFactory;

/**
 * Clasa dedicata entitatii OrderItem
 * Extinde AbstractDao pentru a mosteni operatiile CRUD generice:
 *      inserare
 *      cautare dupa ID
 *      listare completa
 *      actualizare
 *      stergere
 *
 * OrderItem reprezinta un element individual dintr-o comanda,
 * continand ID ul comenzii, ID ul produsului si cantitatea comandata
 *
 * In plus, ofera o metoda specifica pentru a obtine toate OrderItem-urile
 * asociate unei comenzi (orderID)
 */
public class OrderItmDAO extends AbstractDAO<OrderItem>{
    /**
     * reture=neaza toate elementele OrderItem asociate unei comenzi
     * @param orderId (id ul comenzii pentru care se cauta elemente;e)
     * @return lista de OrderItem apartinand comenzii
     *
     * Metoda executa manual un SELECT deoarece AbstractDAO nu ofera
     * un mecanism generic pentru interogari pe campuri multiple
     */
    public List<OrderItem> findItemsByOrderId(int orderId) {
        List<OrderItem> items = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = "SELECT * FROM orderitem WHERE orderid = ?";

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, orderId);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                items.add(new OrderItem(
                        resultSet.getInt("id"),
                        resultSet.getInt("orderid"),
                        resultSet.getInt("productid"),
                        resultSet.getInt("quantity")
                ));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "OrderItemDAO:findItemsByOrderId " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return items;
    }
}
