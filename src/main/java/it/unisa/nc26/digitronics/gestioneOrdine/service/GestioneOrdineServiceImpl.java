package it.unisa.nc26.digitronics.gestioneOrdine.service;

import it.unisa.nc26.digitronics.gestioneOrdine.service.IndirizzoAdapter.OpenStreetMapVerificaIndirizzoApiAdapterImpl;
import it.unisa.nc26.digitronics.gestioneOrdine.service.IndirizzoAdapter.VerificaIndirizzoApiAdapter;
import it.unisa.nc26.digitronics.model.bean.ItemOrdine;
import it.unisa.nc26.digitronics.model.bean.MetodoSpedizione;
import it.unisa.nc26.digitronics.model.bean.Ordine;
import it.unisa.nc26.digitronics.model.bean.Prodotto;
import it.unisa.nc26.digitronics.model.dao.*;
import it.unisa.nc26.digitronics.utils.MyServletException;

import java.util.List;

/**
 * Implementazione del servizio per la gestione degli ordini.
 *
 * Questa classe fornisce le implementazioni concrete per le operazioni
 * definite nell'interfaccia {@link GestioneOrdineService}.
 */
public class GestioneOrdineServiceImpl implements GestioneOrdineService {

    private ProdottoDAO prodottoDAO;
    private MetodoSpedizioneDAO metodoSpedizioneDAO;
    private OrdineDAO ordineDAO;
    private ItemCarrelloDAO itemCarrelloDAO;
    private ItemOrdineDAO itemOrdineDAO;
    private VerificaIndirizzoApiAdapter verificaIndirizzoApiAdapter;

    /**
     * Costruttore di default che inizializza i DAO e l'adapter per la verifica
     * degli indirizzi.
     */
    public GestioneOrdineServiceImpl() {
        this.prodottoDAO = new ProdottoDAO();
        this.metodoSpedizioneDAO = new MetodoSpedizioneDAO();
        this.ordineDAO = new OrdineDAO();
        this.itemCarrelloDAO = new ItemCarrelloDAO();
        this.itemOrdineDAO = new ItemOrdineDAO();
        this.verificaIndirizzoApiAdapter = new OpenStreetMapVerificaIndirizzoApiAdapterImpl();
    }

    /**
     * Imposta il DAO per la gestione dei prodotti.
     *
     * @param prodottoDAO il DAO da utilizzare per le operazioni sui prodotti
     */
    public void setProdottoDAO(ProdottoDAO prodottoDAO) {
        this.prodottoDAO = prodottoDAO;
    }

    /**
     * Imposta il DAO per la gestione dei metodi di spedizione.
     *
     * @param metodoSpedizioneDAO il DAO da utilizzare per le operazioni sui metodi di spedizione
     */
    public void setMetodoSpedizioneDAO(MetodoSpedizioneDAO metodoSpedizioneDAO) {
        this.metodoSpedizioneDAO = metodoSpedizioneDAO;
    }

    /**
     * Imposta il DAO per la gestione degli ordini.
     *
     * @param ordineDAO il DAO da utilizzare per le operazioni sugli ordini
     */
    public void setOrdineDAO(OrdineDAO ordineDAO) {
        this.ordineDAO = ordineDAO;
    }

    /**
     * Imposta il DAO per la gestione degli item del carrello.
     *
     * @param itemCarrelloDAO il DAO da utilizzare per le operazioni sugli item del carrello
     */
    public void setItemCarrelloDAO(ItemCarrelloDAO itemCarrelloDAO) {
        this.itemCarrelloDAO = itemCarrelloDAO;
    }

    /**
     * Imposta il DAO per la gestione degli item degli ordini.
     *
     * @param itemOrdineDAO il DAO da utilizzare per le operazioni sugli item degli ordini
     */
    public void setItemOrdineDAO(ItemOrdineDAO itemOrdineDAO) {
        this.itemOrdineDAO = itemOrdineDAO;
    }

    /**
     * Imposta l'adapter per la verifica degli indirizzi.
     *
     * @param verificaIndirizzoApiAdapter l'adapter da utilizzare per la verifica degli indirizzi
     */
    public void setVerificaIndirizzoApiAdapter(VerificaIndirizzoApiAdapter verificaIndirizzoApiAdapter) {
        this.verificaIndirizzoApiAdapter = verificaIndirizzoApiAdapter;
    }

