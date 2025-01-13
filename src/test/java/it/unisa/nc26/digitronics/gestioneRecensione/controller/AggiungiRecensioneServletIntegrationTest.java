package it.unisa.nc26.digitronics.gestioneRecensione.controller;

import it.unisa.nc26.digitronics.gestioneOrdine.controller.GestioneOrdineServlet;
import it.unisa.nc26.digitronics.gestioneRecensione.service.RecensioneServiceImpl;
import it.unisa.nc26.digitronics.model.bean.Recensione;
import it.unisa.nc26.digitronics.model.bean.Utente;
import it.unisa.nc26.digitronics.utils.ConPool;
import it.unisa.nc26.digitronics.utils.MyServletException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

/**
 * Classe di test d'integrazione per la servlet AggiungiRecensione.
 * Questa classe testa il comportamento della servlet in diversi scenari,
 * utilizzando un database H2 in memoria per simulare il backend dei dati.
 */
@RunWith(MockitoJUnitRunner.class)
public class AggiungiRecensioneServletIntegrationTest {

    /**
     * Oggetto AggiungiRecensione da testare.
     */
    private AggiungiRecensioneServlet servlet;
    /** Mock della richiesta HTTP. */
    @Mock
    private HttpServletRequest request;
    /** Mock della risposta HTTP. */
    @Mock
    private HttpServletResponse response;
    /** Mock della sessione HTTP. */
    @Mock
    private HttpSession session;
    /** Mock dell'oggetto utente. */
    @Mock
    private Utente utente;

    /**
     * Configurazione iniziale dei test.
     * Inizializza la servlet, i mock e il database H2 in memoria.
     *
     * @throws SQLException in caso di errori nella configurazione del database.
     */
    @Before
    public void setUp() throws SQLException {
        // Inizializza la servlet
        servlet = new AggiungiRecensioneServlet();

        // Configurazione del datasource H2 in memoria
        PoolProperties p = new PoolProperties();
        // Assicuriamoci che il file database.sql sia coerente con quanto riportato qui
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

        // 1. Inserimento categorie
        try (Connection connection = ConPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "INSERT INTO categoria (nome, descrizione, immagine) VALUES (?, ?, ?)")) {
            ps.setString(1, "categoria");
            ps.setString(2, "categoria descrizione");
            ps.setString(3, "categoria immagine");
            ps.executeUpdate();
        }

        // 2. Inserimento prodotti(id 1,2)
        try (Connection connection = ConPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "INSERT INTO prodotto (IdProdotto, nome, prezzo, descrizione, immagine, vetrina, quantità, nomecategoria) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
            ps.setInt(1, 1);
            ps.setString(2, "Prodotto1");
            ps.setDouble(3, 10.00);
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
            ps.setDouble(3, 25.00);
            ps.setString(4, "Prodotto2 descrizione");
            ps.setString(5, "Prodotto2 immagine");
            ps.setBoolean(6, false);
            ps.setInt(7, 100);
            ps.setString(8, "categoria");
            ps.executeUpdate();
        }

