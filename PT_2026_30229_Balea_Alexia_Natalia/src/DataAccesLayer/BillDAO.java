package DataAccesLayer;

import Connection.ConnectionFactory;
import DataAccesLayer.AbstractDAO;
import Model.Bill;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * clasa DAO dedicata entitatii Bill
 * extinde AbstractDAO pentru a mosteni operatiile CRUD generice
 *
 * Deoarece facturile sunt imuabile si nu trebuie modificate,
 * aceasta clasa este folosita in principal pentru:
 *      inserarea facturilor in tabela Log/Bill
 *      citirea facturilor existente
 *
 * Nu se permit operatii de update sau delete asupra facturilor
 */
public class BillDAO extends AbstractDAO<Bill> {

    /**
     * creeaza un nou BillDAO
     * constructorul nu necesita logica suplimentara deoarece
     * abstractDao gestioneaza toate operatiile necesare
     */
    public BillDAO() {
        super();
    }

    /**
     * insereaza o factura noua in baza de date
     * @param bill (factura care trebuie inserata)
     * @return factura inserata cu id ul completat
     */
    @Override
    public Bill insert(Bill bill) {
        Connection connection = null;
        PreparedStatement statement = null;
        String query = "INSERT INTO bill (order_id, client_id, total_price, date, address) VALUES (?, ?, ?, ?, ?)";

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            statement.setInt(1, bill.id());
            statement.setInt(2, bill.clientId());
            statement.setDouble(3, bill.totalPrice());
            statement.setTimestamp(4, new java.sql.Timestamp(bill.date().getTime()));
            statement.setString(5, bill.address());

            statement.executeUpdate();

            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                int insertedId = rs.getInt(1);
                return new Bill(insertedId, bill.clientId(), bill.totalPrice(), bill.date(), bill.address());
            }

        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "BillDAO:insert " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return bill;
    }

    /**
     * returneaza toate facturile generate
     * @return o lista cu toate elementele facturile
     */
    @Override
    public List<Bill> findAll() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Bill> bills = new ArrayList<>();
        String query = "SELECT * FROM bill";

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                bills.add(new Bill(
                        resultSet.getInt("order_id"),
                        resultSet.getInt("client_id"),
                        resultSet.getDouble("total_price"),
                        resultSet.getTimestamp("date"),
                        resultSet.getString("address")
                ));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "BillDAO:findAll " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return bills;
    }

    /**
     * incearca actualizarea unei factura deja existenta in baza de date
     * @param bill (factura care trebuie actualizata)
     * @return o exceptie deoarece facturile nu pot fi actualizate
     */
    @Override
    public Bill update(Bill bill) {
        throw new UnsupportedOperationException("Bills cannot be updated!");
    }

    /**
     * incearca stergerea unei facturi din baza de date
     * @param id (id ul facturii ce trebuie stearsa)
     */
    @Override
    public void delete(int id) {
        throw new UnsupportedOperationException("Bills cannot be deleted!");
    }
}