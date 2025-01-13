package it.unisa.nc26.digitronics.gestioneRecensione.service;

import it.unisa.nc26.digitronics.gestioneOrdine.service.GestioneOrdineServiceImpl;
import it.unisa.nc26.digitronics.model.bean.Recensione;
import it.unisa.nc26.digitronics.utils.ConPool;
import it.unisa.nc26.digitronics.utils.MyServletException;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.*;

/**
 * Classe di test di integrazione per {@link RecensioneServiceImpl}.
 * Questa classe verifica il corretto funzionamento dei metodi del servizio
 * che gestisce le recensioni, utilizzando un database H2 in memoria.
 *
 * <p>La configurazione iniziale del database e i dati di test vengono
 * impostati utilizzando il metodo {@link #globalSetUp()}.</p>
 */
public class RecensioneServiceImplIntegrationTest {


    /**
     * Istanza del servizio di recensioni da testare.
     */
    private static RecensioneServiceImpl service;

    /**
     * Questo metodo viene eseguito una sola volta prima di tutti i test e
     * inserisce i dati necessari seguendo l’ordine di inserimento specificato.
     */
    @BeforeClass
    public static void globalSetUp() throws SQLException {
        // Configurazione del datasource H2 in memoria
        PoolProperties p = new PoolProperties();
        // Si assicuri che il file database.sql sia coerente con quanto riportato qui
        p.setUrl("jdbc:h2:mem:Digitronics;MODE=MySQL;DATABASE_TO_UPPER=false;INIT=RUNSCRIPT FROM 'classpath:database/database.sql'");
        p.setDriverClassName("org.h2.Driver");
        p.setUsername("sa");
        p.setPassword("");
        p.setMaxActive(100);
        p.setInitialSize(10);
        p.setMinIdle(10);
        p.setRemoveAbandonedTimeout(60);
        p.setRemoveAbandoned(true);
        DataSource h2DataSource = new DataSource();
        h2DataSource.setPoolProperties(p);

        // Sostituisci il datasource in ConPool
        ConPool.setDataSource(h2DataSource);

        // Inizializza il service
        service = new RecensioneServiceImpl();

        // Inserimenti comuni per tutti i test
        // 1. Inserimento categoria
        try (Connection connection = ConPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "INSERT INTO categoria (nome, descrizione, immagine) VALUES (?, ?, ?)")) {
            ps.setString(1, "categoria");
            ps.setString(2, "categoria descrizione");
            ps.setString(3, "categoria immagine");
            ps.executeUpdate();
        }

        // 2. Inserimento prodotti
        try (Connection connection = ConPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "INSERT INTO prodotto (IdProdotto, nome, prezzo, descrizione, immagine, vetrina, quantità, nomecategoria) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
            ps.setInt(1, 1);
            ps.setString(2, "Prodotto1");
            ps.setDouble(3, 10.50);
            ps.setString(4, "Prodotto1 descrizione");
            ps.setString(5, "Prodotto1 immagine");
            ps.setBoolean(6, false);
            ps.setInt(7, 100);
            ps.setString(8, "categoria");
            ps.executeUpdate();
        }
        try (Connection connection = ConPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "INSERT INTO prodotto (IdProdotto, nome, prezzo, descrizione, immagine, vetrina, quantità, nomecategoria) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
            ps.setInt(1, 2);
            ps.setString(2, "Prodotto2");
            ps.setDouble(3, 23.50);
            ps.setString(4, "Prodotto2 descrizione");
            ps.setString(5, "Prodotto2 immagine");
            ps.setBoolean(6, false);
            ps.setInt(7, 100);
            ps.setString(8, "categoria");
            ps.executeUpdate();
        }
        try (Connection connection = ConPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "INSERT INTO prodotto (IdProdotto, nome, prezzo, descrizione, immagine, vetrina, quantità, nomecategoria) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
            ps.setInt(1, 3);
            ps.setString(2, "Prodotto3");
            ps.setDouble(3, 100.50);
            ps.setString(4, "Prodotto3 descrizione");
            ps.setString(5, "Prodotto3 immagine");
            ps.setBoolean(6, false);
            ps.setInt(7, 100);
            ps.setString(8, "categoria");
            ps.executeUpdate();
        }

