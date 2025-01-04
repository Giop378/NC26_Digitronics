package it.unisa.nc26.digitronics.infoProdotto.service;

import it.unisa.nc26.digitronics.model.bean.Prodotto;
import it.unisa.nc26.digitronics.model.dao.ProdottoDAO;

import java.sql.SQLException;
import java.util.Collection;

public class ProdottoServiceImpl implements ProdottoService {
    ProdottoDAO prodottoDAO;

    public ProdottoServiceImpl() {
        prodottoDAO = new ProdottoDAO();
    }

    public void setProdottoDAO(ProdottoDAO prodottoDAO) {
        this.prodottoDAO = prodottoDAO;
    }

    @Override
    public Prodotto fetchByIdProdotto(int idProdotto) throws SQLException {
        return prodottoDAO.doRetrieveById(idProdotto);
    }

}
