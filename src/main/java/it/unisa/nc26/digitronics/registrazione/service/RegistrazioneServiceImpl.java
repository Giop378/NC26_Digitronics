package it.unisa.nc26.digitronics.registrazione.service;

import it.unisa.nc26.digitronics.model.bean.Utente;
import it.unisa.nc26.digitronics.model.dao.UtenteDAO;

/**
 * Implementazione dell'interfaccia RegistrazioneService.
 * Fornisce la logica per la registrazione degli utenti.
 */
public class RegistrazioneServiceImpl implements RegistrazioneService {
    private UtenteDAO utenteDAO;

    /**
     * Costruttore di default. Inizializza un'istanza di UtenteDAO.
     */
    public RegistrazioneServiceImpl() {
        utenteDAO = new UtenteDAO();
    }

    /**
     * Imposta un'istanza personalizzata di UtenteDAO.
     *
     * @param utenteDAO L'istanza di UtenteDAO da impostare
     */
    public void setUtenteDAO(UtenteDAO utenteDAO) {
        this.utenteDAO = utenteDAO;
    }

    /**
     * Registra un utente nel sistema utilizzando UtenteDAO.
     *
     * @param utente L'utente da registrare
     * @return Un valore intero che rappresenta l'esito della registrazione
     */
    @Override
    public int registraUtente(Utente utente) {
        return utenteDAO.doSave(utente);
    }
}
