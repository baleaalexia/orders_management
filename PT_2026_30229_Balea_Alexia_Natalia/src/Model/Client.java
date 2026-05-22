package Model;

/**
    Reprezinta un client din sistem si contine informatii de
 identificare precum id, nume, adresa si email.
    Este folosita in operatiile CreateReadUpdateDelete si in
 procesarea comenzilor.
 */
public class Client {
    private int id;
    private String name;
    private String address;
    private String email;

    /**
     * Constructor implicit necesar pentru reflectie si DAO.
     */
    public Client(){}

    /**
     * Creeaza un obiect de tipul Client complet initializat.
     * @param id
     * @param name
     * @param address
     * @param email
     */
    public Client(int id, String name, String address, String email) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.email = email;
    }

    /**
     * @return ID-ul clientului
     */
    public int getId() {
        return id;
    }

    /**
     * @param id seteaza ID-ul clientului
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return numele clientului
     */
    public String getName() {
        return name;
    }

    /**
     * @return adresa clientului
     */
    public String getAddress() {
        return address;
    }

    /**
     * @return email ul clientului
     */
    public String getEmail() {
        return email;
    }

}
