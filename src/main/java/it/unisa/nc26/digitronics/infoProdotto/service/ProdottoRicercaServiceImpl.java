package it.unisa.nc26.digitronics.infoProdotto.service;

import it.unisa.nc26.digitronics.model.bean.Prodotto;
import it.unisa.nc26.digitronics.model.dao.ProdottoDAO;

import java.util.List;

public class ProdottoRicercaServiceImpl implements ProdottoRicercaService{
    private ProdottoDAO prodottoDAO;

    public ProdottoRicercaServiceImpl() {
        this.prodottoDAO = new ProdottoDAO();
    }

    public void setProdottoDAO(ProdottoDAO prodottoDAO) {
        this.prodottoDAO = prodottoDAO;
    }

    @Override
    public List<Prodotto> cercaProdottoPerNome(String query) {
        return prodottoDAO.doRetrieveByName(query);
    }
}
