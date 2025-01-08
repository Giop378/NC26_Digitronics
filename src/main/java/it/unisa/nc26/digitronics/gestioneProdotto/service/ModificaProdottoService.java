package it.unisa.nc26.digitronics.gestioneProdotto.service;

import it.unisa.nc26.digitronics.model.bean.Prodotto;
import it.unisa.nc26.digitronics.utils.MyServletException;

public interface ModificaProdottoService {
    void modificaProdotto(Prodotto prodotto) throws MyServletException;
    public void aggiungiProdotto(Prodotto prodotto) throws MyServletException;
    void eliminaProdotto(int idProdotto);
}
