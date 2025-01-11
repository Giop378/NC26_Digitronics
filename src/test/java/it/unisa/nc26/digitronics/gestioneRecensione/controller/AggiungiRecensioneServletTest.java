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

@RunWith(MockitoJUnitRunner.class)
public class AggiungiRecensioneServletTest {
    private AggiungiRecensioneServlet servlet;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession session;
    @Mock
    private RecensioneService recensioneService;
    @Mock
    private Utente utente;
    @Before
    public void setUp() {
        servlet = new AggiungiRecensioneServlet();
        servlet.setRecensioneService(recensioneService);
    }

    @Test(expected = MyServletException.class)
    public void testUtenteNonRegistrato() throws Exception {
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("utente")).thenReturn(null);

        servlet.doGet(request, response);
    }

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
    }

}