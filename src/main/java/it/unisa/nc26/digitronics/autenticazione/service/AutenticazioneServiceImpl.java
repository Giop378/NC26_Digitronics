package it.unisa.nc26.digitronics.autenticazione.service;

import it.unisa.nc26.digitronics.model.bean.ItemCarrello;
import it.unisa.nc26.digitronics.model.bean.Utente;
import it.unisa.nc26.digitronics.model.dao.ItemCarrelloDAO;
import it.unisa.nc26.digitronics.model.dao.UtenteDAO;

import java.sql.SQLException;
import java.util.List;

public class AutenticazioneServiceImpl implements AutenticazioneService{
    private UtenteDAO utenteDAO;
    private ItemCarrelloDAO itemCarrelloDAO;

    public AutenticazioneServiceImpl() {
        utenteDAO = new UtenteDAO();
        itemCarrelloDAO = new ItemCarrelloDAO();
    }

    public void setUtenteDAO(UtenteDAO utenteDAO) {
        this.utenteDAO = utenteDAO;
    }

    public void setItemCarrelloDAO(ItemCarrelloDAO itemCarrelloDAO) {
        this.itemCarrelloDAO = itemCarrelloDAO;
    }

    @Override
    public Utente retrieveUtenteEmailPassword(String email, String password) {
        return utenteDAO.doRetrieveByEmailPassword(email, password);
    }

    @Override
    public List<ItemCarrello> retrieveCarrelloUtente(int id) {
        return itemCarrelloDAO.doRetrieveByIdUtente(id);
    }

    @Override
    public void clearCarrelloUtente(int idUtente) throws SQLException {
        itemCarrelloDAO.doDelete(idUtente);
    }

    @Override
    public void saveCarrelloUtente(List<ItemCarrello> carrelli) throws SQLException {
        itemCarrelloDAO.doSave(carrelli);
    }
}
