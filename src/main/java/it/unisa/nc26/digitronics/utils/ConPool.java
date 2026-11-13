package it.unisa.nc26.digitronics.utils;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.util.TimeZone;

/**
 * Classe per la gestione del pool di connessioni al database.
 *
 * Questa classe utilizza il pool di connessioni fornito da Apache Tomcat
 * per ottimizzare la gestione delle connessioni al database.
 *
 * <p>I parametri di connessione vengono letti da variabili d'ambiente
 * (con valori di default adatti a un'installazione locale):
 * <ul>
 *   <li>{@code DB_URL} &mdash; URL JDBC del database;</li>
 *   <li>{@code DB_USER} &mdash; utente del database;</li>
 *   <li>{@code DB_PASSWORD} &mdash; password del database;</li>
 *   <li>{@code DB_PASSWORD_FILE} &mdash; percorso di un file contenente la password
 *       (es. un Docker secret montato in {@code /run/secrets}); se presente ha
 *       la precedenza su {@code DB_PASSWORD}.</li>
 * </ul>
 */
public class ConPool {

    private static final String DEFAULT_URL =
            "jdbc:mysql://localhost:3306/digitronics?serverTimezone=" + TimeZone.getDefault().getID();
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASSWORD = "root";

    private static DataSource datasource;

    /**
     * Permette di sovrascrivere il datasource, utile per i test.
     */
    public static void setDataSource(DataSource ds) {
        datasource = ds;
    }

    /**
     * Recupera una connessione al database dal pool tramite jdbv.
     *
     * Se il pool non è ancora stato inizializzato, viene configurato e creato.
     *
     * @return un oggetto {@link Connection} dal pool di connessioni
     * @throws RuntimeException se si verifica un errore durante la connessione al database
     */
    public static Connection getConnection() {
        try {
            if (datasource == null) {
                // Configurazione delle proprietà del pool di connessioni
                PoolProperties p = new PoolProperties();
                p.setUrl(getEnv("DB_URL", DEFAULT_URL));
                p.setDriverClassName("com.mysql.cj.jdbc.Driver");
                p.setUsername(getEnv("DB_USER", DEFAULT_USER));
                p.setPassword(readPassword());
                p.setMaxActive(100);
                p.setInitialSize(10);
                p.setMinIdle(10);
                p.setRemoveAbandonedTimeout(60);
                p.setRemoveAbandoned(true);
                datasource = new DataSource();
                datasource.setPoolProperties(p);
            }
            // Recupera una connessione dal pool
            return datasource.getConnection();
        } catch (Exception ex) {
            throw new RuntimeException("Connessione persa col database, riprova più tardi");
        }
    }

    /**
     * Restituisce il valore di una variabile d'ambiente o un valore di default
     * se la variabile non è definita o è vuota.
     */
    private static String getEnv(String name, String defaultValue) {
        String value = System.getenv(name);
        return (value == null || value.isBlank()) ? defaultValue : value;
    }

    /**
     * Legge la password del database da {@code DB_PASSWORD_FILE}, se definita,
     * altrimenti da {@code DB_PASSWORD} o dal valore di default.
     */
    private static String readPassword() {
        String passwordFile = System.getenv("DB_PASSWORD_FILE");
        if (passwordFile != null && !passwordFile.isBlank()) {
            try {
                return Files.readString(Path.of(passwordFile), StandardCharsets.UTF_8).strip();
            } catch (IOException e) {
                throw new IllegalStateException("Impossibile leggere il file della password del database", e);
            }
        }
        return getEnv("DB_PASSWORD", DEFAULT_PASSWORD);
    }
}