    /**
     * Recupera un prodotto in base al suo ID.
     *
     * @param idProdotto l'identificativo del prodotto da recuperare
     * @return il prodotto corrispondente all'ID specificato
     * @throws MyServletException se il prodotto non esiste
     */
    @Override
    public Prodotto fetchByIdProdotto(int idProdotto) throws MyServletException {
        Prodotto prodotto = prodottoDAO.doRetrieveById(idProdotto);
        if (prodotto == null) {
            throw new MyServletException("Prodotto con ID " + idProdotto + " non esistente.");
        }
        return prodotto;
    }

    /**
     * Recupera tutti i metodi di spedizione disponibili.
     *
     * @return la lista di tutti i metodi di spedizione
     */
    @Override
    public List<MetodoSpedizione> fetchAllMetodiSpedizione() {
        return metodoSpedizioneDAO.doRetrieveAll();
    }

    /**
     * Recupera un metodo di spedizione attraverso il nome.
     *
     * @param nome il nome del metodo di spedizione da recuperare
     * @return il metodo di spedizione corrispondente al nome specificato
     */
    @Override
    public MetodoSpedizione fetchMetodoSpedizioneByNome(String nome) {
        return metodoSpedizioneDAO.doRetrieveByName(nome);
    }

    /**
     * Salva un ordine nel sistema.
     *
     * @param ordine l'ordine da salvare
     * @return l'identificativo assegnato al nuovo ordine
     */
    @Override
    public int saveOrdine(Ordine ordine) {
        return ordineDAO.doSave(ordine);
    }

    /**
     * Aggiorna la quantità disponibile per un prodotto.
     *
     * @param prodotto il prodotto da aggiornare
     */
    @Override
    public void updateQuantitàProdotto(Prodotto prodotto) {
        prodottoDAO.doUpdate(prodotto);
    }

    /**
     * Rimuove il carrello associato ad un utente specifico.
     *
     * @param idUtente l'identificativo dell'utente per il quale rimuovere il carrello
     */
    @Override
    public void rimuoviCarrelloServletByIdUtente(int idUtente) {
        itemCarrelloDAO.doDelete(idUtente);
    }

    /**
     * Salva un item associato ad un ordine.
     *
     * @param itemOrdine l'item dell'ordine da salvare
     */
    @Override
    public void saveItemOrdine(ItemOrdine itemOrdine) {
        itemOrdineDAO.doSave(itemOrdine);
    }

    /**
     * Verifica se un indirizzo è valido.
     *
     * @param via la via dell'indirizzo
     * @param cap il codice di avviamento postale
     * @param città la città
     * @return {@code true} se l'indirizzo è valido, {@code false} altrimenti
     */
    @Override
    public boolean verificaIndirizzo(String via, String cap, String città) {
        return verificaIndirizzoApiAdapter.verifica(via, cap, città);
    }

    /**
     * Recupera tutti gli ordini effettuati da un utente.
     *
     * @param idUtente l'identificativo dell'utente
     * @return la lista degli ordini associati all'utente
     */
    @Override
    public List<Ordine> fetchByIdUtente(int idUtente) {
        return ordineDAO.doRetrieveByCustomer(idUtente);
    }

    /**
     * Recupera tutti gli ordini presenti nel sistema.
     *
     * @return la lista di tutti gli ordini
     */
    @Override
    public List<Ordine> fetchAllOrders() {
        return ordineDAO.doRetrieveAll();
    }

    /**
     * Recupera tutti gli item associati ad un ordine.
     *
     * @param idOrdine l'identificativo dell'ordine
     * @return la lista degli item associati all'ordine
     * @throws MyServletException se non vengono trovati item per l'ordine specificato
     */
    @Override
    public List<ItemOrdine> fetchItemOrder(int idOrdine) throws MyServletException {
        List<ItemOrdine> itemsOrdine = itemOrdineDAO.doRetrieveByOrdine(idOrdine);
        if (itemsOrdine.isEmpty()) {
            throw new MyServletException("Nessun elemento trovato per l'ordine con ID " + idOrdine + ".");
        }
        return itemsOrdine;
    }

    /**
     * Recupera un ordine in base al suo ID.
     *
     * @param idOrdine l'identificativo dell'ordine da recuperare
     * @return l'ordine corrispondente all'ID specificato
     * @throws MyServletException se l'ordine non esiste
     */
    // Prende l'ordine
    @Override
    public Ordine fetchByIdOrder(int idOrdine) throws MyServletException {
        Ordine ordine = ordineDAO.doRetrieveByIdOrder(idOrdine);
        if (ordine == null) {
            throw new MyServletException("Ordine con ID " + idOrdine + " non esistente.");
        }
        return ordine;
    }
}
