package it.unisa.nc26.digitronics.gestioneCarrello.service;

import it.unisa.nc26.digitronics.model.bean.Prodotto;
import it.unisa.nc26.digitronics.model.dao.ItemCarrelloDAO;
import it.unisa.nc26.digitronics.model.dao.ProdottoDAO;

public class GestioneCarrelloServiceImpl implements GestioneCarrelloService{
    private ProdottoDAO prodottoDAO;
    private ItemCarrelloDAO itemCarrelloDAO;

    public GestioneCarrelloServiceImpl() {
        this.prodottoDAO= new ProdottoDAO();
        this.itemCarrelloDAO= new ItemCarrelloDAO();
    }

    public void setProdottoDAO(ProdottoDAO prodottoDAO) {
        this.prodottoDAO = prodottoDAO;
    }

    public void setItemCarrelloDAO(ItemCarrelloDAO itemCarrelloDAO) {
        this.itemCarrelloDAO = itemCarrelloDAO;
    }

    @Override
    public Prodotto fetchByIdProdotto(int idProdotto) {
        return prodottoDAO.doRetrieveById(idProdotto);
    }
}
