package it.unisa.nc26.digitronics.gestioneRecensione.service;

import it.unisa.nc26.digitronics.model.bean.Prodotto;
import it.unisa.nc26.digitronics.model.bean.Recensione;
import it.unisa.nc26.digitronics.model.bean.Utente;

import java.sql.SQLException;
import java.util.Collection;

public interface RecensioneService {

    void saveRecensione(Recensione recensione) throws SQLException;

    Collection<Recensione> fetchByProduct(int idProdotto) throws SQLException;

    Collection<Recensione> fetchByUtente(int idProdotto) throws SQLException;

    Double avaragePunteggioProdotto(int idProdotto) throws SQLException;


}
