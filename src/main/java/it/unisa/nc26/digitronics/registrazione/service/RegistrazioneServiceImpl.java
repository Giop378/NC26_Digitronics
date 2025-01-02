package it.unisa.nc26.digitronics.registrazione.service;

import it.unisa.nc26.digitronics.model.bean.Utente;
import it.unisa.nc26.digitronics.model.dao.UtenteDAO;

public class RegistrazioneServiceImpl implements RegistrazioneService {
    private UtenteDAO utenteDAO;

    public RegistrazioneServiceImpl() {
        utenteDAO = new UtenteDAO();
    }

    public void setUtenteDAO(UtenteDAO utenteDAO) {
        this.utenteDAO = utenteDAO;
    }

    @Override
    public int registraUtente(Utente utente) {
        return utenteDAO.doSave(utente);
    }
}
