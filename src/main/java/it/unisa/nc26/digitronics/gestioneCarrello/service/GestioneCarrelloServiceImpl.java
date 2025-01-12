package it.unisa.nc26.digitronics.gestioneCarrello.service;

import it.unisa.nc26.digitronics.model.bean.Prodotto;
import it.unisa.nc26.digitronics.model.dao.ItemCarrelloDAO;
import it.unisa.nc26.digitronics.model.dao.ProdottoDAO;
import it.unisa.nc26.digitronics.utils.MyServletException;

/**
 * Implementazione del servizio di gestione del carrello.
 */
public class GestioneCarrelloServiceImpl implements GestioneCarrelloService {
    private ProdottoDAO prodottoDAO;
    private ItemCarrelloDAO itemCarrelloDAO;

    /**
     * Costruttore che inizializza i DAO utilizzati dal servizio.
     */
    public GestioneCarrelloServiceImpl() {
        this.prodottoDAO = new ProdottoDAO();
        this.itemCarrelloDAO = new ItemCarrelloDAO();
    }

    /**
     * Imposta un'istanza personalizzata di {@link ProdottoDAO}.
     *
     * @param prodottoDAO il DAO dei prodotti da utilizzare
     */
    public void setProdottoDAO(ProdottoDAO prodottoDAO) {
        this.prodottoDAO = prodottoDAO;
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
     * Recupera un prodotto dal database tramite il suo ID.
     *
     * @param idProdotto l'ID del prodotto da recuperare
     * @return l'oggetto {@link Prodotto} corrispondente
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
}

