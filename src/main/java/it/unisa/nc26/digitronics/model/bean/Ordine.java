package it.unisa.nc26.digitronics.model.bean;

import java.time.LocalDate;
import java.sql.Date;

public class Ordine {
    private int idOrdine;
    private double totale;
    private int idUtente;
    private int cap;
    private int numeroCivico;
    private String nome;
    private String cognome;
    private String via;
    private String telefono;
    private String nomeMetodoSpedizione ;
    private Date dataOrdine;

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

    public int getCap() {
        return cap;
    }

    public void setCap(int cap) {
        this.cap = cap;
    }

    public int getNumeroCivico() {
        return numeroCivico;
    }

    public void setNumeroCivico(int numeroCivico) {
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
}