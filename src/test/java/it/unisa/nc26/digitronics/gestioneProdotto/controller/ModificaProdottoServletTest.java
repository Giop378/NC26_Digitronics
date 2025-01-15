package it.unisa.nc26.digitronics.gestioneProdotto.controller;
import it.unisa.nc26.digitronics.gestioneProdotto.service.GestioneProdottoService;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import it.unisa.nc26.digitronics.model.bean.*;
import it.unisa.nc26.digitronics.utils.MyServletException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Classe di test per la servlet {@link ModificaProdottoServlet}.
 * Verifica il corretto funzionamento dei metodi `doGet` e `doPost` in diversi scenari.
 */
public class ModificaProdottoServletTest {

    private ModificaProdottoServlet modificaProdottoServlet;
    private GestioneProdottoService modificaProdottoService;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private RequestDispatcher requestDispatcher;
    private Utente utente;

    /**
     * Metodo di setup eseguito prima di ogni test.
     * Inizializza gli oggetti necessari e configura i mock per i test.
     */
    @Before
    public void setUp() {
        // Creazione dell'oggetto servlet
        modificaProdottoServlet = new ModificaProdottoServlet();
        modificaProdottoService = mock(GestioneProdottoService.class);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        requestDispatcher = mock(RequestDispatcher.class);
        utente = mock(Utente.class);

        // Iniettare il mock service nella servlet
        modificaProdottoServlet.setGestioneProdottoService(modificaProdottoService);
    }

    /**
     * Test del metodo `doGet` quando un amministratore esegue un'operazione con parametri validi.
     */
    @Test
    public void testDoGet_AdminSuccess() throws Exception {
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
        when(request.getParameter("nomecategoria")).thenReturn("Elettronica");
        when(request.getParameter("vetrina")).thenReturn("true");
        when(request.getRequestDispatcher("WEB-INF/results/confirm-operation.jsp")).thenReturn(requestDispatcher);

        // Chiamata al metodo da testare
        modificaProdottoServlet.doGet(request, response);

        // Verifica che il service sia stato chiamato con il giusto oggetto Prodotto
        verify(modificaProdottoService).modificaProdotto(any(Prodotto.class));

        // Verifica che il dispatcher sia stato chiamato
        verify(requestDispatcher).forward(request, response);
    }
    /**
     * Test del metodo `doGet` quando un utente non amministratore tenta di accedere alla funzionalità.
     * Verifica che venga lanciata una {@link MyServletException}.
     */
    @Test
    public void testDoGet_NonAdmin() throws Exception {
        // Simuliamo un utente che non è un amministratore
        when(session.getAttribute("utente")).thenReturn(utente);
        when(utente.isRuolo()).thenReturn(false);  // L'utente non è un admin

        when(request.getSession()).thenReturn(session);
        when(request.getParameter("id")).thenReturn("1");
        when(request.getParameter("nome")).thenReturn("Telefono");
        when(request.getParameter("descrizione")).thenReturn("Descrizione valida");
        when(request.getParameter("prezzo")).thenReturn("499.99");
        when(request.getParameter("quantita")).thenReturn("10");
        when(request.getParameter("nomecategoria")).thenReturn("Elettronica");

        try {
            // Chiamata al metodo da testare
            modificaProdottoServlet.doGet(request, response);
            fail("MyServletException expected");
        } catch (MyServletException e) {
            // Verifica che l'eccezione sia stata lanciata con il messaggio giusto
            assertEquals("Non è possibile accedere a questa pagina", e.getMessage());
        }

        // Verifica che il service non sia stato chiamato, dato che l'utente non ha il ruolo di admin
        verify(modificaProdottoService, never()).modificaProdotto(any(Prodotto.class));
    }

    /**
     * Test del metodo `doGet` quando il nome del prodotto è nullo.
     * Verifica che venga lanciata una {@link MyServletException}.
     */
    @Test
    public void testDoGet_NomeProdottoNull() throws Exception {
        when(session.getAttribute("utente")).thenReturn(utente);
        when(utente.isRuolo()).thenReturn(true);

        when(request.getSession()).thenReturn(session);
        when(request.getParameter("id")).thenReturn("1");
        when(request.getParameter("nome")).thenReturn(null);  // Nome prodotto null
        when(request.getParameter("descrizione")).thenReturn("Descrizione valida");
        when(request.getParameter("prezzo")).thenReturn("499.99");
        when(request.getParameter("quantita")).thenReturn("10");
        when(request.getParameter("nomecategoria")).thenReturn("Elettronica");

        try {
            modificaProdottoServlet.doGet(request, response);
            fail("MyServletException expected");
        } catch (MyServletException e) {
            assertEquals("Il nome del prodotto deve avere una lunghezza tra 1 e 255 caratteri", e.getMessage());
        }
    }

