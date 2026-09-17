package DataAccesLayer;
import Model.Client;

/**
 * clasa dedicata entitatii Client.
 * extinde AbstractDAO pentru a mosteni operatiile CRUD generice:
 *      inserare
 *      cautare dupa ID
 *      listare completa
 *      actualizare
 *      stergere
 *
 * Deoarece logica specifica este deja implementata in AbstractDAO,
 * aceasta clasa nu necesita metode suplimentare
 */
public class ClientDAO extends AbstractDAO<Client>{
}
