package it.unisa.nc26.digitronics.gestioneCarrello.service;

import it.unisa.nc26.digitronics.model.bean.Prodotto;

public interface GestioneCarrelloService {
    Prodotto fetchByIdProdotto(int idProdotto);
}