    /**
     * Test del metodo `doGet` quando il nome del prodotto è troppo lungo.
     * Verifica che venga lanciata una {@link MyServletException}.
     */
    @Test
    public void testDoGet_NomeProdottoTroppoLungo() throws Exception {
        when(session.getAttribute("utente")).thenReturn(utente);
        when(utente.isRuolo()).thenReturn(true);

        when(request.getSession()).thenReturn(session);
        when(request.getParameter("id")).thenReturn("1");
        when(request.getParameter("nome")).thenReturn("A".repeat(256));  // Nome prodotto > 255 caratteri
        when(request.getParameter("descrizione")).thenReturn("Descrizione valida");
        when(request.getParameter("prezzo")).thenReturn("499.99");
        when(request.getParameter("quantita")).thenReturn("10");
        when(request.getParameter("nomecategoria")).thenReturn("Elettronica");

        try {
            modificaProdottoServlet.doGet(request, response);
            fail("MyServletException expected");
        } catch (MyServletException e) {
            assertEquals("Il nome del prodotto deve avere una lunghezza tra 1 e 255 caratteri", e.getMessage());
        }
    }

    /**
     * Test del metodo `doGet` quando la quantità è minore o uguale a zero.
     * Verifica che venga lanciata una {@link MyServletException}.
     */
    @Test
    public void testDoGet_QuantitaMinoreUgualeZero() throws Exception {
        when(session.getAttribute("utente")).thenReturn(utente);
        when(utente.isRuolo()).thenReturn(true);

        when(request.getSession()).thenReturn(session);
        when(request.getParameter("id")).thenReturn("1");
        when(request.getParameter("nome")).thenReturn("Telefono");
        when(request.getParameter("descrizione")).thenReturn("Descrizione valida");
        when(request.getParameter("prezzo")).thenReturn("499.99");
        when(request.getParameter("quantita")).thenReturn("0");  // Quantità <= 0
        when(request.getParameter("nomecategoria")).thenReturn("Elettronica");

        try {
            modificaProdottoServlet.doGet(request, response);
            fail("MyServletException expected");
        } catch (MyServletException e) {
            assertEquals("La quantità deve essere maggiore di zero", e.getMessage());
        }
    }


    /**
     * Test del metodo `doGet` quando la descrizione del prodotto è vuota.
     * Verifica che venga lanciata una {@link MyServletException}.
     */
    @Test
    public void testDoGet_DescrizioneVuota() throws Exception {
        when(session.getAttribute("utente")).thenReturn(utente);
        when(utente.isRuolo()).thenReturn(true);

        when(request.getSession()).thenReturn(session);
        when(request.getParameter("id")).thenReturn("1");
        when(request.getParameter("nome")).thenReturn("Telefono");
        when(request.getParameter("descrizione")).thenReturn(""); // Descrizione vuota
        when(request.getParameter("prezzo")).thenReturn("499.99");
        when(request.getParameter("quantita")).thenReturn("10");
        when(request.getParameter("nomecategoria")).thenReturn("Elettronica");

        try {
            modificaProdottoServlet.doGet(request, response);
            fail("MyServletException expected");
        } catch (MyServletException e) {
            assertEquals("La descrizione del prodotto non può essere vuota", e.getMessage());
        }
    }

    /**
     * Test del metodo `doGet` quando la categoria è vuota.
     * Verifica che venga lanciata una {@link MyServletException}.
     */
    @Test
    public void testDoGet_CategoriaVuota() throws Exception {
        when(session.getAttribute("utente")).thenReturn(utente);
        when(utente.isRuolo()).thenReturn(true);

        when(request.getSession()).thenReturn(session);
        when(request.getParameter("id")).thenReturn("1");
        when(request.getParameter("nome")).thenReturn("Telefono");
        when(request.getParameter("descrizione")).thenReturn("Descrizione valida");
        when(request.getParameter("prezzo")).thenReturn("499.99");
        when(request.getParameter("quantita")).thenReturn("10");
        when(request.getParameter("nomecategoria")).thenReturn(""); // Categoria vuota

        try {
            modificaProdottoServlet.doGet(request, response);
            fail("MyServletException expected");
        } catch (MyServletException e) {
            assertEquals("La categoria deve avere una lunghezza compresa tra 1 e 255 caratteri", e.getMessage());
        }
    }

    /**
     * Test del metodo `doGet` quando la categoria è troppo lunga.
     * Verifica che venga lanciata una {@link MyServletException}.
     */
    @Test
    public void testDoGet_CategoriaTroppoLunga() throws Exception {
        when(session.getAttribute("utente")).thenReturn(utente);
        when(utente.isRuolo()).thenReturn(true);

        when(request.getSession()).thenReturn(session);
        when(request.getParameter("id")).thenReturn("1");
        when(request.getParameter("nome")).thenReturn("Telefono");
        when(request.getParameter("descrizione")).thenReturn("Descrizione valida");
        when(request.getParameter("prezzo")).thenReturn("499.99");
        when(request.getParameter("quantita")).thenReturn("10");
        when(request.getParameter("nomecategoria")).thenReturn("A".repeat(256)); // Categoria > 255 caratteri

        try {
            modificaProdottoServlet.doGet(request, response);
            fail("MyServletException expected");
        } catch (MyServletException e) {
            assertEquals("La categoria deve avere una lunghezza compresa tra 1 e 255 caratteri", e.getMessage());
        }
    }

