package it.unisa.nc26.digitronics.model.bean;

import java.time.LocalDate;
import java.sql.Date;
import java.util.Objects;

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
    private String nomeMetodoSpedizione ;
    private Date dataOrdine;
    private String città;

    public int getIdOrdine() {
        return idOrdine;
    }

    public void setIdOrdine(int idOrdine) {
        this.idOrdine = idOrdine;
    }

    public double getTotale() {
        return totale;
    }

    public void setTotale(double totale) {
        this.totale = totale;
    }

    public int getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(int idUtente) {
        this.idUtente = idUtente;
    }

    public String getCap() {
        return cap;
    }

    public void setCap(String cap) {
        this.cap = cap;
    }

    public String getNumeroCivico() {
        return numeroCivico;
    }

    public void setNumeroCivico(String numeroCivico) {
        this.numeroCivico = numeroCivico;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getVia() {
        return via;
    }

    public void setVia(String via) {
        this.via = via;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getNomeMetodoSpedizione() {
        return nomeMetodoSpedizione;
    }

    public void setNomeMetodoSpedizione(String nomeMetodoSpedizione) {
        this.nomeMetodoSpedizione = nomeMetodoSpedizione;
    }

    public Date getDataOrdine() {
        return dataOrdine;
    }

    public void setDataOrdine(Date dataOrdine) {
        this.dataOrdine = dataOrdine;
    }

    public String getCittà() {
        return città;
    }

    public void setCittà(String città) {
        this.città = città;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ordine ordine = (Ordine) o;
        return idOrdine == ordine.idOrdine;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idOrdine);
    }
}
