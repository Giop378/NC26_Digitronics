package it.unisa.nc26.digitronics.model.bean;

public class ItemOrdine {
    private int idItemOrdine;
    private String nome;
    private String immagine;
    private double prezzo;
    private int quantità;
    private int idProdotto;
    private int idOrdine;

    public int getIdItemOrdine() {
        return idItemOrdine;
    }

    public void setIdItemOrdine(int idItemOrdine) {
        this.idItemOrdine = idItemOrdine;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getImmagine() {
        return immagine;
    }

    public void setImmagine(String immagine) {
        this.immagine = immagine;
    }

    public double getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }

    public int getQuantità() {
        return quantità;
    }

    public void setQuantità(int quantità) {
        this.quantità = quantità;
    }

    public int getIdProdotto() {
        return idProdotto;
    }

    public void setIdProdotto(int idProdotto) {
        this.idProdotto = idProdotto;
    }

    public int getIdOrdine() {
        return idOrdine;
    }

    public void setIdOrdine(int idOrdine) {
        this.idOrdine = idOrdine;
    }
}