    /**
     * Test del metodo `doGet` quando l'ID del prodotto non è un numero valido.
     * Verifica che venga lanciata una {@link MyServletException}.
     */
    @Test
    public void testDoGet_NumeroNonValido() throws Exception {
        when(session.getAttribute("utente")).thenReturn(utente);
        when(utente.isRuolo()).thenReturn(true);

        when(request.getSession()).thenReturn(session);
        when(request.getParameter("id")).thenReturn("abc"); // ID non numerico
        when(request.getParameter("nome")).thenReturn("Telefono");
        when(request.getParameter("descrizione")).thenReturn("Descrizione valida");
        when(request.getParameter("prezzo")).thenReturn("499.99");
        when(request.getParameter("quantita")).thenReturn("10");
        when(request.getParameter("nomecategoria")).thenReturn("Elettronica");

        try {
            modificaProdottoServlet.doGet(request, response);
            fail("MyServletException expected");
        } catch (MyServletException e) {
            assertEquals("Uno o più parametri errati nel form", e.getMessage());
        }
    }

    /**
     * Test del metodo `doPost`, verificando che reindirizzi al metodo `doGet`.
     */
    @Test
    public void testDoPost_RedirectToDoGet() throws Exception {
        // Configurazione di base (utente admin e parametri validi)
        when(session.getAttribute("utente")).thenReturn(utente);
        when(utente.isRuolo()).thenReturn(true);

        when(request.getSession()).thenReturn(session);
        when(request.getParameter("id")).thenReturn("1");
        when(request.getParameter("nome")).thenReturn("Telefono");
        when(request.getParameter("descrizione")).thenReturn("Descrizione valida");
        when(request.getParameter("prezzo")).thenReturn("499.99");
        when(request.getParameter("quantita")).thenReturn("10");
        when(request.getParameter("nomecategoria")).thenReturn("Elettronica");
        when(request.getParameter("vetrina")).thenReturn("true");
        when(request.getRequestDispatcher("WEB-INF/results/confirm-operation.jsp")).thenReturn(requestDispatcher);

        // Chiamata al metodo doPost
        modificaProdottoServlet.doPost(request, response);

        // Verifica che doGet sia stato chiamato (implicitamente)
        verify(request).getSession(); // doGet è stato invocato perché interagisce con la sessione

        // Verifica che il dispatcher sia stato chiamato
        verify(requestDispatcher).forward(request, response);
    }

    /**
     * Test del metodo `doGet` quando il prezzo è minore o uguale a zero.
     * Verifica che venga lanciata una {@link MyServletException}.
     */
    @Test
    public void testDoGet_PrezzoMinoreUgualeZero() throws Exception {
        // Mock dei dati
        when(session.getAttribute("utente")).thenReturn(utente);
        when(utente.isRuolo()).thenReturn(true);

        when(request.getSession()).thenReturn(session);
        when(request.getParameter("id")).thenReturn("1");
        when(request.getParameter("nome")).thenReturn("Telefono");
        when(request.getParameter("descrizione")).thenReturn("Descrizione valida");
        when(request.getParameter("prezzo")).thenReturn("0");  // Prezzo <= 0
        when(request.getParameter("quantita")).thenReturn("10");
        when(request.getParameter("nomecategoria")).thenReturn("Elettronica");

        // Test per il caso in cui il prezzo è <= 0
        try {
            modificaProdottoServlet.doGet(request, response);
            fail("MyServletException expected");  // Dovrebbe fallire
        } catch (MyServletException e) {
            System.out.println("Eccezione catturata: " + e.getMessage());
            assertEquals("Il prezzo deve essere maggiore di zero e non deve essere vuoto", e.getMessage());
        }
    }
    /**
     * Test del metodo `doGet` quando il prezzo non è nel formato corretto.
     * Verifica che venga lanciata una {@link MyServletException}.
     */
    @Test
    public void testDoGet_PrezzoFormatoErrato() throws Exception {
        // Mock dei dati
        when(session.getAttribute("utente")).thenReturn(utente);
        when(utente.isRuolo()).thenReturn(true);

        when(request.getSession()).thenReturn(session);
        when(request.getParameter("id")).thenReturn("1");
        when(request.getParameter("nome")).thenReturn("Telefono");
        when(request.getParameter("descrizione")).thenReturn("Descrizione valida");
        when(request.getParameter("prezzo")).thenReturn("10");  // Prezzo con solo un decimale
        when(request.getParameter("quantita")).thenReturn("10");
        when(request.getParameter("nomecategoria")).thenReturn("Elettronica");

        // Test per il caso in cui il prezzo non ha almeno due decimali
        try {
            modificaProdottoServlet.doGet(request, response);
            fail("MyServletException expected");  // Dovrebbe fallire
        } catch (MyServletException e) {
            // Verifica che l'eccezione sia stata lanciata con il messaggio giusto
            assertEquals("Il prezzo deve essere un valore numerico valido con almeno due cifre decimali (es. 10.00)", e.getMessage());
        }
    }
}

