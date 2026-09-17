package Model;

import java.util.Date;

/**
 * reprezinta o comanda plasata de un client, care contine:
 *      id ul comenzii
 *      id ul clientului care a plasat comanda
 *      id ul produsului comandat
 *      cantitatea comandata
 *      data la care a fost plasata comanda
 *
 *      obiectele orders sunt gestionate prin operatii crud
 *      si sunt folosite la generarea facturilor
 */
public class Orders {
    private int id; /** identificatorul unic al comenzii*/
    private int clientId; /** id ul clientului care a plasat comanda*/
    private int productId; /** id ul produsului comandat*/
    private int quantity; /** cantitatea comandata*/
    private Date date; /** data la care a fost plasata comanda*/
    /**
     * constructor implicit necesar pentru reflectie si DAO
     */
    public Orders() {}

    /**
     * creeaza un obievct Orders complet initializat
     * @param id
     * @param clientId
     * @param productId
     * @param quantity
     * @param date
     */
    public Orders(int id, int clientId, int productId, int quantity, Date date) {
        this.id = id;
        this.clientId = clientId;
        this.productId = productId;
        this.quantity = quantity;
        this.date = date;
    }

    /**
     * @return id-ul comenzii
     */
    public int getId() { return id; }

    /**
     * @param id seteaza id-ul comenzii
     */
    public void setId(int id) { this.id = id; }

    /**
     * @return id-ul clientului
     */
    public int getClientId() { return clientId; }

}
