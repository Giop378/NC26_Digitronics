package it.unisa.nc26.digitronics.gestioneProdotto.service;

import it.unisa.nc26.digitronics.model.bean.Prodotto;

public interface ModificaProdottoService {
    void modificaProdotto(Prodotto prodotto) throws Exception;
    public void aggiungiProdotto(Prodotto prodotto) throws Exception;
    void eliminaProdotto(int idProdotto);
}
