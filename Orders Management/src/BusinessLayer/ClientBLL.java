package BusinessLayer;

import DataAccesLayer.ClientDAO;
import Model.Client;

import javax.swing.text.View;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * clasa responsabila pentru logica de business asociata entitatii client.
 * intermediaza operatiile dintre interfata grafica si nivelul
 * de acces la date.
 * aici se valideaza datele, se trateaza erorile si se aplica regulile de
 * business inainte de a apela metodele DAO.
 */
public class ClientBLL {
    private ClientDAO clientDAO; /** obiect DAO utilizat pentru operatiile CRUD asupra tabelei Client*/
    private EmailValidator emailValidator;

    /** initializeaza logica de business si creeaza instanta DAO necesara*/
    public ClientBLL(){
        clientDAO = new ClientDAO();
        emailValidator = new EmailValidator();

    }

    /**
     * cauta un client dupa ID
     * @param id (id ul clientului cautat)
     * @return obiectul Client gasit
     */
    public Client findClientById(int id) {
        Client client = clientDAO.findById(id);
        if (client == null) {
            throw new NoSuchElementException("The client with id " + id + " not found!");
        }
        return client;
    }

    /**
     * returneaza lista tuturor clientilor din baza de date
     * @return lista de obiecte Client
     */
    public List<Client> findAllClients() {
        return clientDAO.findAll();
    }

    /**
     * insereaza un client nou in baza de date
     * @param client obiectul Client care trebuie inserat
     * @return
     * @throws Exception
     */
    public Client insertClient(Client client) throws Exception {
        if (client.getName() == null || client.getName().trim().isEmpty()) {
            throw new Exception("Client name cannot be empty!");
        }

        emailValidator.validate(client);

        return clientDAO.insert(client);
    }

    /**
     * actualizeaza informatiile unui client existent
     * @param client obiectul Client cu noile valori
     * @return
     * @throws Exception
     */
    public Client updateClient(Client client) throws Exception {
        if (client.getName() == null || client.getName().trim().isEmpty()) {
            throw new Exception("Client name cannot be empty!");
        }

        emailValidator.validate(client);


        return clientDAO.update(client);
    }

    /**
     * sterge un client din baza de date
     * @param id
     */
    public void deleteClient(int id) {
        clientDAO.delete(id);
    }
}