        // 3. Inserimento utente
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
                     "INSERT INTO recensione (titolo, descrizione, punteggio, IdProdotto, IdUtente ) VALUES (?, ?, ?, ?, ?)")) {
            ps.setString(1, "Ottimo prodotto!");
            ps.setString(2, "Prodotto di qualità, lo consiglio a tutti!");
            ps.setInt(3, 5);
            ps.setInt(4, 1);
            ps.setInt(5, 1);
            ps.executeUpdate();
        }
    }

    @Test(expected = MyServletException.class)
    public void testUtenteNonRegistrato() throws Exception {
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("utente")).thenReturn(null);

        servlet.doGet(request, response);
    }

    /**
     * Verifica che venga sollevata un'eccezione {@link MyServletException} se il titolo della recensione non è valido.
     */
    @Test(expected = MyServletException.class)
    public void testTitoloNonValido() throws Exception {
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("utente")).thenReturn(utente);

        when(request.getParameter("title")).thenReturn("");
        when(request.getParameter("description")).thenReturn("Descrizione valida");
        when(request.getParameter("score")).thenReturn("3");
        when(request.getParameter("productId")).thenReturn("1");

        servlet.doGet(request, response);
    }

    /**
     * Verifica che venga sollevata un'eccezione {@link MyServletException} se la descrizione della recensione non è valida.
     */
    @Test(expected = MyServletException.class)
    public void testDescrizioneNonValido() throws Exception {
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("utente")).thenReturn(utente);

        when(request.getParameter("title")).thenReturn("Titolo valido");
        when(request.getParameter("description")).thenReturn("");
        when(request.getParameter("score")).thenReturn("3");
        when(request.getParameter("productId")).thenReturn("1");

        servlet.doGet(request, response);
    }

    /**
     * Verifica che venga sollevata un'eccezione {@link MyServletException} se il punteggio è vuoto.
     */
    @Test(expected = MyServletException.class)
    public void testPunteggioVuoto() throws Exception {
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("utente")).thenReturn(utente);

        when(request.getParameter("title")).thenReturn("Titolo valido");
        when(request.getParameter("description")).thenReturn("Descrizione valida");
        when(request.getParameter("score")).thenReturn("");
        when(request.getParameter("productId")).thenReturn("1");

        servlet.doGet(request, response);
    }

    /**
     * Verifica che venga sollevata un'eccezione {@link MyServletException} se il punteggio è fuori dai limiti consentiti.
     */
    @Test(expected = MyServletException.class)
    public void testPunteggioNonValido() throws Exception {
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("utente")).thenReturn(utente);

        when(request.getParameter("title")).thenReturn("Titolo valido");
        when(request.getParameter("description")).thenReturn("Descrizione valida");
        when(request.getParameter("score")).thenReturn("6");
        when(request.getParameter("productId")).thenReturn("1");

        servlet.doGet(request, response);
    }

    /**
     * Verifica che venga sollevata un'eccezione {@link MyServletException} se il punteggio non è un numero.
     */
    @Test(expected = MyServletException.class)
    public void testPunteggioNonNumero() throws Exception {
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("utente")).thenReturn(utente);

        when(request.getParameter("title")).thenReturn("Titolo valido");
        when(request.getParameter("description")).thenReturn("Descrizione valida");
        when(request.getParameter("score")).thenReturn("nonNumero");
        when(request.getParameter("productId")).thenReturn("1");

        servlet.doGet(request, response);
    }

    /**
     * Verifica che venga sollevata un'eccezione {@link MyServletException} se l'ID del prodotto è vuoto.
     */
    @Test(expected = MyServletException.class)
    public void testIdProdottoVuoto() throws Exception {
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("utente")).thenReturn(utente);

        when(request.getParameter("title")).thenReturn("Titolo valido");
        when(request.getParameter("description")).thenReturn("Descrizione valida");
        when(request.getParameter("score")).thenReturn("5");
        when(request.getParameter("productId")).thenReturn("");

        servlet.doGet(request, response);
    }

    /**
     * Verifica che venga sollevata un'eccezione {@link MyServletException} in caso di errore del database.
     */
    @Test(expected = MyServletException.class)
    public void testErroreDatabase() throws Exception {
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("utente")).thenReturn(utente);
        when(utente.getIdUtente()).thenReturn(1);

        when(request.getParameter("title")).thenReturn("Titolo valido");
        when(request.getParameter("description")).thenReturn("Descrizione valida");
        when(request.getParameter("score")).thenReturn("5");
        when(request.getParameter("productId")).thenReturn("10000");

        servlet.doGet(request, response);
    }

    /**
     * Verifica che la servlet reindirizzi correttamente in caso di parametri validi e che
     * la recensione venga salvata correttamente.
     */
    @Test
    public void testParametriValidi() throws Exception {
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("utente")).thenReturn(utente);
        when(utente.getIdUtente()).thenReturn(1);

        when(request.getParameter("title")).thenReturn("Titolo valido");
        when(request.getParameter("description")).thenReturn("Descrizione valida");
        when(request.getParameter("score")).thenReturn("5");
        when(request.getParameter("productId")).thenReturn("1");

        servlet.doGet(request, response);

        verify(response).sendRedirect("dettagliProdotto?id=1");

        RecensioneServiceImpl service = new RecensioneServiceImpl();
        ArrayList<Recensione> retrieved = (ArrayList<Recensione>) service.fetchByProduct(1);
        assertEquals(2, retrieved.size());
        assertEquals(2, retrieved.get(1).getIdRecensione());


    }

}
