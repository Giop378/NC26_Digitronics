package it.unisa.nc26.digitronics.utils;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.TimeZone;

/**
 * Classe per la gestione del pool di connessioni al database.
 *
 * Questa classe utilizza il pool di connessioni fornito da Apache Tomcat
 * per ottimizzare la gestione delle connessioni al database.
 */
public class ConPool {

    private static DataSource datasource;

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
                p.setUrl("jdbc:mysql://localhost:3306/digitronics?serverTimezone=" + TimeZone.getDefault().getID());
                p.setDriverClassName("com.mysql.cj.jdbc.Driver");
                p.setUsername("root");
                p.setPassword("root");
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
}
