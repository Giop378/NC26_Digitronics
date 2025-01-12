package it.unisa.nc26.digitronics.model.bean;

import java.time.LocalDate;
import java.sql.Date;
import java.util.Objects;

/**
 * Classe che rappresenta un ordine effettuato da un utente.
 */
public class Ordine {
    private int idOrdine;
    private double totale;
    private int idUtente;
    private String cap;
    private String numeroCivico;
    private String nome;
    private String cognome;
    private String via;
    private String telefono;
    private String nomeMetodoSpedizione;
    private Date dataOrdine;
    private String città;

    /**
     * Restituisce l'ID dell'ordine.
     *
     * @return l'ID dell'ordine
     */
    public int getIdOrdine() {
        return idOrdine;
    }

    /**
     * Imposta l'ID dell'ordine.
     *
     * @param idOrdine l'ID dell'ordine
     */
    public void setIdOrdine(int idOrdine) {
        this.idOrdine = idOrdine;
    }

    /**
     * Restituisce il totale dell'ordine.
     *
     * @return il totale dell'ordine
     */
    public double getTotale() {
        return totale;
    }

    /**
     * Imposta il totale dell'ordine.
     *
     * @param totale il totale dell'ordine
     */
    public void setTotale(double totale) {
        this.totale = totale;
    }

    /**
     * Restituisce l'ID dell'utente che ha effettuato l'ordine.
     *
     * @return l'ID dell'utente
     */
    public int getIdUtente() {
        return idUtente;
    }

    /**
     * Imposta l'ID dell'utente che ha effettuato l'ordine.
     *
     * @param idUtente l'ID dell'utente
     */
    public void setIdUtente(int idUtente) {
        this.idUtente = idUtente;
    }

    /**
     * Restituisce il CAP dell'indirizzo di spedizione.
     *
     * @return il CAP dell'indirizzo
     */
    public String getCap() {
        return cap;
    }

    /**
     * Imposta il CAP dell'indirizzo di spedizione.
     *
     * @param cap il CAP dell'indirizzo
     */
    public void setCap(String cap) {
        this.cap = cap;
    }

    /**
     * Restituisce il numero civico dell'indirizzo di spedizione.
     *
     * @return il numero civico dell'indirizzo
     */
    public String getNumeroCivico() {
        return numeroCivico;
    }

    /**
     * Imposta il numero civico dell'indirizzo di spedizione.
     *
     * @param numeroCivico il numero civico dell'indirizzo
     */
    public void setNumeroCivico(String numeroCivico) {
        this.numeroCivico = numeroCivico;
    }

    /**
     * Restituisce il nome del destinatario dell'ordine.
     *
     * @return il nome del destinatario
     */
    public String getNome() {
        return nome;
    }

    /**
     * Imposta il nome del destinatario dell'ordine.
     *
     * @param nome il nome del destinatario
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Restituisce il cognome del destinatario dell'ordine.
     *
     * @return il cognome del destinatario
     */
    public String getCognome() {
        return cognome;
    }

    /**
     * Imposta il cognome del destinatario dell'ordine.
     *
     * @param cognome il cognome del destinatario
     */
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    /**
     * Restituisce la via dell'indirizzo di spedizione.
     *
     * @return la via dell'indirizzo
     */
    public String getVia() {
        return via;
    }

    /**
     * Imposta la via dell'indirizzo di spedizione.
     *
     * @param via la via dell'indirizzo
     */
    public void setVia(String via) {
        this.via = via;
    }

    /**
     * Restituisce il numero di telefono del destinatario.
     *
     * @return il numero di telefono del destinatario
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Imposta il numero di telefono del destinatario.
     *
     * @param telefono il numero di telefono del destinatario
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * Restituisce il nome del metodo di spedizione utilizzato.
     *
     * @return il nome del metodo di spedizione
     */
    public String getNomeMetodoSpedizione() {
        return nomeMetodoSpedizione;
    }

    /**
     * Imposta il nome del metodo di spedizione utilizzato.
     *
     * @param nomeMetodoSpedizione il nome del metodo di spedizione
     */
    public void setNomeMetodoSpedizione(String nomeMetodoSpedizione) {
        this.nomeMetodoSpedizione = nomeMetodoSpedizione;
    }

    /**
     * Restituisce la data dell'ordine.
     *
     * @return la data dell'ordine
     */
    public Date getDataOrdine() {
        return dataOrdine;
    }

    /**
     * Imposta la data dell'ordine.
     *
     * @param dataOrdine la data dell'ordine
     */
    public void setDataOrdine(Date dataOrdine) {
        this.dataOrdine = dataOrdine;
    }

    /**
     * Restituisce la città dell'indirizzo di spedizione.
     *
     * @return la città dell'indirizzo
     */
    public String getCittà() {
        return città;
    }

    /**
     * Imposta la città dell'indirizzo di spedizione.
     *
     * @param città la città dell'indirizzo
     */
    public void setCittà(String città) {
        this.città = città;
    }

    /**
     * Confronta l'oggetto corrente con un altro oggetto per verificare l'uguaglianza basata sull'ID dell'ordine.
     *
     * @param o l'oggetto con cui confrontare
     * @return true se gli oggetti sono uguali, altrimenti false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ordine ordine = (Ordine) o;
        return idOrdine == ordine.idOrdine;
    }

    /**
     * Restituisce il valore hash dell'oggetto, calcolato utilizzando l'ID dell'ordine.
     *
     * @return il valore hash dell'oggetto
     */
    @Override
    public int hashCode() {
        return Objects.hash(idOrdine);
    }
}
