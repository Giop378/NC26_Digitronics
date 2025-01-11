package it.unisa.nc26.digitronics.model.bean;

/**
 * Rappresenta un prodotto disponibile nel sistema.
 */
public class Prodotto {

    /** ID univoco del prodotto. */
    private int idProdotto;

    /** Nome del prodotto. */
    private String nome;

    /** Prezzo del prodotto. */
    private double prezzo;

    /** Descrizione del prodotto. */
    private String descrizione;

    /** Percorso o URL dell'immagine associata al prodotto. */
    private String immagine;

    /** Indica se il prodotto è in vetrina. */
    private boolean vetrina;

    /** Quantità disponibile in magazzino del prodotto. */
    private int quantità;

    /** Nome della categoria a cui appartiene il prodotto. */
    private String nomeCategoria;

    /**
     * Restituisce l'ID del prodotto.
     *
     * @return ID univoco del prodotto.
     */
    public int getIdProdotto() {
        return idProdotto;
    }

    /**
     * Imposta l'ID del prodotto.
     *
     * @param idProdotto Nuovo ID del prodotto.
     */
    public void setIdProdotto(int idProdotto) {
        this.idProdotto = idProdotto;
    }

    /**
     * Restituisce il nome del prodotto.
     *
     * @return Nome del prodotto.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Imposta il nome del prodotto.
     *
     * @param nome Nuovo nome del prodotto.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Restituisce il prezzo del prodotto.
     *
     * @return Prezzo del prodotto.
     */
    public double getPrezzo() {
        return prezzo;
    }

    /**
     * Imposta il prezzo del prodotto.
     *
     * @param prezzo Nuovo prezzo del prodotto.
     */
    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }

    /**
     * Restituisce la descrizione del prodotto.
     *
     * @return Descrizione del prodotto.
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * Imposta la descrizione del prodotto.
     *
     * @param descrizione Nuova descrizione del prodotto.
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * Restituisce l'immagine associata al prodotto.
     *
     * @return Percorso o URL dell'immagine.
     */
    public String getImmagine() {
        return immagine;
    }

    /**
     * Imposta l'immagine associata al prodotto.
     *
     * @param immagine Nuovo percorso o URL dell'immagine.
     */
    public void setImmagine(String immagine) {
        this.immagine = immagine;
    }

    /**
     * Indica se il prodotto è in vetrina.
     *
     * @return {@code true} se il prodotto è in vetrina, {@code false} altrimenti.
     */
    public boolean isVetrina() {
        return vetrina;
    }

    /**
     * Imposta se il prodotto è in vetrina.
     *
     * @param vetrina {@code true} per mettere il prodotto in vetrina, {@code false} altrimenti.
     */
    public void setVetrina(boolean vetrina) {
        this.vetrina = vetrina;
    }

    /**
     * Restituisce la quantità disponibile del prodotto.
     *
     * @return Quantità disponibile in magazzino.
     */
    public int getQuantità() {
        return quantità;
    }

    /**
     * Imposta la quantità disponibile del prodotto.
     *
     * @param quantità Nuova quantità disponibile.
     */
    public void setQuantità(int quantità) {
        this.quantità = quantità;
    }

    /**
     * Restituisce il nome della categoria a cui appartiene il prodotto.
     *
     * @return Nome della categoria.
     */
    public String getNomeCategoria() {
        return nomeCategoria;
    }

    /**
     * Imposta il nome della categoria a cui appartiene il prodotto.
     *
     * @param nomeCategoria Nuovo nome della categoria.
     */
    public void setNomeCategoria(String nomeCategoria) {
        this.nomeCategoria = nomeCategoria;
    }
}
