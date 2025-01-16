package it.unisa.nc26.digitronics.gestioneRecensione.service;

import it.unisa.nc26.digitronics.model.bean.Prodotto;
import it.unisa.nc26.digitronics.model.bean.Recensione;
import it.unisa.nc26.digitronics.model.bean.Utente;

import java.sql.SQLException;
import java.util.Collection;

/**
 * Interfaccia per la gestione delle operazioni relative alle recensioni.
 */
public interface RecensioneService {

    /**
     * Salva una nuova recensione nel database.
     *
     * @param recensione Oggetto {@link Recensione} che rappresenta i dettagli della recensione da salvare.
     * @throws SQLException Se si verifica un errore durante l'operazione sul database.
     */
    void saveRecensione(Recensione recensione) throws SQLException;

    /**
     * Recupera tutte le recensioni associate a un prodotto specifico.
     *
     * @param idProdotto L'ID del prodotto di cui recuperare le recensioni.
     * @return Una collezione di oggetti {@link Recensione} associati al prodotto specificato.
     * @throws SQLException Se si verifica un errore durante l'operazione sul database.
     */
    Collection<Recensione> fetchByProduct(int idProdotto) throws SQLException;


    /**
     * Recupera tutte le recensioni effettuate da un utente specifico.
     *
     * @param idUtente L'ID dell'utente di cui recuperare le recensioni.
     * @return Una collezione di oggetti {@link Recensione} effettuati dall'utente specificato.
     * @throws SQLException Se si verifica un errore durante l'operazione sul database.
     */
    Collection<Recensione> fetchByUtente(int idUtente) throws SQLException;


}
