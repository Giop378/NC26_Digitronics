package it.unisa.nc26.digitronics.gestioneProdotto.controller;

import it.unisa.nc26.digitronics.gestioneProdotto.service.GestioneProdottoService;
import it.unisa.nc26.digitronics.gestioneProdotto.service.GestioneProdottoServiceImpl;
import it.unisa.nc26.digitronics.model.bean.Prodotto;
import it.unisa.nc26.digitronics.model.bean.Utente;
import it.unisa.nc26.digitronics.utils.ConPool;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

/**
 * Classe di test di integrazione per la servlet {@link ModificaProdottoServlet}.
 * Questa classe verifica il corretto funzionamento della servlet responsabile
 * della modifica dei prodotti tramite interazione con un database H2 in memoria.
 *
 * <p>
 * I test includono:
 * <ul>
 *   <li>Preparazione dell'ambiente con un database H2 in memoria e script SQL.</li>
 *   <li>Test di successo della modifica di un prodotto con parametri validi.</li>
 * </ul>
 * </p>
 *
 * <p><b>Prerequisiti:</b></p>
 * <ul>
 *   <li>Il file <code>database.sql</code> deve essere coerente con le tabelle e i campi
 *       utilizzati nei test.</li>
 * </ul>
 *
 * <p><b>Nota:</b> La classe utilizza il framework JUnit 4 e librerie per il mocking.</p>
 */
public class ModificaProdottoServletIntegration {

    /**
     * Istanza della servlet sotto test.
     */
    private ModificaProdottoServlet modificaProdottoServlet;
    /**
     * Mock del servizio di gestione dei prodotti.
     */
    private GestioneProdottoService modificaProdottoService;
    /**
     * Mock dell'oggetto {@link HttpServletRequest}.
     */
    private HttpServletRequest request;
    /**
     * Mock dell'oggetto {@link HttpServletResponse}.
     */
    private HttpServletResponse response;
    /**
     * Mock dell'oggetto {@link HttpSession}.
     */
    private HttpSession session;
    /**
     * Mock del dispatcher delle richieste.
     */
    private RequestDispatcher requestDispatcher;
    /**
     * Mock dell'entità utente.
     */
    private Utente utente;
    /**
     * Configura l'ambiente per i test di integrazione.
     *
     * <p>Questa configurazione include:</p>
     * <ul>
     *   <li>Inizializzazione dei mock degli oggetti servlet, request, response e session.</li>
     *   <li>Configurazione di un database H2 in memoria e popolamento delle tabelle con dati iniziali.</li>
     * </ul>
     *
     * @throws SQLException se si verifica un errore durante la configurazione del database.
     */
    @Before
    public  void setUp() throws SQLException {

        modificaProdottoServlet = new ModificaProdottoServlet();

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        requestDispatcher = mock(RequestDispatcher.class);
        utente = mock(Utente.class);

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

        // Inizializza la


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
     * Testa la modifica di un prodotto con parametri validi.
     *
     * <p><b>Scenario di successo:</b></p>
     * <ul>
     *   <li>Un utente admin modifica un prodotto esistente con parametri validi.</li>
     *   <li>La servlet aggiorna correttamente i dati del prodotto nel database.</li>
     * </ul>
     *
     * <p><b>Asserzioni:</b></p>
     * <ul>
     *   <li>Verifica che i campi del prodotto siano stati aggiornati nel database.</li>
     *   <li>Verifica che il dispatcher inoltri correttamente la richiesta alla pagina di conferma.</li>
     * </ul>
     *
     * @throws Exception se si verifica un errore durante il test.
     */
    @Test
    public void modificaProdottoSuccesso() throws Exception {
        // Setup
        when(session.getAttribute("utente")).thenReturn(utente);
        when(utente.isRuolo()).thenReturn(true);  // Simuliamo un utente admin

        // Parametri validi
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("id")).thenReturn("1");
        when(request.getParameter("nome")).thenReturn("Telefono");
        when(request.getParameter("descrizione")).thenReturn("Descrizione valida");
        when(request.getParameter("prezzo")).thenReturn("499.99");
        when(request.getParameter("quantita")).thenReturn("10");
        when(request.getParameter("nomecategoria")).thenReturn("categoria");
        when(request.getParameter("vetrina")).thenReturn("true");
        when(request.getRequestDispatcher("WEB-INF/results/confirm-operation.jsp")).thenReturn(requestDispatcher);

        // Chiamata al metodo da testare
        modificaProdottoServlet.doGet(request, response);

        // Verifica che il dispatcher sia stato chiamato
        verify(requestDispatcher).forward(request, response);

        try (Connection connection = ConPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "SELECT * FROM prodotto WHERE IdProdotto = ?")) {
            ps.setInt(1, 1);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    assertEquals("Telefono", rs.getString("nome"));
                    assertEquals(499.99, rs.getDouble("prezzo"), 0.01);
                    assertEquals("Descrizione valida", rs.getString("descrizione"));
                    assertTrue(rs.getBoolean("vetrina"));
                    assertEquals(10, rs.getInt("quantità"));
                    assertEquals("categoria", rs.getString("nomecategoria"));
                } else {
                    fail("Prodotto con ID 1 non trovato nel database");
                }
            }
        }
    }
}
