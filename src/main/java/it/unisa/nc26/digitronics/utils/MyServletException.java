package it.unisa.nc26.digitronics.utils;

import jakarta.servlet.ServletException;

/**
 * Classe per la gestione di eccezioni personalizzate nelle servlet.
 *
 * Questa classe estende {@link ServletException} e consente di definire
 * eccezioni specifiche per l'applicazione, migliorando la gestione degli errori.
 */
public class MyServletException extends ServletException {

    /**
     * Costruttore di default che richiama il costruttore della superclasse.
     */
    public MyServletException() {
        super();
    }

    /**
     * Costruttore che consente di specificare un messaggio di errore personalizzato.
     *
     * @param message il messaggio di errore da associare all'eccezione
     */
    public MyServletException(String message) {
        super(message);
    }
}