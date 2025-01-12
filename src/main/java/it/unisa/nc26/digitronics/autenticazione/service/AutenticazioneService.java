package it.unisa.nc26.digitronics.autenticazione.service;

import it.unisa.nc26.digitronics.model.bean.ItemCarrello;
import it.unisa.nc26.digitronics.model.bean.Utente;

import java.sql.SQLException;
import java.util.List;

/**
 * Interfaccia che definisce i metodi del servizio di autenticazione.
 */
public interface AutenticazioneService {

    /**
     * Recupera un utente dal database tramite email e password.
     *
     * @param email    l'email dell'utente
     * @param password la password dell'utente
     * @return l'oggetto {@link Utente} corrispondente, oppure null se non trovato
     */
    Utente retrieveUtenteEmailPassword(String email, String password);

    /**
     * Recupera il carrello associato a un utente tramite il suo ID.
     *
     * @param id l'ID dell'utente
     * @return una lista di {@link ItemCarrello} contenente gli elementi del carrello
     */
    List<ItemCarrello> retrieveCarrelloUtente(int id);

    /**
     * Elimina tutti gli elementi del carrello associato a un utente.
     *
     * @param idUtente l'ID dell'utente
     */
    void clearCarrelloUtente(int idUtente);

    /**
     * Salva gli elementi di un carrello nel database.
     *
     * @param carrelli la lista di {@link ItemCarrello} da salvare
     */
    void saveCarrelloUtente(List<ItemCarrello> carrelli);
}
