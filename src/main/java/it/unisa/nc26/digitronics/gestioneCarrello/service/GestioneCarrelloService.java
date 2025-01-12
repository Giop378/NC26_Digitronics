package it.unisa.nc26.digitronics.gestioneCarrello.service;

import it.unisa.nc26.digitronics.model.bean.Prodotto;
import it.unisa.nc26.digitronics.utils.MyServletException;

public interface GestioneCarrelloService {
    Prodotto fetchByIdProdotto(int idProdotto) throws MyServletException;
}
