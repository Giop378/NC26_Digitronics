package it.unisa.nc26.digitronics.infoProdotto.service;

import it.unisa.nc26.digitronics.model.bean.Prodotto;

import java.util.List;

public interface ProdottoRicercaService {
    List<Prodotto> cercaProdottoPerNome(String query);
}
