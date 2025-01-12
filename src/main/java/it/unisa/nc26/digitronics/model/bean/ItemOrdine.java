package it.unisa.nc26.digitronics.model.bean;

/**
 * Classe che rappresenta un elemento di un ordine.
 */
public class ItemOrdine {
    private int idItemOrdine;
    private String nome;
    private String immagine;
    private double prezzo;
    private int quantità;
    private int idProdotto;
    private int idOrdine;

    /**
     * Restituisce l'ID dell'item ordine.
     *
     * @return l'ID dell'item ordine
     */
    public int getIdItemOrdine() {
        return idItemOrdine;
    }

    /**
     * Imposta l'ID dell'item ordine.
     *
     * @param idItemOrdine l'ID dell'item ordine
     */
    public void setIdItemOrdine(int idItemOrdine) {
        this.idItemOrdine = idItemOrdine;
    }

    /**
     * Restituisce il nome dell'item ordine.
     *
     * @return il nome dell'item ordine
     */
    public String getNome() {
        return nome;
    }

    /**
     * Imposta il nome dell'item ordine.
     *
     * @param nome il nome dell'item ordine
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Restituisce l'immagine associata all'item ordine.
     *
     * @return l'immagine associata all'item ordine
     */
    public String getImmagine() {
        return immagine;
    }

    /**
     * Imposta l'immagine associata all'item ordine.
     *
     * @param immagine l'immagine associata all'item ordine
     */
    public void setImmagine(String immagine) {
        this.immagine = immagine;
    }

    /**
     * Restituisce il prezzo dell'item ordine.
     *
     * @return il prezzo dell'item ordine
     */
    public double getPrezzo() {
        return prezzo;
    }

    /**
     * Imposta il prezzo dell'item ordine.
     *
     * @param prezzo il prezzo dell'item ordine
     */
    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }

    /**
     * Restituisce la quantità dell'item ordine.
     *
     * @return la quantità dell'item ordine
     */
    public int getQuantità() {
        return quantità;
    }

    /**
     * Imposta la quantità dell'item ordine.
     *
     * @param quantità la quantità dell'item ordine
     */
    public void setQuantità(int quantità) {
        this.quantità = quantità;
    }

    /**
     * Restituisce l'ID del prodotto associato all'item ordine.
     *
     * @return l'ID del prodotto associato all'item ordine
     */
    public int getIdProdotto() {
        return idProdotto;
    }

    /**
     * Imposta l'ID del prodotto associato all'item ordine.
     *
     * @param idProdotto l'ID del prodotto associato all'item ordine
     */
    public void setIdProdotto(int idProdotto) {
        this.idProdotto = idProdotto;
    }

    /**
     * Restituisce l'ID dell'ordine a cui l'item appartiene.
     *
     * @return l'ID dell'ordine a cui l'item appartiene
     */
    public int getIdOrdine() {
        return idOrdine;
    }

    /**
     * Imposta l'ID dell'ordine a cui l'item appartiene.
     *
     * @param idOrdine l'ID dell'ordine a cui l'item appartiene
     */
    public void setIdOrdine(int idOrdine) {
        this.idOrdine = idOrdine;
    }
}
