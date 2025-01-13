package it.unisa.nc26.digitronics.registrazione.service;

import it.unisa.nc26.digitronics.model.bean.Utente;

/**
 * Interfaccia che definisce il servizio di registrazione degli utenti.
 */
public interface RegistrazioneService {

    /**
     * Registra un utente nel sistema.
     *
     * @param utente L'utente da registrare
     * @return Un valore intero che rappresenta l'esito della registrazione
     */
    int registraUtente(Utente utente);
}
