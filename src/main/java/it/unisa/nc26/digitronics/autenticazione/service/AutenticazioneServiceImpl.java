package it.unisa.nc26.digitronics.autenticazione.service;

import it.unisa.nc26.digitronics.model.bean.ItemCarrello;
import it.unisa.nc26.digitronics.model.bean.Utente;
import it.unisa.nc26.digitronics.model.dao.ItemCarrelloDAO;
import it.unisa.nc26.digitronics.model.dao.UtenteDAO;

import java.sql.SQLException;
import java.util.List;

/**
 * Implementazione del servizio di autenticazione che gestisce le operazioni relative
 * agli utenti e ai loro carrelli.
 */
public class AutenticazioneServiceImpl implements AutenticazioneService {
    private UtenteDAO utenteDAO;
    private ItemCarrelloDAO itemCarrelloDAO;

    /**
     * Costruttore che inizializza le dipendenze DAO.
     */
    public AutenticazioneServiceImpl() {
        utenteDAO = new UtenteDAO();
        itemCarrelloDAO = new ItemCarrelloDAO();
    }

    /**
     * Imposta un'istanza personalizzata di {@link UtenteDAO}.
     *
     * @param utenteDAO il DAO degli utenti da utilizzare
     */
    public void setUtenteDAO(UtenteDAO utenteDAO) {
        this.utenteDAO = utenteDAO;
    }

    /**
     * Imposta un'istanza personalizzata di {@link ItemCarrelloDAO}.
     *
     * @param itemCarrelloDAO il DAO degli item del carrello da utilizzare
     */
    public void setItemCarrelloDAO(ItemCarrelloDAO itemCarrelloDAO) {
        this.itemCarrelloDAO = itemCarrelloDAO;
    }

    /**
     * Recupera un utente dal database tramite email e password.
     *
     * @param email    l'email dell'utente
     * @param password la password dell'utente
     * @return l'oggetto {@link Utente} corrispondente, oppure null se non trovato
     */
    @Override
    public Utente retrieveUtenteEmailPassword(String email, String password) {
        return utenteDAO.doRetrieveByEmailPassword(email, password);
    }

    /**
     * Recupera il carrello associato a un utente tramite il suo ID.
     *
     * @param id l'ID dell'utente
     * @return una lista di {@link ItemCarrello} contenente gli elementi del carrello
     */
    @Override
    public List<ItemCarrello> retrieveCarrelloUtente(int id) {
        return itemCarrelloDAO.doRetrieveByIdUtente(id);
    }

    /**
     * Elimina tutti gli elementi del carrello associato a un utente.
     *
     * @param idUtente l'ID dell'utente
     */
    @Override
    public void clearCarrelloUtente(int idUtente) {
        itemCarrelloDAO.doDelete(idUtente);
    }

    /**
     * Salva gli elementi di un carrello nel database.
     *
     * @param carrelli la lista di {@link ItemCarrello} da salvare
     */
    @Override
    public void saveCarrelloUtente(List<ItemCarrello> carrelli) {
        itemCarrelloDAO.doSave(carrelli);
    }
}

