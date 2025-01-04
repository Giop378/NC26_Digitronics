package it.unisa.nc26.digitronics.infoProdotto.service;

import it.unisa.nc26.digitronics.model.bean.Categoria;
import it.unisa.nc26.digitronics.model.bean.Prodotto;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public interface ProdottoService {
    Prodotto fetchByIdProdotto(int idProdotto) throws SQLException;
    List<Prodotto> cercaProdottoPerNome(String query);
    List<Prodotto> getProdottiPerCategoria(String nomeCategoria);
    Categoria getCategoriaPerNome(String nomeCategoria);
    List<Categoria> getAllCategorie();

}
