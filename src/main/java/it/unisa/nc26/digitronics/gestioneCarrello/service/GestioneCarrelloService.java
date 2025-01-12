package it.unisa.nc26.digitronics.gestioneCarrello.service;

import it.unisa.nc26.digitronics.model.bean.Prodotto;
import it.unisa.nc26.digitronics.utils.MyServletException;

/**
 * Interfaccia per il servizio di gestione del carrello.
 */
public interface GestioneCarrelloService {

    /**
     * Recupera un prodotto dal database tramite il suo ID.
     *
     * @param idProdotto l'ID del prodotto da recuperare
     * @return l'oggetto {@link Prodotto} corrispondente
     * @throws MyServletException se il prodotto non esiste
     */
    Prodotto fetchByIdProdotto(int idProdotto) throws MyServletException;
}
