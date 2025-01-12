package it.unisa.nc26.digitronics.gestioneOrdine.service.IndirizzoAdapter;

/**
 * Interfaccia per la verifica degli indirizzi tramite API esterne.
 * Fornisce un metodo per controllare se un indirizzo specificato è valido.
 */
public interface VerificaIndirizzoApiAdapter {

    /**
     * Verifica se un indirizzo composto da via, CAP e città è valido utilizzando un'API esterna.
     *
     * @param via   il nome della via
     * @param cap   il codice di avviamento postale (CAP)
     * @param città il nome della città
     * @return true se l'indirizzo è valido, false altrimenti
     */
    boolean verifica(String via, String cap, String città);
}

