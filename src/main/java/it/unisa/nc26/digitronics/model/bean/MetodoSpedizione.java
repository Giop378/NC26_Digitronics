package it.unisa.nc26.digitronics.model.bean;

/**
 * Rappresenta un metodo di spedizione per gli ordini.
 *
 * <p>La classe {@code MetodoSpedizione} memorizza le informazioni relative
 * a un metodo di spedizione, inclusi il nome, la descrizione e il costo
 * associato al servizio di spedizione.</p>
 *
 * <p>Un'istanza di {@code MetodoSpedizione} rappresenta un'opzione di
 * spedizione disponibile per gli utenti durante il processo di acquisto.</p>
 */
public class MetodoSpedizione {
    private String nome;
    private String descrizione;
    private double costo;

    /**
     * Restituisce il nome del metodo di spedizione.
     *
     * @return il nome del metodo di spedizione
     */
    public String getNome() {
        return nome;
    }

    /**
     * Imposta il nome del metodo di spedizione.
     *
     * @param nome il nome da associare al metodo di spedizione
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Restituisce la descrizione del metodo di spedizione.
     *
     * @return la descrizione del metodo di spedizione
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * Imposta la descrizione del metodo di spedizione.
     *
     * @param descrizione la descrizione da associare al metodo di spedizione
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * Restituisce il costo del metodo di spedizione.
     *
     * @return il costo del metodo di spedizione
     */
    public double getCosto() {
        return costo;
    }

    /**
     * Imposta il costo del metodo di spedizione.
     *
     * @param costo il costo da associare al metodo di spedizione
     */
    public void setCosto(double costo) {
        this.costo = costo;
    }
}

