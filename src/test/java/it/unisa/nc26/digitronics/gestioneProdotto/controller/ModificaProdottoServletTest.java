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
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Classe di test per la servlet ModificaProdottoServlet.
 * Questa classe contiene vari test per verificare il funzionamento dei metodi doGet() e doPost()
 * della ModificaProdottoServlet. I test utilizzano Mockito per simulare le dipendenze e i vari scenari
 * (utente amministratore, utente non amministratore, parametri non validi, ecc.) per garantire che la servlet
 * si comporti come previsto in diverse situazioni.
 *
 * I test includono la validazione di parametri come nome del prodotto, descrizione, quantità, categoria e prezzo.
 * Verificano anche il comportamento quando un utente amministratore e un utente non amministratore tentano
 * di accedere alla pagina, e quando vengono sollevate diverse eccezioni a causa di input errati.
 *
 * @RunWith(MockitoJUnitRunner.class) specifica che il test runner è il MockitoJUnitRunner,
 * che gestisce la creazione e l'iniezione dei mock.
 */
@RunWith(MockitoJUnitRunner.class)
public class ModificaProdottoServletTest {

    /**
     * Istanza mock della ModificaProdottoServlet che viene testata.
     */
    @InjectMocks
    private ModificaProdottoServlet modificaProdottoServlet;

    /**
     * Istanza mock del servizio GestioneProdottoService utilizzato dalla servlet.
     */
    @Mock
    private GestioneProdottoService modificaProdottoService;

    /**
     * Istanza mock di HttpServletRequest utilizzata per simulare le richieste HTTP.
     */
    @Mock
    private HttpServletRequest request;

    /**
     * Istanza mock di HttpServletResponse utilizzata per simulare le risposte HTTP.
     */
    @Mock
    private HttpServletResponse response;

    /**
     * Istanza mock di HttpSession utilizzata per simulare la gestione delle sessioni.
     */
    @Mock
    private HttpSession session;

    /**
     * Istanza mock di RequestDispatcher utilizzata per inoltrare le richieste.
     */
    @Mock
    private RequestDispatcher requestDispatcher;

    /**
     * Istanza mock di Utente utilizzata per simulare l'utente che interagisce con la servlet.
     */
    @Mock
    private Utente utente;

    /**
     * Metodo di setup per inizializzare gli oggetti mock prima di ogni test.
     * In questo metodo viene iniettato il servizio mock nella servlet.
     */
    @Before
    public void setUp() {
        // Iniettare il mock service nella servlet
        modificaProdottoServlet.setGestioneProdottoService(modificaProdottoService);
    }

    /**
     * Test del metodo doGet() quando l'utente è un amministratore e i parametri di input sono validi.
     * Verifica che il servizio venga chiamato con il prodotto corretto e che la richiesta venga inoltrata
     * alla pagina di conferma.
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
     * Test del metodo doGet() quando l'utente non è un amministratore.
     * Verifica che venga sollevata un'eccezione MyServletException con il messaggio appropriato.
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
     * Test del metodo doGet() quando il nome del prodotto è nullo.
     * Verifica che venga sollevata un'eccezione MyServletException con il messaggio appropriato.
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
     * Test del metodo doGet() quando il nome del prodotto è troppo lungo.
     * Verifica che venga sollevata un'eccezione MyServletException con il messaggio appropriato.
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
     * Test del metodo doGet() quando la quantità del prodotto è minore o uguale a zero.
     * Verifica che venga sollevata un'eccezione MyServletException con il messaggio appropriato.
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
     * Test del metodo doGet() quando la descrizione del prodotto è vuota.
     * Verifica che venga sollevata un'eccezione MyServletException con il messaggio appropriato.
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
     * Test del metodo doGet() quando la categoria del prodotto è vuota.
     * Verifica che venga sollevata un'eccezione MyServletException con il messaggio appropriato.
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
     * Test del metodo doGet() quando la categoria del prodotto è troppo lunga.
     * Verifica che venga sollevata un'eccezione MyServletException con il messaggio appropriato.
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
     * Test del metodo doGet() quando l'ID del prodotto non è un numero valido.
     * Verifica che venga sollevata un'eccezione MyServletException con il messaggio appropriato.
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
     * Test del metodo doPost() per verificare il corretto reindirizzamento alla pagina di conferma.
     * Verifica che, dopo l'invio del modulo, la servlet invii la richiesta alla pagina di conferma.
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
     * Test del metodo doGet() quando il prezzo del prodotto è minore o uguale a zero.
     * Verifica che venga sollevata un'eccezione MyServletException con il messaggio appropriato.
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
            assertEquals("Il prezzo deve essere maggiore di zero", e.getMessage());
        }
    }

    /**
     * Test del metodo doGet() quando il prezzo del prodotto ha un formato errato (ad esempio, con un solo decimale).
     * Verifica che venga sollevata un'eccezione MyServletException con il messaggio appropriato.
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



