package it.unisa.nc26.digitronics.infoProdotto.service;

import it.unisa.nc26.digitronics.model.bean.Categoria;
import it.unisa.nc26.digitronics.model.bean.Prodotto;
import it.unisa.nc26.digitronics.model.dao.CategoriaDAO;
import it.unisa.nc26.digitronics.model.dao.ProdottoDAO;

import java.util.List;

public class CategoriaServiceImpl implements CategoriaService {
    private ProdottoDAO prodottoDAO;
    private CategoriaDAO categoriaDAO;

    public CategoriaServiceImpl() {
        this.prodottoDAO = new ProdottoDAO();
        this.categoriaDAO = new CategoriaDAO();
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

