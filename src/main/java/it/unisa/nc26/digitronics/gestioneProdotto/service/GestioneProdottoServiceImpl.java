package it.unisa.nc26.digitronics.gestioneProdotto.service;

import it.unisa.nc26.digitronics.model.bean.Prodotto;
import it.unisa.nc26.digitronics.model.dao.CategoriaDAO;
import it.unisa.nc26.digitronics.model.dao.ProdottoDAO;
import it.unisa.nc26.digitronics.utils.MyServletException;

/**
 * Implementazione del servizio di gestione dei prodotti.
 * Questa classe fornisce l'implementazione concreta dei metodi definiti
 * nell'interfaccia {@link GestioneProdottoService}, eseguendo operazioni
 * di modifica, aggiunta e eliminazione di prodotti nel sistema.
 *
 * <p>La classe interagisce con i DAO {@link CategoriaDAO} e {@link ProdottoDAO}
 * per accedere ai dati relativi ai prodotti e alle categorie nel database.</p>
 *
 * @see GestioneProdottoService
 * @see CategoriaDAO
 * @see ProdottoDAO
 */
public class GestioneProdottoServiceImpl implements GestioneProdottoService {

    private CategoriaDAO categoriaDAO;
    private ProdottoDAO prodottoDAO;

    /**
     * Costruttore di default che inizializza i DAO per interagire con il database.
     */
    public GestioneProdottoServiceImpl() {
        this.categoriaDAO = new CategoriaDAO();
        this.prodottoDAO = new ProdottoDAO();
    }

    /**
     * Imposta il DAO per la gestione delle categorie.
     *
     * @param categoriaDAO il DAO per la gestione delle categorie
     */
    public void setCategoriaDAO(CategoriaDAO categoriaDAO) {
        this.categoriaDAO = categoriaDAO;
    }

    /**
     * Imposta il DAO per la gestione dei prodotti.
     *
     * @param prodottoDAO il DAO per la gestione dei prodotti
     */
    public void setProdottoDAO(ProdottoDAO prodottoDAO) {
        this.prodottoDAO = prodottoDAO;
    }

    /**
     * Modifica un prodotto esistente nel sistema.
     *
     * <p>Il metodo verifica che il prodotto esista nel sistema. Se il prodotto
     * esiste, verifica che la categoria specificata sia valida. Se l'immagine del
     * prodotto non è fornita, mantiene quella esistente. Successivamente, aggiorna
     * il prodotto nel database.</p>
     *
     * @param prodottoModificato il prodotto con le nuove informazioni
     * @throws MyServletException se si verifica un errore durante la modifica,
     *         ad esempio se il prodotto o la categoria non esistono
     */
    @Override
    public void modificaProdotto(Prodotto prodottoModificato) throws MyServletException {
        // Verifica che il prodotto esista
        Prodotto prodottoEsistente = prodottoDAO.doRetrieveById(prodottoModificato.getIdProdotto());
        if (prodottoEsistente == null) {
            throw new MyServletException("Prodotto non esistente");
        }

        // Verifica che la categoria esista
        if (categoriaDAO.doRetrieveByNomeCategoria(prodottoModificato.getNomeCategoria()) == null) {
            throw new MyServletException("Categoria non esistente");
        }

        // Mantieni l'immagine se non fornita
        if (prodottoModificato.getImmagine() == null || prodottoModificato.getImmagine().isEmpty()) {
            prodottoModificato.setImmagine(prodottoEsistente.getImmagine());
        }

        // Aggiorna il prodotto nel database
        prodottoDAO.doUpdate(prodottoModificato);
    }

    /**
     * Aggiunge un nuovo prodotto al sistema.
     *
     * <p>Il metodo verifica che la categoria del prodotto esista. Se la categoria
     * è valida, aggiunge il nuovo prodotto al database.</p>
     *
     * @param prodotto il prodotto da aggiungere
     * @throws MyServletException se si verifica un errore durante l'aggiunta,
     *         ad esempio se la categoria non esiste
     */
    public void aggiungiProdotto(Prodotto prodotto) throws MyServletException {
        // Verifica che la categoria esista
        if (categoriaDAO.doRetrieveByNomeCategoria(prodotto.getNomeCategoria()) == null) {
            throw new MyServletException("Categoria non esistente");
        }

        // Aggiungi il nuovo prodotto al database
        prodottoDAO.doSave(prodotto);
    }

    /**
     * Elimina un prodotto dal sistema.
     *
     * <p>Il metodo rimuove il prodotto dal sistema identificato dal suo ID.
     * Se il prodotto esiste, viene eliminato tramite il DAO {@link ProdottoDAO}.</p>
     *
     * @param idProdotto l'ID del prodotto da eliminare
     */
    @Override
    public void eliminaProdotto(int idProdotto) {
        prodottoDAO.doDelete(idProdotto);
    }
}


