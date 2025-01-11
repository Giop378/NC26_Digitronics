package it.unisa.nc26.digitronics.model.bean;

/**
 * Rappresenta una recensione effettuata su un prodotto.
 */
public class Recensione {

    /** ID univoco della recensione. */
    private int idRecensione;

    /** Titolo della recensione. */
    private String titolo;

    /** Descrizione o contenuto testuale della recensione. */
    private String descrizione;

    /** Punteggio assegnato al prodotto (ad esempio, da 1 a 5). */
    private int punteggio;

    /** ID del prodotto a cui è associata la recensione. */
    private int idProdotto;

    /** ID dell'utente che ha scritto la recensione. */
    private int idUtente;

    /** Oggetto {@link Utente} che rappresenta l'utente che ha scritto la recensione. */
    private Utente utente;

    /**
     * Restituisce l'ID della recensione.
     *
     * @return ID della recensione.
     */
    public int getIdRecensione() {
        return idRecensione;
    }

    /**
     * Imposta l'ID della recensione.
     *
     * @param idRecensione Nuovo ID della recensione.
     */
    public void setIdRecensione(int idRecensione) {
        this.idRecensione = idRecensione;
    }

    /**
     * Restituisce il titolo della recensione.
     *
     * @return Titolo della recensione.
     */
    public String getTitolo() {
        return titolo;
    }

    /**
     * Imposta il titolo della recensione.
     *
     * @param titolo Nuovo titolo della recensione.
     */
    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    /**
     * Restituisce la descrizione della recensione.
     *
     * @return Descrizione della recensione.
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * Imposta la descrizione della recensione.
     *
     * @param descrizione Nuova descrizione della recensione.
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * Restituisce il punteggio assegnato nella recensione.
     *
     * @return Punteggio della recensione.
     */
    public int getPunteggio() {
        return punteggio;
    }

    /**
     * Imposta il punteggio della recensione.
     *
     * @param punteggio Nuovo punteggio della recensione.
     */
    public void setPunteggio(int punteggio) {
        this.punteggio = punteggio;
    }

    /**
     * Restituisce l'ID del prodotto recensito.
     *
     * @return ID del prodotto.
     */
    public int getIdProdotto() {
        return idProdotto;
    }

    /**
     * Imposta l'ID del prodotto recensito.
     *
     * @param idProdotto Nuovo ID del prodotto.
     */
    public void setIdProdotto(int idProdotto) {
        this.idProdotto = idProdotto;
    }

    /**
     * Restituisce l'ID dell'utente che ha scritto la recensione.
     *
     * @return ID dell'utente.
     */
    public int getIdUtente() {
        return idUtente;
    }

    /**
     * Imposta l'ID dell'utente che ha scritto la recensione.
     *
     * @param idUtente Nuovo ID dell'utente.
     */
    public void setIdUtente(int idUtente) {
        this.idUtente = idUtente;
    }

    /**
     * Restituisce l'oggetto {@link Utente} che ha scritto la recensione.
     *
     * @return Oggetto {@link Utente}.
     */
    public Utente getUtente() {
        return utente;
    }

    /**
     * Imposta l'oggetto {@link Utente} che ha scritto la recensione.
     *
     * @param utente Nuovo oggetto {@link Utente}.
     */
    public void setUtente(Utente utente) {
        this.utente = utente;
    }
}
