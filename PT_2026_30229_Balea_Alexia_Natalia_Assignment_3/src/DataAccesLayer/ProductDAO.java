package DataAccesLayer;

import Model.Product;

/**
 * Clasa dedicata entitatii Product.
 * Extinde AbstractDAO pentru a mosteni operatiile CRUD generice:
 *      inserare
 *      cautare dupa ID
 *      listare completa
 *      actualizare
 *      stergere
 *
 * Product reprezintaun articol din stoc avand
 * campurile: id, name, price si stock
 *
 * Deoarece logica specifica este deja implementata in AbstractDAO,
 * aceasta clasa nu necesita metode suplimentare
 */
public class ProductDAO extends AbstractDAO<Product>{
}
