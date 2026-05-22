package Model;

/**
 * reprezinta un produs din depozit.
 * contine informatii despre identificator, nume, pret si stoc
 * este folosit in operatiile CRUD si in procesarea comenzilor.
 */

public class Product {
    private int id;
    private String name;
    private double price;
    private int stock;

    /**
     * constructor implicit pentru reflectie si pentru operatiile DAO
     */
    public Product(){}

    /**
     * creeaza un obiect Product complet initializat
     * @param id (id ul unic al produsului)
     * @param name (numele produsului)
     * @param price (pretul produsului)
     * @param stock (cantitatea disponibila in stoc)
     */
    public Product(int id, String name, double price, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    /**
     * @return id-ul produsului
     */
    public int getId() {
        return id;
    }

    /**
     * @param id seteaza id ul produsului
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return numele produsului
     */
    public String getName() {
        return name;
    }

    /**
     * @return pretul produsului
     */
    public double getPrice() {
        return price;
    }

    /**
     * @return cantitatea disponibila in stoc
     */
    public int getStock() {
        return stock;
    }

    /**
     * @param stock seteaza cantitatea disponibila in stoc
     */
    public void setStock(int stock) {
        this.stock = stock;
    }
}
