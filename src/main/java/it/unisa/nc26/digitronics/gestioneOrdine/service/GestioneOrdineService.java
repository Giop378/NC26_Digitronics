package it.unisa.nc26.digitronics.gestioneOrdine.service;

import it.unisa.nc26.digitronics.model.bean.ItemOrdine;
import it.unisa.nc26.digitronics.model.bean.MetodoSpedizione;
import it.unisa.nc26.digitronics.model.bean.Ordine;
import it.unisa.nc26.digitronics.model.bean.Prodotto;
import it.unisa.nc26.digitronics.utils.MyServletException;

import java.util.List;

/**
 * Interfaccia per la gestione degli ordini.
 *
 * Fornisce metodi per operazioni relative a prodotti, spedizioni, ordini e
 * item degli ordini.
 */
public interface GestioneOrdineService {

    /**
     * Recupera un prodotto in base al suo ID.
     *
     * @param idProdotto l'identificativo del prodotto da recuperare
     * @return il prodotto corrispondente all'ID specificato
     * @throws MyServletException se il prodotto non esiste
     */
    Prodotto fetchByIdProdotto(int idProdotto) throws MyServletException;

    /**
     * Recupera tutti i metodi di spedizione disponibili.
     *
     * @return la lista di tutti i metodi di spedizione
     */
    List<MetodoSpedizione> fetchAllMetodiSpedizione();

    /**
     * Recupera un metodo di spedizione tramite il nome.
     *
     * @param nome il nome del metodo di spedizione da recuperare
     * @return il metodo di spedizione corrispondente al nome specificato
     */
    MetodoSpedizione fetchMetodoSpedizioneByNome(String nome);

    /**
     * Salva un ordine nel sistema.
     *
     * @param ordine l'ordine da salvare
     * @return l'identificativo assegnato al nuovo ordine
     */
    int saveOrdine(Ordine ordine);

    /**
     * Aggiorna la quantità disponibile per un prodotto.
     *
     * @param prodotto il prodotto da aggiornare
     */
    void updateQuantitàProdotto(Prodotto prodotto);

    /**
     * Rimuove il carrello associato a un utente specifico.
     *
     * @param idUtente l'identificativo dell'utente per il quale rimuovere il carrello
     */
    void rimuoviCarrelloServletByIdUtente(int idUtente);

    /**
     * Salva un item associato ad un ordine.
     *
     * @param itemOrdine l'item dell'ordine da salvare
     */
    void saveItemOrdine(ItemOrdine itemOrdine);

    /**
     * Verifica se un indirizzo risulta valido.
     *
     * @param via la via dell'indirizzo
     * @param cap il codice di avviamento postale dell'indirizzo
     * @param città la città dell'indirizzo
     * @return {@code true} se l'indirizzo è valido, {@code false} altrimenti
     */
    boolean verificaIndirizzo(String via, String cap, String città);

    /**
     * Recupera tutti gli ordini effettuati da un utente.
     *
     * @param idUtente l'identificativo dell'utente
     * @return la lista degli ordini associati all'utente
     */
    List<Ordine> fetchByIdUtente(int idUtente);

    /**
     * Recupera tutti gli ordini presenti nel sistema.
     *
     * @return la lista di tutti gli ordini
     */
    List<Ordine> fetchAllOrders();

    /**
     * Recupera tutti gli item associati ad un ordine.
     *
     * @param idOrdine l'identificativo dell'ordine
     * @return la lista degli item dell'ordine
     * @throws MyServletException se non vengono trovati item per l'ordine specificato
     */
    List<ItemOrdine> fetchItemOrder(int idOrdine) throws MyServletException;

    /**
     * Recupera un ordine in base al suo ID.
     *
     * @param idOrdine l'identificativo dell'ordine da recuperare
     * @return l'ordine corrispondente all'ID specificato
     * @throws MyServletException se l'ordine non esiste
     */
    Ordine fetchByIdOrder(int idOrdine) throws MyServletException;
}
