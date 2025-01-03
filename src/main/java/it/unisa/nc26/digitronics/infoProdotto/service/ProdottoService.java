package it.unisa.nc26.digitronics.infoProdotto.service;

import it.unisa.nc26.digitronics.model.bean.Prodotto;

import java.sql.SQLException;
import java.util.Collection;

public interface ProdottoService {
    Prodotto fetchByIdProdotto(int idProdotto) throws SQLException;

}
