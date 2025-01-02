package it.unisa.nc26.digitronics.autenticazione.service;

import it.unisa.nc26.digitronics.model.bean.ItemCarrello;
import it.unisa.nc26.digitronics.model.bean.Utente;

import java.util.List;

public interface AutenticazioneService {
    Utente retrieveUtenteEmailPassword(String email, String password);
    List<ItemCarrello> retrieveCarrelloUtente(int id);
}
