package it.unisa.nc26.digitronics.infoProdotto.service;

import it.unisa.nc26.digitronics.model.bean.Categoria;
import it.unisa.nc26.digitronics.model.bean.Prodotto;
import it.unisa.nc26.digitronics.model.bean.Recensione;

import java.sql.SQLException;
import java.util.List;

/**
 * Interfaccia per la gestione delle informazioni relative ai prodotti.
 *
 * Fornisce metodi per il recupero di dettagli, ricerche e recensioni relative a un prodotto,
 * oltre a gestire le categorie dei prodotti.
 */
public interface InfoProdottoService {

    /**
     * Recupera un prodotto in base al suo ID.
     *
     * @param idProdotto l'identificativo del prodotto da recuperare
     * @return il prodotto corrispondente all'ID specificato
     */
    Prodotto fetchByIdProdotto(int idProdotto);

    /**
     * Cerca prodotti il cui nome corrisponde alla query fornita.
     *
     * @param query la stringa di ricerca
     * @return la lista dei prodotti che corrispondono alla query
     */
    List<Prodotto> cercaProdottoPerNome(String query);

    /**
     * Recupera i prodotti che appartengono alla categoria specificata.
     *
     * @param nomeCategoria il nome della categoria
     * @return la lista dei prodotti associati alla categoria
     */
    List<Prodotto> getProdottiPerCategoria(String nomeCategoria);

    /**
     * Recupera una categoria in base al nome.
     *
     * @param nomeCategoria il nome della categoria da recuperare
     * @return la categoria corrispondente al nome specificato
     */
    Categoria getCategoriaPerNome(String nomeCategoria);

    /**
     * Recupera tutte le categorie presenti nel sistema.
     *
     * @return la lista di tutte le categorie
     */
    List<Categoria> getAllCategorie();

    /**
     * Recupera le recensioni associate ad un prodotto.
     *
     * @param idProdotto l'identificativo del prodotto
     * @return la lista delle recensioni associate al prodotto
     * @throws SQLException se si verifica un errore durante il recupero dei dati dal database
     */
    List<Recensione> fetchRecensioniByIdProdotto(int idProdotto) throws SQLException;

    /**
     * Recupera i prodotti in vetrina.
     *
     * @return la lista dei prodotti in vetrina
     */
    List<Prodotto> fetchProdottoVetrina();
}
