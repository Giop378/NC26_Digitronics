package it.unisa.nc26.digitronics.gestioneProdotto.service;

import it.unisa.nc26.digitronics.model.bean.Prodotto;
import it.unisa.nc26.digitronics.utils.MyServletException;

/**
 * Interfaccia per la gestione dei prodotti nel sistema.
 *
 * <p>Questa interfaccia definisce i metodi per le operazioni fondamentali
 * relative ai prodotti, come la modifica, l'aggiunta e l'eliminazione.</p>
 *
 * <p>Le classi che implementano questa interfaccia sono responsabili per
 * l'accesso ai dati dei prodotti e l'esecuzione delle operazioni corrispondenti
 * sul database o su altre fonti di dati.</p>
 *
 * @see Prodotto
 * @see MyServletException
 */
public interface GestioneProdottoService {

    /**
     * Modifica un prodotto esistente nel sistema.
     *
     * <p>Questo metodo consente di modificare un prodotto già esistente,
     * inclusi i suoi attributi come nome, descrizione, prezzo, quantità, ecc.
     * Il prodotto viene identificato tramite il suo ID e la modifica avviene
     * nel database.</p>
     *
     * @param prodotto il prodotto con i nuovi valori da applicare
     * @throws MyServletException se si verifica un errore durante la modifica
     *         (ad esempio, se il prodotto non esiste o se i dati sono invalidi)
     */
    void modificaProdotto(Prodotto prodotto) throws MyServletException;

    /**
     * Aggiunge un nuovo prodotto al sistema.
     *
     * <p>Questo metodo permette di aggiungere un nuovo prodotto al sistema.
     * Il prodotto viene salvato nel database e deve essere completo di tutte
     * le informazioni necessarie, come nome, descrizione, prezzo, categoria, ecc.</p>
     *
     * @param prodotto il prodotto da aggiungere al sistema
     * @throws MyServletException se si verifica un errore durante l'aggiunta
     *         (ad esempio, se la categoria del prodotto non esiste o i dati sono errati)
     */
    public void aggiungiProdotto(Prodotto prodotto) throws MyServletException;

    /**
     * Elimina un prodotto dal sistema.
     *
     * <p>Questo metodo permette di rimuovere un prodotto esistente dal sistema
     * utilizzando il suo ID. Una volta eliminato, il prodotto non sarà più
     * disponibile nel database.</p>
     *
     * @param idProdotto l'ID del prodotto da eliminare
     */
    void eliminaProdotto(int idProdotto);
}
