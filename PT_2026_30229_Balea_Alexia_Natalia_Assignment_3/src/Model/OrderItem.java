package Model;

/**
 * reprezinta un element dintr-o comanda
 * fiecare OrderItem indica un produs si cantitatea comandata din acel produs, si
 * este folosita pentru:
 *      construirea cosului de cumparaturi,
 *      inserarea in tabela OrderItem din baza de date,
 *      afisarea detaliilor unei comenzi
 */
public class OrderItem {
    private int id; /** id al elementului de comanda*/
    private int orderId; /** id ul comenzii din care face parte acest element*/
    private int productId; /** id ul produsului comandat*/
    private int quantity; /** cantitatea comandata din produs*/

    /**
     * constructor implicit necesar pentru reflectie si operatiile DAO
     */
    public OrderItem() {
    }

    /**
     * creeaza un obiect de tipul OrderItem complet initializat
     * @param id (id unic al elementului)
     * @param orderId (id ul comenzii asociate)
     * @param productId (id ul produsului comandat)
     * @param quantity (cantitatea comandata)
     */
    public OrderItem(int id, int orderId, int productId, int quantity) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
    }

    /** id ul elementului de comanda
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * @param id seteaza id ul elementului de comanda
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @param orderId seteaza id ul comenzii asociate
     */
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    /**
     * @return id ul produsului comandat
     */
    public int getProductId() {
        return productId;
    }

    /**
     * @return cantitatea comandata
     */
    public int getQuantity() {
        return quantity;
    }

}