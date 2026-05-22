package DataAccesLayer;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import Connection.ConnectionFactory;

/**
 * clasa generica ce implementeaza operatiile CRUD pt orice tip de obiect
 * foloseste reflectia pentru a genera automat interogarile SQL si pentru a popula
 * obiectele model pe baza rezultatelor din baza de date
 * @param <T> (tipul obiectului gestionat de DAO)
 */
public class AbstractDAO<T> {
    /** Logger pentru afisarea mesajelor de eroare si debugging*/
    protected static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());
    private final Class<T> type; /** tipul clasei generice T, determinat prin reflectie */

    /**
     * Constructor care determina tipul generic T la runtgime
     * este necesar pentru a putea crea instante si a accesa campurile prin reflectie
     */
    public AbstractDAO() {
        this.type = (Class<T>)((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * creeaza o interogare SELECT pentru un anumit camp
     * @param field (numele coloanei dupa care se cauta)
     * @return interogarea SQL generata
     */
    private String createSelectQuery(String field){
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM ");
        sb.append(type.getSimpleName());
        sb.append(" WHERE ").append(field).append(" = ?");
        return sb.toString();
    }

    /**
     * returneaza toate interogarile din tabela asociata clasei T
     * @return lista de obiecte
     */
    public List<T> findAll(){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = "select * from " + type.getSimpleName();
        try{
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            return createObjects(resultSet);
        } catch (SQLException e){
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findAll " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    /**
     * cauta o inregistrare dupa id
     * @param id (valoarea id ului cautat)
     * @return obiectul T gasit sau null daca nu exista
     */
    public T findById(int id){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectQuery("id");
        try{
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            List<T> list = createObjects(resultSet);
            if(!list.isEmpty()){
                return list.get(0);
            }
        } catch(SQLException e){
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findById " + e.getMessage());
        } finally{
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    /**
     * creeaza obiecte de tip T pe baza unui ResultSet
     * Foloseste reflectia pentru a seta valorile campurilor
     *
     * Include conversie automata pentru:
     *      java.sql.Timestamp -> java.util.Date
     *      java.time.LocalDateTime -> java.util.Date
     * @param resultSet (rezultatul interogarii SQL)
     * @return lista de obiecte T populate
     */
    private List<T> createObjects(ResultSet resultSet) {
        List<T> list = new ArrayList<>();
        try {
            while (resultSet.next()) {
                T instance = type.getDeclaredConstructor().newInstance();

                for (Field field : type.getDeclaredFields()) {
                    field.setAccessible(true);
                    Object value = resultSet.getObject(field.getName());

                    if (value != null && field.getType() == java.util.Date.class) {
                        if (value instanceof java.sql.Timestamp ts) {
                            value = new java.util.Date(ts.getTime());
                        } else if (value instanceof java.time.LocalDateTime ldt) {
                            value = java.util.Date.from(
                                    ldt.atZone(java.time.ZoneId.systemDefault()).toInstant()
                            );
                        }
                    }

                    field.set(instance, value);
                }

                list.add(instance);
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Err creating obj " + e.getMessage());
        }
        return list;
    }

    /**
     * Actualizeaza o inregistrare existenta in baza de date
     * @param t (obiectul cu valorile noi)
     * @return obiectul actualizat
     */
    public T update(T t){
        Connection connection = null;
        PreparedStatement statement = null;
        Field[] fields = t.getClass().getDeclaredFields();
        StringBuilder sql = new StringBuilder("update " + t.getClass().getSimpleName() + " set ");

        try{
            for(int i = 0; i < fields.length; i++){
                fields[i].setAccessible(true);
                sql.append(fields[i].getName() + " =?" + (i == fields.length - 1 ? "" : ", "));
            }
            sql.append(" where id = ?");

            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(sql.toString());

            for(int i = 0; i <  fields.length; i++){
                fields[i].setAccessible(true);
                statement.setObject( i + 1, fields[i].get(t));
            }

            Field idF = t.getClass().getDeclaredField("id");
            idF.setAccessible(true);
            statement.setObject(fields.length + 1, idF.get(t));

            statement.executeUpdate();

        }catch(Exception e){
            LOGGER.log(Level.WARNING, type.getName() + "DAO:update " + e.getMessage());
        } finally{
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return t;
    }

    /**
     * sterge o inregistrare din baza de date dupa id
     * @param id (id ul inregistrarii ce trbuie stearsa)
     */
    public void delete(int id){
        Connection connection = null;
        PreparedStatement statement = null;
        String query = "delete from " + type.getSimpleName() + " where id = ?";
        try{
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch(SQLException e){
            LOGGER.log(Level.WARNING, type.getName() + "DAO:delete " + e.getMessage());
        } finally{
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }

    /**
     * insereaza un obiect nou in baza de date
     * seteaza automat id ul generat in obiectul T
     * @param t (obiectul care trebuie inserat)
     * @return obiectul inserat cu id ul completat
     */
    public T insert(T t){
        Connection connection = null;
        PreparedStatement statement = null;
        Field[] fields = type.getDeclaredFields();
        StringBuilder sql = new StringBuilder("insert into " + type.getSimpleName() + " (");

        boolean first = true;
        for (Field field : fields) {
            if (field.getName().equalsIgnoreCase("id")) continue;
            if (!first) sql.append(", ");
            sql.append(field.getName());
            first = false;
        }

        sql.append(") values (");

        first = true;
        for (Field field : fields) {
            if (field.getName().equalsIgnoreCase("id")) continue;
            if (!first) sql.append(", ");
            sql.append("?");
            first = false;
        }
        sql.append(")");

        try{
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);

            int parameterIndex = 1;
            for (Field field : fields) {
                if (field.getName().equalsIgnoreCase("id")) continue;
                field.setAccessible(true);
                Object value = field.get(t);

                if (value instanceof java.util.Date) {
                    statement.setObject(parameterIndex, new java.sql.Timestamp(((java.util.Date) value).getTime()));
                } else {
                    statement.setObject(parameterIndex, value);
                }
                parameterIndex++;
            }

            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                int insertedId = rs.getInt(1);
                Field idField = type.getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(t, insertedId);
            }
        } catch(Exception e){
            LOGGER.log(Level.WARNING, type.getName() + "DAO:insert " + e.getMessage());
        } finally{
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }

        return t;
    }

}
