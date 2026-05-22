package DataAccesLayer;

import Model.Orders;

/**
 * Clasa dedicata entitatii Orders.
 * Extinde AbstractDAO pentru a mosteni operatiile CRUD generice:
 *      inserare
 *      cautare dupa ID
 *      listare completa
 *      actualizare
 *      stergere
 *
 * Orders reprezinta o comanda plasata de un client, continand
 * informatii precum ID ul clientului, ID ul produsului, cantitatea si data
 * comenzii
 *
 * Deoarece logica specifica este deja implementata in AbstractDAO,
 * aceasta clasa nu necesita metode suplimentare
 */
public class OrdersDAO extends AbstractDAO<Orders>{
}
