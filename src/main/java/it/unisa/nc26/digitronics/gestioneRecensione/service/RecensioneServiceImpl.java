package it.unisa.nc26.digitronics.gestioneRecensione.service;

import it.unisa.nc26.digitronics.gestioneRecensione.service.RecensioneService;
import it.unisa.nc26.digitronics.model.bean.Recensione;
import it.unisa.nc26.digitronics.model.dao.RecensioneDAO;

import java.sql.SQLException;
import java.util.Collection;

public class RecensioneServiceImpl implements RecensioneService {

    RecensioneDAO recensioneDAO;

    public RecensioneServiceImpl() {
        recensioneDAO = new RecensioneDAO();
    }

    public void setRecensioneDAO(RecensioneDAO recensioneDAO) {
        this.recensioneDAO = recensioneDAO;
    }

    @Override
    public void saveRecensione(Recensione recensione) throws SQLException {
        recensioneDAO.doSave(recensione);
    }

    @Override
    public Collection<Recensione> fetchByProduct(int idProdotto) throws SQLException {
        return recensioneDAO.doRetrieveByProduct(idProdotto);
    }

    @Override
    public Collection<Recensione> fetchByUtente(int idProdotto) throws SQLException {
        return recensioneDAO.doRetrieveByUtente(idProdotto);
    }

    @Override
    public Double avaragePunteggioProdotto(int idProdotto) throws SQLException {
        return recensioneDAO.doRetrieveAveragePunteggio(idProdotto);
    }
}
