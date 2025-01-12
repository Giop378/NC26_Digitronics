package it.unisa.nc26.digitronics.model.bean;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;

/**
 * Rappresenta un utente del sistema.
 */
public class Utente {

    /** ID univoco dell'utente. */
    private int idUtente;

    /** Nome dell'utente. */
    private String nome;

    /** Cognome dell'utente. */
    private String cognome;

    /** Email dell'utente. */
    private String email;

    /** Hash della password dell'utente (SHA-1). */
    private String passwordHash;

    /** Data di nascita dell'utente. */
    private LocalDate dataDiNascita;

    /** Ruolo dell'utente: true se amministratore, false se utente normale. */
    private boolean ruolo;

    /**
     * Restituisce l'ID dell'utente.
     *
     * @return ID univoco dell'utente.
     */
    public int getIdUtente() {
        return idUtente;
    }

    /**
     * Imposta l'ID dell'utente.
     *
     * @param idUtente Nuovo ID dell'utente.
     */
    public void setIdUtente(int idUtente) {
        this.idUtente = idUtente;
    }

    /**
     * Restituisce il nome dell'utente.
     *
     * @return Nome dell'utente.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Imposta il nome dell'utente.
     *
     * @param nome Nuovo nome dell'utente.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Restituisce il cognome dell'utente.
     *
     * @return Cognome dell'utente.
     */
    public String getCognome() {
        return cognome;
    }

    /**
     * Imposta il cognome dell'utente.
     *
     * @param cognome Nuovo cognome dell'utente.
     */
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    /**
     * Restituisce l'email dell'utente.
     *
     * @return Email dell'utente.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Imposta l'email dell'utente.
     *
     * @param email Nuova email dell'utente.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Restituisce l'hash della password dell'utente.
     *
     * @return Hash della password dell'utente.
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Imposta e calcola l'hash della password dell'utente.
     *
     * <p>La password viene hashata utilizzando l'algoritmo SHA-1.
     * In caso di errore con l'algoritmo, viene lanciata un'eccezione di runtime.</p>
     *
     * @param passwordHash Password in chiaro da hashare.
     */
    public void setPasswordHash(String passwordHash) {
        try {
            // Crea un'istanza di MessageDigest per SHA-1
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.reset();
            digest.update(passwordHash.getBytes(StandardCharsets.UTF_8));

            // Converte l'hash in formato esadecimale e lo assegna
            this.passwordHash = String.format("%040x", new BigInteger(1, digest.digest()));
        } catch (NoSuchAlgorithmException e) {
            // Gestione dell'eccezione in caso l'algoritmo non sia supportato
            throw new RuntimeException(e);
        }
    }

    /**
     * Restituisce la data di nascita dell'utente.
     *
     * @return Data di nascita dell'utente.
     */
    public LocalDate getDataDiNascita() {
        return dataDiNascita;
    }

    /**
     * Imposta la data di nascita dell'utente.
     *
     * @param dataDiNascita Nuova data di nascita dell'utente.
     */
    public void setDataDiNascita(LocalDate dataDiNascita) {
        this.dataDiNascita = dataDiNascita;
    }

    /**
     * Restituisce il ruolo dell'utente.
     *
     * @return {@code true} se l'utente è un amministratore, {@code false} altrimenti.
     */
    public boolean isRuolo() {
        return ruolo;
    }

    /**
     * Imposta il ruolo dell'utente.
     *
     * @param ruolo {@code true} per amministratore, {@code false} per utente normale.
     */
    public void setRuolo(boolean ruolo) {
        this.ruolo = ruolo;
    }
}
