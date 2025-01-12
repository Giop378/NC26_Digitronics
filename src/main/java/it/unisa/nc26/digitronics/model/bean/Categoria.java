package it.unisa.nc26.digitronics.model.bean;

/**
 * La classe Categoria rappresenta una categoria con un nome, una descrizione e un'immagine associata.
 * Viene utilizzata per gestire informazioni relative a categorie in un contesto applicativo.
 */
public class Categoria {

    /**
     * Il nome della categoria.
     */
    private String nome;

    /**
     * La descrizione della categoria.
     */
    private String descrizione;

    /**
     * Il percorso o riferimento all'immagine associata alla categoria.
     */
    private String immagine;

    /**
     * Restituisce il nome della categoria.
     *
     * @return il nome della categoria.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Imposta il nome della categoria.
     *
     * @param nome il nome della categoria.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Restituisce la descrizione della categoria.
     *
     * @return la descrizione della categoria.
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * Imposta la descrizione della categoria.
     *
     * @param descrizione la descrizione della categoria.
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * Restituisce l'immagine associata alla categoria.
     *
     * @return l'immagine della categoria.
     */
    public String getImmagine() {
        return immagine;
    }

    /**
     * Imposta l'immagine associata alla categoria.
     *
     * @param immagine il percorso o riferimento all'immagine della categoria.
     */
    public void setImmagine(String immagine) {
        this.immagine = immagine;
    }
}
