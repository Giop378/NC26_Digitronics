package it.unisa.nc26.digitronics.infoProdotto.service;

import it.unisa.nc26.digitronics.model.bean.Categoria;
import it.unisa.nc26.digitronics.model.bean.Prodotto;
import it.unisa.nc26.digitronics.model.bean.Recensione;
import it.unisa.nc26.digitronics.model.dao.CategoriaDAO;
import it.unisa.nc26.digitronics.model.dao.ProdottoDAO;
import it.unisa.nc26.digitronics.model.dao.RecensioneDAO;

import java.sql.SQLException;
import java.util.List;

/**
 * Implementazione del servizio per la gestione delle informazioni dei prodotti.
 *
 * Questa classe fornisce le implementazioni concrete per le operazioni definite
 * nell'interfaccia {@link InfoProdottoService}.
 */
public class InfoProdottoServiceImpl implements InfoProdottoService {

    ProdottoDAO prodottoDAO;
    CategoriaDAO categoriaDAO;
    RecensioneDAO recensioneDAO;

    /**
     * Costruttore di default che inizializza i DAO necessari per le operazioni sui prodotti,
     * le categorie e le recensioni.
     */
    public InfoProdottoServiceImpl() {
        prodottoDAO = new ProdottoDAO();
        categoriaDAO = new CategoriaDAO();
        recensioneDAO = new RecensioneDAO();
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
     * Imposta il DAO per la gestione delle categorie.
     *
     * @param categoriaDAO il DAO da utilizzare per le operazioni sulle categorie
     */
    public void setCategoriaDAO(CategoriaDAO categoriaDAO) {
        this.categoriaDAO = categoriaDAO;
    }

    /**
     * Imposta il DAO per la gestione delle recensioni.
     *
     * @param recensioneDAO il DAO da utilizzare per le operazioni sulle recensioni
     */
    public void setRecensioneDAO(RecensioneDAO recensioneDAO) {
        this.recensioneDAO = recensioneDAO;
    }

    /**
     * Recupera un prodotto in base al suo ID.
     *
     * @param idProdotto l'identificativo del prodotto da recuperare
     * @return il prodotto corrispondente all'ID specificato
     */
    @Override
    public Prodotto fetchByIdProdotto(int idProdotto) {
        return prodottoDAO.doRetrieveById(idProdotto);
    }

    /**
     * Cerca prodotti il cui nome corrisponde alla query fornita.
     *
     * @param query la stringa di ricerca
     * @return la lista dei prodotti che corrispondono alla query
     */
    @Override
    public List<Prodotto> cercaProdottoPerNome(String query) {
        return prodottoDAO.doRetrieveByName(query);
    }

    /**
     * Recupera i prodotti che appartengono alla categoria specificata.
     *
     * @param nomeCategoria il nome della categoria
     * @return la lista dei prodotti associati alla categoria
     */
    @Override
    public List<Prodotto> getProdottiPerCategoria(String nomeCategoria) {
        return prodottoDAO.doRetrieveByCategory(nomeCategoria);
    }

    /**
     * Recupera una categoria in base al nome.
     *
     * @param nomeCategoria il nome della categoria da recuperare
     * @return la categoria corrispondente al nome specificato
     */
    @Override
    public Categoria getCategoriaPerNome(String nomeCategoria) {
        return categoriaDAO.doRetrieveByNomeCategoria(nomeCategoria);
    }

    /**
     * Recupera tutte le categorie presenti nel sistema.
     *
     * @return la lista di tutte le categorie
     */
    @Override
    public List<Categoria> getAllCategorie() {
        return categoriaDAO.doRetrieveAll();
    }

    /**
     * Recupera le recensioni associate ad un prodotto.
     *
     * @param idProdotto l'identificativo del prodotto
     * @return la lista delle recensioni associate al prodotto
     * @throws SQLException se si verifica un errore durante il recupero dei dati dal database
     */
    @Override
    public List<Recensione> fetchRecensioniByIdProdotto(int idProdotto) throws SQLException {
        return (List<Recensione>) recensioneDAO.doRetrieveByProduct(idProdotto);
    }

    /**
     * Recupera i prodotti in vetrina.
     *
     * @return la lista dei prodotti in vetrina
     */
    @Override
    public List<Prodotto> fetchProdottoVetrina() {
        return prodottoDAO.doRetrieveVetrina();
    }
}
