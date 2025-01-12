package it.unisa.nc26.digitronics.model.bean;

/**
 * Rappresenta una categoria di prodotto nel sistema.
 *
 * <p>La classe {@code Categoria} contiene le informazioni relative a una
 * categoria di prodotto, tra cui il nome, la descrizione e l'immagine.</p>
 *
 * <p>Un'istanza di {@code Categoria} può essere utilizzata per rappresentare
 * una categoria di prodotto, che può essere associata a uno o più prodotti
 * nel sistema.</p>
 */
public class Categoria {

    private String nome;
    private String descrizione;
    private String immagine;

    /**
     * Restituisce il nome della categoria.
     *
     * @return il nome della categoria
     */
    public String getNome() {
        return nome;
    }

    /**
     * Imposta il nome della categoria.
     *
     * @param nome il nome da assegnare alla categoria
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Restituisce la descrizione della categoria.
     *
     * @return la descrizione della categoria
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * Imposta la descrizione della categoria.
     *
     * @param descrizione la descrizione da assegnare alla categoria
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * Restituisce l'immagine associata alla categoria.
     *
     * @return l'immagine della categoria
     */
    public String getImmagine() {
        return immagine;
    }

    /**
     * Imposta l'immagine associata alla categoria.
     *
     * @param immagine l'immagine da assegnare alla categoria
     */
    public void setImmagine(String immagine) {
        this.immagine = immagine;
    }
}

