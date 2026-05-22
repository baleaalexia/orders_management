package Model;

import java.util.Date;

/**
 * Reprezinta o factura generata pentru fiecare comanda procesata
 * Aceasta clasa este definita ca un record => nu poate fi modificata.
 *
 * O factura contine:
 * @param id (id-ul comenzii/facturii)
 * @param clientId (id-ul clientului)
 * @param totalPrice (pretul total al comenzii)
 * @param date (data emiterii facturii)
 * @param address (adresa clientului)
 *
 * Facturile sunt doar inserate si citite din baza de date,
 * fara posibilitatea de a le modifica / sterge
 */

public record Bill(
        int id,
        int clientId,
        double totalPrice,
        Date date,
        String address
) {
}
