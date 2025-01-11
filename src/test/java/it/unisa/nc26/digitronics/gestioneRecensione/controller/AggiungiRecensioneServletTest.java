package it.unisa.nc26.digitronics.gestioneRecensione.controller;

import it.unisa.nc26.digitronics.gestioneRecensione.service.RecensioneService;
import it.unisa.nc26.digitronics.gestioneRecensione.service.RecensioneServiceImpl;
import it.unisa.nc26.digitronics.model.bean.Recensione;
import it.unisa.nc26.digitronics.model.bean.Utente;
import it.unisa.nc26.digitronics.utils.MyServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

/**
 * Test della servlet {@link AggiungiRecensioneServlet}.
 *
 * Questa classe utilizza Mockito per simulare il comportamento di servlet, richieste HTTP,
 * sessioni e servizi di business. Include vari test unitari per verificare il comportamento della servlet
 * con input validi e non validi.
 */
@RunWith(MockitoJUnitRunner.class)
public class AggiungiRecensioneServletTest {
    /** La servlet sotto test. */
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

    /** Mock del servizio di gestione delle recensioni. */
    @Mock
    private RecensioneService recensioneService;

    /** Mock dell'utente autenticato. */
    @Mock
    private Utente utente;

    /**
     * Configura l'ambiente di test prima di ogni esecuzione.
     */
    @Before
    public void setUp() {
        servlet = new AggiungiRecensioneServlet();
        servlet.setRecensioneService(recensioneService);
    }

    /**
     * Verifica che venga sollevata un'eccezione {@link MyServletException} se l'utente non è registrato.
     */
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
        when(request.getParameter("productId")).thenReturn("1");

        doThrow(new SQLException()).when(recensioneService).saveRecensione(any(Recensione.class));
        servlet.doGet(request, response);
    }

    /**
     * Verifica che la servlet reindirizzi correttamente in caso di parametri validi.
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

        // Verifica che venga effettuato il reindirizzamento corretto
        verify(response).sendRedirect("dettagliProdotto?id=1");
    }
}
