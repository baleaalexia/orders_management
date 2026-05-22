package BusinessLayer;

import DataAccesLayer.ProductDAO;
import Model.Product;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * clasa responsabila pentru logica de business asociata produselor
 * intermediaza comunicarea dintre interfata grafica si nivelul de acces la date
 * aici se valideaza datele, se trateaza erorile si se aplica regulile de business
 * inainte de a apela metodele in DAO
 */
public class ProductBLL {
    private ProductDAO productDAO; /** obiect DAO pt operatiile CRUD asupra produselor*/
    /**
     * initializeaza logica de business si creeaza instanta DAO necesara
     */
    public ProductBLL(){
        productDAO = new ProductDAO();
    }

    /**
     * cauta un produs dupa id
     * @param id (id ul produsului cautat)
     * @return obiectul product gasit
     */
    public Product findProductById(int id){
        Product p = productDAO.findById(id);
        if(p == null)
            throw new NoSuchElementException("Product with id not found");
        return p;
    }

    /**
     * returneaza lista tuturor produselor din baza de date
     * @return lista de obiecte product
     */
    public List<Product> findAllProducts(){
        return productDAO.findAll();
    }

    /**
     * insereaza un produs nou in baza de date
     * @param product obiectul product care trebuie inserat
     */
    public void insertProduct(Product product){
        productDAO.insert(product);
    }

    /**
     * actualizeaza informatiile unui produs existent
     * @param product obiectul product cu noile valori
     */
    public void updateProduct(Product product){
        productDAO.update(product);
    }

    /**
     * sterge un produs din baza de date
     * @param id id ul produsului care trebuie sters
     */
    public void deleteProduct(int id){
        productDAO.delete(id);
    }

    /**
     * returneaza doar produsele care au stoc disponibil >0
     * @return lista de produse disponibile
     */
    public List<Product> findAvailableProducts() {
        List<Product> allProducts = productDAO.findAll();

        return allProducts.stream()
                .filter(product -> product.getStock() > 0)
                .collect(Collectors.toList());
    }
}