        // 3. Inserimento utenti
        // Utente con id 1
        try (Connection connection = ConPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "INSERT INTO utente (IdUtente, nome, cognome, email, passwordhash, datadinascita, ruolo) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
            ps.setInt(1, 1);
            ps.setString(2, "Mario");
            ps.setString(3, "Rossi");
            ps.setString(4, "mario.rossi@example.com");
            ps.setString(5, "hashdummy");
            ps.setDate(6, Date.valueOf(LocalDate.of(1990, 1, 1)));
            ps.setBoolean(7, false);
            ps.executeUpdate();
        }

        // 4. Inserimento metodi di spedizione
        try (Connection connection = ConPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "INSERT INTO metodospedizione (nome, descrizione, costo) VALUES (?, ?, ?)")) {
            ps.setString(1, "Standard");
            ps.setString(2, "Spedizione Standard");
            ps.setDouble(3, 5.0);
            ps.executeUpdate();
        }
        try (Connection connection = ConPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "INSERT INTO metodospedizione (nome, descrizione, costo) VALUES (?, ?, ?)")) {
            ps.setString(1, "Espress");
            ps.setString(2, "Spedizione Espress");
            ps.setDouble(3, 10.0);
            ps.executeUpdate();
        }

        // 5. Inserimento di alcuni item nel carrello
        try (Connection connection = ConPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "INSERT INTO itemCarrello (IdProdotto, IdUtente, quantità) VALUES (?, ?, ?)")) {
            ps.setInt(1, 2);
            ps.setInt(2, 1);
            ps.setInt(3, 3);
            ps.executeUpdate();
        }
        try (Connection connection = ConPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "INSERT INTO itemCarrello (IdProdotto, IdUtente, quantità) VALUES (?, ?, ?)")) {
            ps.setInt(1, 1);
            ps.setInt(2, 1);
            ps.setInt(3, 5);
            ps.executeUpdate();
        }
        try (Connection connection = ConPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "INSERT INTO recensione (titolo, descrizione, punteggio, IdProdotto, IdUtente ) VALUES (?, ?, ?, ?, ?)")) {
            ps.setString(1, "Ottimo prodotto!");
            ps.setString(2, "Prodotto di qualità, lo consiglio a tutti!");
            ps.setInt(3, 5);
            ps.setInt(4, 1);
            ps.setInt(5, 1);
            ps.executeUpdate();
        }

        // A questo punto il database è popolato con tutti i dati necessari per i test.
    }


    /**
     * Verifica che il metodo {@link RecensioneServiceImpl#saveRecensione(Recensione)}
     * salvi correttamente una nuova recensione e che le recensioni per un prodotto
     * possano essere recuperate.
     */
    @Test
    public void testSaveRecensione() throws SQLException {
        Recensione recensione = new Recensione();
        recensione.setIdProdotto(1);
        recensione.setIdUtente(1);
        recensione.setTitolo("Titolo Test");
        recensione.setDescrizione("Descrizione Test");
        recensione.setPunteggio(5);

        service.saveRecensione(recensione);
        ArrayList<Recensione> retrieved = (ArrayList<Recensione>) service.fetchByProduct(1);
        assertNotNull(retrieved);
        assertEquals(1, retrieved.get(0).getIdRecensione());
        assertEquals(2, retrieved.get(1).getIdRecensione());
    }

    /**
     * Verifica che il metodo {@link RecensioneServiceImpl#fetchByProduct(int)}
     * restituisca correttamente le recensioni associate a un prodotto specifico.
     */
    @Test
    public void testFetchByProductSucces() throws SQLException {
        ArrayList<Recensione> retrieved = (ArrayList<Recensione>) service.fetchByProduct(1);

        assertNotNull(retrieved);
        assertEquals(1, retrieved.size());
        assertEquals(1, retrieved.get(0).getIdRecensione());
        assertEquals(1, retrieved.get(0).getIdUtente());
        assertEquals(1, retrieved.get(0).getIdProdotto());
    }

    /**
     * Verifica che il metodo {@link RecensioneServiceImpl#fetchByUtente(int)}
     * restituisca correttamente le recensioni associate a un utente specifico.
     */
    @Test
    public void testFetchByUtente() throws SQLException {
        ArrayList<Recensione> retrieved = (ArrayList<Recensione>) service.fetchByUtente(1);

        assertNotNull(retrieved);
        assertEquals(1, retrieved.size());
        assertEquals(1, retrieved.get(0).getIdRecensione());
        assertEquals(1, retrieved.get(0).getIdUtente());
    }

}
