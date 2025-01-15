package it.unisa.nc26.digitronics.gestioneProdotto.service;


import it.unisa.nc26.digitronics.gestioneOrdine.service.GestioneOrdineServiceImpl;
import it.unisa.nc26.digitronics.model.bean.Categoria;
import it.unisa.nc26.digitronics.model.bean.Prodotto;
import it.unisa.nc26.digitronics.utils.ConPool;
import it.unisa.nc26.digitronics.utils.MyServletException;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe di test di integrazione per la classe {@link GestioneProdottoServiceImpl}.
 * Questa classe utilizza un database H2 in memoria configurato tramite script SQL per
 * eseguire test sui metodi di gestione dei prodotti.
 *
 * <p>
 * I test verificano la corretta interazione del servizio con il database e includono:
 * <ul>
 *   <li>Inserimento iniziale dei dati (categorie e prodotti).</li>
 *   <li>Verifica delle operazioni di modifica sui prodotti.</li>
 * </ul>
 * </p>
 *
 * <p><b>Prerequisiti:</b></p>
 * <ul>
 *   <li>Il file <code>database.sql</code> deve essere coerente con le tabelle e i campi
 *       utilizzati nei test.</li>
 * </ul>
 *
 * <p><b>Nota:</b> La classe utilizza il framework JUnit 4.</p>
 */
public class GestioneProdottoServiceIntegrationTest {
    /**
     * Servizio sotto test.
     */
    private static GestioneProdottoServiceImpl service;
    /**
     * Configura l'ambiente globale per i test.
     *
     * <p>Questa configurazione include:</p>
     * <ul>
     *   <li>Impostazione di un database H2 in memoria.</li>
     *   <li>Popolamento delle tabelle del database con dati iniziali (categorie e prodotti).</li>
     *   <li>Inizializzazione del servizio da testare.</li>
     * </ul>
     *
     * @throws SQLException se si verifica un errore durante la configurazione del database.
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
        service = new GestioneProdottoServiceImpl();

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
    }
    /**
     * Testa la modifica di un prodotto nel database.
     *
     * <p><b>Scenario di successo:</b></p>
     * <ul>
     *   <li>Un prodotto esistente viene modificato con nuovi dati validi.</li>
     *   <li>Le modifiche sono salvate correttamente nel database.</li>
     * </ul>
     *
     * <p><b>Asserzioni:</b></p>
     * <ul>
     *   <li>Verifica che i campi del prodotto siano stati aggiornati correttamente nel database.</li>
     *   <li>Verifica che nessun altro campo sia stato alterato inaspettatamente.</li>
     * </ul>
     *
     * @throws MyServletException se si verifica un errore durante la modifica del prodotto.
     * @throws SQLException se si verifica un errore di accesso al database.
     */
    @Test
    public void testModificaProdotto_Successo() throws MyServletException, SQLException {
        // Arrange: Prepara i dati necessari
        Prodotto prodottoModificato = new Prodotto();
        prodottoModificato.setIdProdotto(1); // Prodotto già presente nella tabella
        prodottoModificato.setNomeCategoria("categoria"); // Categoria esistente
        prodottoModificato.setNome("Prodotto1Modificato");
        prodottoModificato.setPrezzo(15.99);
        prodottoModificato.setQuantità(50);
        prodottoModificato.setDescrizione("Descrizione modificata");
        prodottoModificato.setImmagine("Prodotto1 immagine.jpg");


        // Act: Esegui il metodo da testare
        service.modificaProdotto(prodottoModificato);

        // Assert: Verifica che i dati siano stati aggiornati correttamente nel database
        try (Connection connection = ConPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "SELECT * FROM prodotto WHERE IdProdotto = ?")) {
            ps.setInt(1, 1);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    assertEquals("Prodotto1Modificato", rs.getString("nome"));
                    assertEquals(15.99, rs.getDouble("prezzo"), 0.01);
                    assertEquals("Descrizione modificata", rs.getString("descrizione"));
                    assertEquals("Prodotto1 immagine.jpg", rs.getString("immagine"));
                    assertFalse(rs.getBoolean("vetrina"));
                    assertEquals(50, rs.getInt("quantità"));
                    assertEquals("categoria", rs.getString("nomecategoria"));
                } else {
                    fail("Prodotto con ID 1 non trovato nel database");
                }
            }
        }
    }

}