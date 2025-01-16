package it.unisa.nc26.digitronics.gestioneRecensione.service;

import it.unisa.nc26.digitronics.gestioneRecensione.service.RecensioneService;
import it.unisa.nc26.digitronics.model.bean.Prodotto;
import it.unisa.nc26.digitronics.model.bean.Recensione;
import it.unisa.nc26.digitronics.model.dao.ProdottoDAO;
import it.unisa.nc26.digitronics.model.dao.RecensioneDAO;

import java.sql.SQLException;
import java.util.Collection;

/**
 * Implementazione del servizio per la gestione delle recensioni.
 * Fornisce metodi per salvare e recuperare recensioni da un database.
 */
public class RecensioneServiceImpl implements RecensioneService {

    /** DAO per gestire le operazioni sulle recensioni. */
    private RecensioneDAO recensioneDAO;

    /** DAO per gestire le operazioni sui prodotti. */
    private ProdottoDAO prodottoDAO;

    /**
     * Costruttore predefinito.
     * Inizializza i DAO necessari per interagire con il database.
     */
    public RecensioneServiceImpl() {
        recensioneDAO = new RecensioneDAO();
        prodottoDAO = new ProdottoDAO();
    }

    /**
     * Imposta un'istanza personalizzata di {@link RecensioneDAO}.
     *
     * @param recensioneDAO Nuova istanza di {@link RecensioneDAO}.
     */
    public void setRecensioneDAO(RecensioneDAO recensioneDAO) {
        this.recensioneDAO = recensioneDAO;
    }

    /**
     * Imposta un'istanza personalizzata di {@link ProdottoDAO}.
     *
     * @param prodottoDAO Nuova istanza di {@link ProdottoDAO}.
     */
    public void setProdottoDAO(ProdottoDAO prodottoDAO) {
        this.prodottoDAO = prodottoDAO;
    }

    /**
     * Salva una recensione nel database.
     *
     * @param recensione Oggetto {@link Recensione} che rappresenta la recensione da salvare.
     * @throws SQLException Se si verifica un errore durante l'operazione sul database.
     */
    @Override
    public void saveRecensione(Recensione recensione) throws SQLException {
        recensioneDAO.doSave(recensione);
    }

    /**
     * Recupera tutte le recensioni associate a un prodotto specifico.
     *
     * @param idProdotto ID del prodotto di cui recuperare le recensioni.
     * @return Una collezione di oggetti {@link Recensione} associati al prodotto specificato.
     * @throws SQLException Se si verifica un errore durante l'operazione sul database.
     */
    @Override
    public Collection<Recensione> fetchByProduct(int idProdotto) throws SQLException {
        return recensioneDAO.doRetrieveByProduct(idProdotto);
    }

    /**
     * Recupera tutte le recensioni effettuate da un utente specifico.
     *
     * @param idUtente ID dell'utente di cui recuperare le recensioni.
     * @return Una collezione di oggetti {@link Recensione} effettuati dall'utente specificato.
     * @throws SQLException Se si verifica un errore durante l'operazione sul database.
     */
    @Override
    public Collection<Recensione> fetchByUtente(int idUtente) throws SQLException {
        return recensioneDAO.doRetrieveByUtente(idUtente);
    }

}