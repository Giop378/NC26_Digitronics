package it.unisa.nc26.digitronics.infoProdotto.service;

import it.unisa.nc26.digitronics.model.bean.Categoria;
import it.unisa.nc26.digitronics.model.bean.Prodotto;
import it.unisa.nc26.digitronics.model.dao.CategoriaDAO;
import it.unisa.nc26.digitronics.model.dao.ProdottoDAO;

import java.sql.SQLException;
import java.util.List;

public class infoProdottoServiceImpl implements infoProdottoService {
    ProdottoDAO prodottoDAO;
    CategoriaDAO categoriaDAO;

    public infoProdottoServiceImpl() {
        prodottoDAO = new ProdottoDAO();
        categoriaDAO = new CategoriaDAO();
    }

    public void setProdottoDAO(ProdottoDAO prodottoDAO) {
        this.prodottoDAO = prodottoDAO;
    }

    public void setCategoriaDAO(CategoriaDAO categoriaDAO) {
        this.categoriaDAO = categoriaDAO;
    }

    @Override
    public Prodotto fetchByIdProdotto(int idProdotto) throws SQLException {
        return prodottoDAO.doRetrieveById(idProdotto);
    }

    @Override
    public List<Prodotto> cercaProdottoPerNome(String query) {
        return prodottoDAO.doRetrieveByName(query);
    }

    @Override
    public List<Prodotto> getProdottiPerCategoria(String nomeCategoria) {
        return prodottoDAO.doRetrieveByCategory(nomeCategoria);
    }

    @Override
    public Categoria getCategoriaPerNome(String nomeCategoria) {
        return categoriaDAO.doRetrieveByNomeCategoria(nomeCategoria);
    }

    @Override
    public List<Categoria> getAllCategorie() {
        return categoriaDAO.doRetrieveAll();
    }
}
