package Connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clasa responsabila pentru gestionarea conexiunilor la baza de date.
 * Foloseste un mecanism de tip factory pentru a crea conexiuni
 * si ofera metode statice pentru inchiderea in siguranta a resurselor JDBC
 *
 * Aceasta clasa este utilizata de toate DAO-urile pentru:
 *      obtinerea conexiunii la baza de date
 *      inchiderea conexiunilor, statement urilor si resultSet urilor
 *
 *      Implementarea este simpla si robusta, evitand scurgerile de resurse
 */
public class ConnectionFactory {

    /** Logger pentru afisarea erorilor si mesajelor de debugging*/
    private static final Logger LOGGER = Logger.getLogger(ConnectionFactory.class.getName());
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    /** url ul bazei de date*/
    private static final String DBURL = "jdbc:mysql://localhost:3306/ordermanagementapp";
    /** username ul pentru conectare*/
    private static final String USER = "root";
    /** parola pentru conectare*/
    private static final String PASS = "Cerdanyola05";

    private static ConnectionFactory singleInstance = new ConnectionFactory();

    private ConnectionFactory() {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Connection createConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DBURL, USER, PASS);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "An error occured while trying to connect to the database");
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * creeaza o conexiune catre baza de date
     * @return obiect Connection daca reuseste, altfel NULL
     */
    public static Connection getConnection() {
        return singleInstance.createConnection();
    }

    /**
     * inchide un obiect connection
     * @param connection  (obiectul de inchis (poate fi null))
     */
    public static void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "An error occured while trying to close the connection");
            }
        }
    }

    /**
     * inchide un obiect Statement
     * @param statement (obiectul de inchis (poate fi null))
     */
    public static void close(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "An error occured while trying to close the statement");
            }
        }
    }

    /**
     * inchide un obiect ResultSet
     * @param resultSet (obiectul de inchis (poate fi null))
     */
    public static void close(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "An error occured while trying to close the ResultSet");
            }
        }
    }
}
