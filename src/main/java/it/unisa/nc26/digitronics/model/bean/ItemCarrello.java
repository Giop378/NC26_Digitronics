package it.unisa.nc26.digitronics.model.bean;

/**
 * Rappresenta un elemento nel carrello di un utente.
 *
 * <p>La classe {@code ItemCarrello} memorizza le informazioni relative a un
 * prodotto aggiunto al carrello, inclusi l'ID dell'utente, l'ID del prodotto,
 * la quantità del prodotto nel carrello e l'oggetto {@link Prodotto} associato.</p>
 *
 * <p>Un'istanza di {@code ItemCarrello} rappresenta una singola voce nel carrello
 * di acquisto di un utente, contenente informazioni sia sul prodotto che sulla
 * quantità selezionata dall'utente.</p>
 */
public class ItemCarrello {

    private Integer idUtente;
    private int idProdotto;
    private int quantità;
    private Prodotto prodotto;

    /**
     * Restituisce l'ID dell'utente che possiede l'elemento nel carrello.
     *
     * @return l'ID dell'utente
     */
    public Integer getIdUtente() {
        return idUtente;
    }

    /**
     * Imposta l'ID dell'utente che possiede l'elemento nel carrello.
     *
     * @param idUtente l'ID dell'utente da associare all'elemento del carrello
     */
    public void setIdUtente(Integer idUtente) {
        this.idUtente = idUtente;
    }

    /**
     * Restituisce l'ID del prodotto nell'elemento del carrello.
     *
     * @return l'ID del prodotto
     */
    public int getIdProdotto() {
        return idProdotto;
    }

    /**
     * Imposta l'ID del prodotto nell'elemento del carrello.
     *
     * @param idProdotto l'ID del prodotto da associare all'elemento del carrello
     */
    public void setIdProdotto(int idProdotto) {
        this.idProdotto = idProdotto;
    }

    /**
     * Restituisce la quantità del prodotto nell'elemento del carrello.
     *
     * @return la quantità del prodotto nel carrello
     */
    public int getQuantità() {
        return quantità;
    }

    /**
     * Imposta la quantità del prodotto nell'elemento del carrello.
     *
     * @param quantità la quantità da assegnare al prodotto nel carrello
     */
    public void setQuantità(int quantità) {
        this.quantità = quantità;
    }

    /**
     * Restituisce l'oggetto {@link Prodotto} associato all'elemento del carrello.
     *
     * @return il prodotto nell'elemento del carrello
     */
    public Prodotto getProdotto() {
        return prodotto;
    }

    /**
     * Imposta l'oggetto {@link Prodotto} associato all'elemento del carrello.
     *
     * @param prodotto il prodotto da associare all'elemento del carrello
     */
    public void setProdotto(Prodotto prodotto) {
        this.prodotto = prodotto;
    }
}

