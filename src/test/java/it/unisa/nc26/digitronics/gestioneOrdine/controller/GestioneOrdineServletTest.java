package it.unisa.nc26.digitronics.gestioneOrdine.controller;

import it.unisa.nc26.digitronics.gestioneOrdine.service.GestioneOrdineService;
import it.unisa.nc26.digitronics.model.bean.ItemCarrello;
import it.unisa.nc26.digitronics.model.bean.MetodoSpedizione;
import it.unisa.nc26.digitronics.model.bean.Ordine;
import it.unisa.nc26.digitronics.model.bean.Prodotto;
import it.unisa.nc26.digitronics.model.bean.ItemOrdine;
import it.unisa.nc26.digitronics.model.bean.Utente;
import it.unisa.nc26.digitronics.utils.MyServletException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Classe di test per la {@code GestioneOrdineServlet}.
 * Questa classe verifica il corretto funzionamento delle funzionalità offerte dalla servlet.
 */
public class GestioneOrdineServletTest {

    private GestioneOrdineServlet servlet;

    @Mock
    private GestioneOrdineService serviceMock;

    @Mock
    private HttpServletRequest requestMock;

    @Mock
    private HttpServletResponse responseMock;

    @Mock
    private HttpSession sessionMock;

    @Mock
    private RequestDispatcher requestDispatcherMock;

    /**
     * Inizializza i mock e la servlet da testare.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        servlet = new GestioneOrdineServlet();

        servlet.setGestioneOrdineService(serviceMock);

        when(requestMock.getSession()).thenReturn(sessionMock);
    }

    /**
     * Caso in cui l'utente non è loggato (utente == null) e quindi viene rediretto alla pagina di login.
     *
     * @throws ServletException in caso di errore nella servlet
     * @throws IOException      in caso di errore di input/output
     */
    @Test
    public void redirigeSuLoginPerUtenteNonLoggato() throws ServletException, IOException {
        when(sessionMock.getAttribute("utente")).thenReturn(null);
        // Quando viene richiesto il dispatcher deve restituire quello simulato
        when(requestMock.getRequestDispatcher("/WEB-INF/results/login.jsp")).thenReturn(requestDispatcherMock);

        servlet.doGet(requestMock, responseMock);

        verify(requestDispatcherMock).forward(requestMock, responseMock);
    }

    /**
     * Caso in cui l'utente è admin (ruolo==true) e quindi viene lanciata eccezione MyServletException.
     *
     * @throws ServletException in caso di errore nella servlet
     * @throws IOException      in caso di errore di input/output
     */
    @Test
    public void lanciaEccezionePerUtenteAdmin() throws ServletException, IOException {
        Utente admin = new Utente();
        admin.setIdUtente(1);
        admin.setRuolo(true);
        when(sessionMock.getAttribute("utente")).thenReturn(admin);

        // Anche se il carrello non viene controllato in questo caso, lo setta ad un valore non nullo
        when(sessionMock.getAttribute("carrello")).thenReturn(new ArrayList<ItemCarrello>());

        try {
            servlet.doGet(requestMock, responseMock);
            fail("MyServletException attesa");
        } catch (MyServletException ex) {
            assertEquals("Non puoi accedere a questa funzionalità", ex.getMessage());
        }
    }

    /**
     * Caso in cui il carrello è nullo o vuoto: redirezione alla pagina del carrello.
     *
     * @throws ServletException in caso di errore nella servlet
     * @throws IOException      in caso di errore di input/output
     */
    @Test
    public void redirigeSuCarrelloPerCarrelloVuoto() throws ServletException, IOException {
        Utente utente = new Utente();
        utente.setRuolo(false);
        when(sessionMock.getAttribute("utente")).thenReturn(utente);
        // Prova con carrello vuoto
        when(sessionMock.getAttribute("carrello")).thenReturn(new ArrayList<ItemCarrello>());
        when(requestMock.getRequestDispatcher("/WEB-INF/results/cart.jsp")).thenReturn(requestDispatcherMock);

        servlet.doGet(requestMock, responseMock);

        verify(requestDispatcherMock).forward(requestMock, responseMock);
    }

    /**
     * Test per verificare che il metodo doPost delega correttamente a doGet.
     * In questo test si simula una chiamata doPost con utente non loggato, verificando che venga
     * eseguito il forward alla pagina di login.
     *
     * @throws ServletException in caso di errore nella servlet
     * @throws IOException      in caso di errore di input/output
     */
    @Test
    public void doPostDelegaACorrettoDoGet() throws ServletException, IOException {
        // Imposta l'utente non loggato per simulare il forward alla login.jsp
        when(sessionMock.getAttribute("utente")).thenReturn(null);
        when(requestMock.getRequestDispatcher("/WEB-INF/results/login.jsp")).thenReturn(requestDispatcherMock);

        // Chiamata doPost
        servlet.doPost(requestMock, responseMock);

        // Si verifica che doGet sia stato eseguito internamente (il forward alla login.jsp)
        verify(requestDispatcherMock).forward(requestMock, responseMock);
    }

    /**
     * Test per verificare che venga lanciata l'eccezione se viene richiesta una funzionalità non esistente.
     * Si imposta un servletPath non gestito e si attende MyServletException.
     *
     * @throws ServletException in caso di errore nella servlet
     * @throws IOException      in caso di errore di input/output
     */
    @Test
    public void funzionalitaNonEsistente() throws ServletException, IOException {
        Utente utente = new Utente();
        utente.setRuolo(false);
        when(sessionMock.getAttribute("utente")).thenReturn(utente);

        // Imposta un carrello non vuoto per superare il controllo del carrello
        ArrayList<ItemCarrello> carrello = new ArrayList<>();
        carrello.add(new ItemCarrello());
        when(sessionMock.getAttribute("carrello")).thenReturn(carrello);

        // Imposta un servletPath non esistente
        when(requestMock.getServletPath()).thenReturn("/funzionalita-non-esistente");

        try {
            servlet.doGet(requestMock, responseMock);
            fail("MyServletException attesa per funzionalità non esistente");
        } catch (MyServletException ex) {
            assertEquals("Funzionalità non esistente", ex.getMessage());
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////
    // CASO INIZIO CHECKOUT
    /////////////////////////////////////////////////////////////////////////////////////

    /**
     * Caso "/inizio-checkout": con carrello valido e quantità disponibili.
     * Viene calcolato il prezzo totale, vengono impostati gli attributi e viene fatto forward alla pagina di checkout.
     *
     * @throws ServletException in caso di errore nella servlet
     * @throws IOException      in caso di errore di input/output
     * @throws MyServletException se si verificano errori nelle logiche di business della servlet
     */
    @Test
    public void checkoutConCarrelloValido() throws ServletException, IOException, MyServletException {
        Utente utente = new Utente();
        utente.setIdUtente(10);
        utente.setRuolo(false);
        when(sessionMock.getAttribute("utente")).thenReturn(utente);

        // Costruisce un carrello con un item
        ItemCarrello item = new ItemCarrello();
        item.setIdProdotto(100);
        item.setQuantità(2);
        List<ItemCarrello> carrello = new ArrayList<>();
        carrello.add(item);
        when(sessionMock.getAttribute("carrello")).thenReturn(carrello);

        // Simula il comportamento del service per il prodotto
        Prodotto prodotto = new Prodotto();
        prodotto.setQuantità(5);
        prodotto.setPrezzo(10.0);
        when(serviceMock.fetchByIdProdotto(100)).thenReturn(prodotto);

        // Simula il recupero dei metodi di spedizione
        List<MetodoSpedizione> metodi = Arrays.asList(new MetodoSpedizione());
        when(serviceMock.fetchAllMetodiSpedizione()).thenReturn(metodi);

        // Imposta il path per la servlet
        when(requestMock.getServletPath()).thenReturn("/inizio-checkout");
        when(requestMock.getRequestDispatcher("/WEB-INF/results/checkout.jsp")).thenReturn(requestDispatcherMock);

        servlet.doGet(requestMock, responseMock);

        // Si verifica che l'attributo prezzoTotale sia stato impostato correttamente: 2 * 10.0 = 20.0
        verify(requestMock).setAttribute("prezzoTotale", 20.0);
        // Si verifica che i metodi di spedizione siano stati impostati
        verify(requestMock).setAttribute("metodiSpedizione", metodi);
        // Si verifica il forward alla pagina di checkout
        verify(requestDispatcherMock).forward(requestMock, responseMock);
    }

    /**
     * Caso "/inizio-checkout" in cui, durante il checkout, la quantità richiesta di un prodotto nel carrello
     * supera la quantità disponibile in magazzino. Deve essere lanciata MyServletException e la quantità del prodotto
     * nel carrello deve essere impostata al massimo.
     *
     * @throws ServletException in caso di errore nella servlet
     * @throws IOException      in caso di errore di input/output
     */
    @Test
    public void checkoutQuantitaNonDisponibile() throws ServletException, IOException {
        Utente utente = new Utente();
        utente.setIdUtente(50);
        utente.setRuolo(false);
        when(sessionMock.getAttribute("utente")).thenReturn(utente);

        // Carrello con un item che richiede più quantità di quelle disponibili
        ItemCarrello item = new ItemCarrello();
        item.setIdProdotto(500);
        item.setQuantità(10); // L'utente richiede 10 unità
        List<ItemCarrello> carrello = new ArrayList<>();
        carrello.add(item);
        when(sessionMock.getAttribute("carrello")).thenReturn(carrello);

        // Simula che il prodotto abbia una quantità insufficiente in magazzino
        Prodotto prodotto = new Prodotto();
        prodotto.setQuantità(5); // Solo 5 unità disponibili
        prodotto.setPrezzo(15.0);
        when(serviceMock.fetchByIdProdotto(500)).thenReturn(prodotto);

        // Imposta il path per "inizio-checkout"
        when(requestMock.getServletPath()).thenReturn("/inizio-checkout");

        try {
            servlet.doGet(requestMock, responseMock);
            fail("MyServletException attesa per quantità non disponibile");
        } catch (MyServletException ex) {
            // Si verifica che il messaggio dell'eccezione sia quello corretto
            assertEquals("Quantità selezionata del prodotto non presente in magazzino", ex.getMessage());
        }

        // Si verifica che il prodotto nel carrello sia stato aggiornato con la quantità massima disponibile
        assertEquals(5, item.getQuantità()); // La quantità viene impostata a quella disponibile
    }

    /////////////////////////////////////////////////////////////////////////////////////
    // CASO PROCEDI AL PAGAMENTO
    /////////////////////////////////////////////////////////////////////////////////////

    /**
     * Caso procedi al pagamento: parametri mancanti.
     * In questo caso se uno dei parametri obbligatori manca viene lanciata MyServletException.
     *
     * @throws ServletException in caso di errore nella servlet
     * @throws IOException      in caso di errore di input/output
     */
    @Test
    public void pagamentoConParametriMancanti() throws ServletException, IOException {
        Utente utente = new Utente();
        utente.setIdUtente(20);
        utente.setRuolo(false);
        when(sessionMock.getAttribute("utente")).thenReturn(utente);

        // Carrello con almeno un item per non cadere nel controllo iniziale sul carrello
        ItemCarrello item = new ItemCarrello();
        item.setIdProdotto(200);
        item.setQuantità(1);
        List<ItemCarrello> carrello = new ArrayList<>();
        carrello.add(item);
        when(sessionMock.getAttribute("carrello")).thenReturn(carrello);

        // Imposta il path per procedi al pagamento
        when(requestMock.getServletPath()).thenReturn("/procedi-al-pagamento");

        // Non imposta i parametri obbligatori, ad esempio "nome" risulta null
        when(requestMock.getParameter("nome")).thenReturn(null);
        // gli altri parametri possono essere simulati come non nulli
        when(requestMock.getParameter("cognome")).thenReturn("Rossi");
        when(requestMock.getParameter("via")).thenReturn("Via Roma");
        when(requestMock.getParameter("numerocivico")).thenReturn("10");
        when(requestMock.getParameter("cap")).thenReturn("12345");
        when(requestMock.getParameter("città")).thenReturn("Milano");
        when(requestMock.getParameter("telefono")).thenReturn("+391234567890");
        when(requestMock.getParameter("numeroCarta")).thenReturn("1234567812345678");
        when(requestMock.getParameter("nomeIntestatario")).thenReturn("Mario Rossi");
        when(requestMock.getParameter("scadenzaCarta")).thenReturn("12/30");
        when(requestMock.getParameter("cvv")).thenReturn("123");
        when(requestMock.getParameter("metodoSpedizione")).thenReturn("Standard");
        // Simula il recupero del metodo di spedizione
        MetodoSpedizione metodo = new MetodoSpedizione();
        metodo.setNome("Standard");
        metodo.setCosto(5.0);
        when(serviceMock.fetchMetodoSpedizioneByNome("Standard")).thenReturn(metodo);

        try {
            servlet.doGet(requestMock, responseMock);
            fail("MyServletException attesa per parametri mancanti");
        } catch (MyServletException ex) {
            assertEquals("Tutti i campi sono obbligatori", ex.getMessage());
        }
    }

    /**
     * Caso procedi al pagamento: indirizzo non verificato e quantità corretta.
     * Viene simulato che l'indirizzo inserito non è verificato.
     * In questo caso viene fatto forward alla pagina di checkout e viene impostato l'attributo "erroreIndirizzo".
     *
     * @throws ServletException in caso di errore nella servlet
     * @throws IOException      in caso di errore di input/output
     * @throws MyServletException se si verificano errori nelle logiche di business della servlet
     */
    @Test
    public void riprovaPerIndirizzoNonVerificato() throws ServletException, IOException, MyServletException {
        Utente utente = new Utente();
        utente.setIdUtente(30);
        utente.setRuolo(false);
        when(sessionMock.getAttribute("utente")).thenReturn(utente);

        // Carrello con un item
        ItemCarrello item = new ItemCarrello();
        item.setIdProdotto(300);
        item.setQuantità(1);
        List<ItemCarrello> carrello = new ArrayList<>();
        carrello.add(item);
        when(sessionMock.getAttribute("carrello")).thenReturn(carrello);

        // Imposta il path per procedi al pagamento
        when(requestMock.getServletPath()).thenReturn("/procedi-al-pagamento");

        // Imposta tutti i parametri validi
        when(requestMock.getParameter("nome")).thenReturn("Luigi");
        when(requestMock.getParameter("cognome")).thenReturn("Bianchi");
        when(requestMock.getParameter("via")).thenReturn("Via Verdi");
        when(requestMock.getParameter("numerocivico")).thenReturn("15");
        when(requestMock.getParameter("cap")).thenReturn("54321");
        when(requestMock.getParameter("città")).thenReturn("Torino");
        when(requestMock.getParameter("telefono")).thenReturn("+391234567891");
        when(requestMock.getParameter("numeroCarta")).thenReturn("1234567812345678");
        when(requestMock.getParameter("nomeIntestatario")).thenReturn("Luigi Bianchi");
        when(requestMock.getParameter("scadenzaCarta")).thenReturn("11/30");
        when(requestMock.getParameter("cvv")).thenReturn("321");
        when(requestMock.getParameter("metodoSpedizione")).thenReturn("Express");

        // Simula il recupero del metodo di spedizione
        MetodoSpedizione metodo = new MetodoSpedizione();
        metodo.setNome("Express");
        metodo.setCosto(10.0);
        when(serviceMock.fetchMetodoSpedizioneByNome("Express")).thenReturn(metodo);
        // Simula che l'indirizzo non sia verificato
        when(serviceMock.verificaIndirizzo("Via Verdi", "54321", "Torino")).thenReturn(false);

        // Simula il recupero del prodotto per il controllo quantità
        Prodotto prodotto = new Prodotto();
        prodotto.setQuantità(2);
        prodotto.setPrezzo(20.0);
        when(serviceMock.fetchByIdProdotto(300)).thenReturn(prodotto);

        // Simula il recupero dei metodi di spedizione per ripopolare la pagina di checkout
        List<MetodoSpedizione> metodi = Arrays.asList(metodo);
        when(serviceMock.fetchAllMetodiSpedizione()).thenReturn(metodi);

        when(requestMock.getRequestDispatcher("/WEB-INF/results/checkout.jsp")).thenReturn(requestDispatcherMock);

        servlet.doGet(requestMock, responseMock);

        // In questo scenario, si riconta il prezzo totale che è dato dalla somma dei prezzi dei prodotti considerate le quantità
        // NB: non si considera la spedizione in questo caso
        double prezzoTotaleAtteso = 20.0;
        verify(requestMock).setAttribute("prezzoTotale", prezzoTotaleAtteso);
        verify(requestMock).setAttribute(eq("erroreIndirizzo"), anyString());
        // Si verifica che alcuni parametri vengano reimpostati
        verify(requestMock).setAttribute("nome", "Luigi");
        verify(requestMock).setAttribute("cognome", "Bianchi");
        verify(requestMock).setAttribute("telefono", "+391234567891");
        verify(requestMock).setAttribute("metodiSpedizione", metodi);
        verify(requestDispatcherMock).forward(requestMock, responseMock);
    }

    /**
     * Caso procedi al pagamento: indirizzo non verificato e quantità eccedente nel carrello.
     * Viene simulato che l'indirizzo inserito non sia verificato e che la quantità di uno
     * dei prodotti nel carrello superi quella disponibile in magazzino.
     *
     * @throws ServletException in caso di errore nella servlet
     * @throws IOException      in caso di errore di input/output
     */
    @Test
    public void indirizzoNonVerificatoEQuantitàEccedente() throws ServletException, IOException {
        Utente utente = new Utente();
        utente.setIdUtente(50);
        utente.setRuolo(false);
        when(sessionMock.getAttribute("utente")).thenReturn(utente);

        // Carrello con un item
        ItemCarrello item = new ItemCarrello();
        item.setIdProdotto(500);
        item.setQuantità(5); // L'utente ha aggiunto 5 unità al carrello
        List<ItemCarrello> carrello = new ArrayList<>();
        carrello.add(item);
        when(sessionMock.getAttribute("carrello")).thenReturn(carrello);

        // Imposta il path per procedi al pagamento
        when(requestMock.getServletPath()).thenReturn("/procedi-al-pagamento");

        // Parametri validi per spedizione e pagamento
        when(requestMock.getParameter("nome")).thenReturn("Mario");
        when(requestMock.getParameter("cognome")).thenReturn("Rossi");
        when(requestMock.getParameter("via")).thenReturn("Via Prova");
        when(requestMock.getParameter("numerocivico")).thenReturn("22");
        when(requestMock.getParameter("cap")).thenReturn("54321");
        when(requestMock.getParameter("città")).thenReturn("Napoli");
        when(requestMock.getParameter("telefono")).thenReturn("+391234567890");
        when(requestMock.getParameter("numeroCarta")).thenReturn("1234567812345678");
        when(requestMock.getParameter("nomeIntestatario")).thenReturn("Mario Rossi");
        when(requestMock.getParameter("scadenzaCarta")).thenReturn("12/30");
        when(requestMock.getParameter("cvv")).thenReturn("123");
        when(requestMock.getParameter("metodoSpedizione")).thenReturn("Standard");

        // Simula il metodo di spedizione
        MetodoSpedizione metodo = new MetodoSpedizione();
        metodo.setNome("Standard");
        metodo.setCosto(5.0);
        when(serviceMock.fetchMetodoSpedizioneByNome("Standard")).thenReturn(metodo);

        // Simula che l'indirizzo non sia verificato
        when(serviceMock.verificaIndirizzo("Via Prova", "54321", "Napoli")).thenReturn(false);

        // Simula il prodotto con quantità insufficiente nel magazzino
        Prodotto prodotto = new Prodotto();
        prodotto.setQuantità(3); // Solo 3 unità disponibili in magazzino
        prodotto.setPrezzo(10.0);
        when(serviceMock.fetchByIdProdotto(500)).thenReturn(prodotto);

        // Simula il recupero dei metodi di spedizione
        List<MetodoSpedizione> metodi = Arrays.asList(metodo);
        when(serviceMock.fetchAllMetodiSpedizione()).thenReturn(metodi);

        try {
            servlet.doGet(requestMock, responseMock);
            fail("MyServletException attesa per quantità eccedente");
        } catch (MyServletException ex) {
            assertEquals("Quantità selezionata del prodotto non presente in magazzino", ex.getMessage());
        }

        // Si verifica che il servizio fetchByIdProdotto sia stato chiamato
        verify(serviceMock).fetchByIdProdotto(500);

        // Si verifica che l'indirizzo sia stato verificato
        verify(serviceMock).verificaIndirizzo("Via Prova", "54321", "Napoli");
    }

    /**
     * Caso procedi al pagamento: indirizzo verificato ma quantità nel carrello eccedente
     * a quella del magazzino.
     *
     * @throws ServletException in caso di errore nella servlet
     * @throws IOException      in caso di errore di input/output
     * @throws MyServletException se si verificano errori nelle logiche di business della servlet
     */
    @Test
    public void nonConfermaOrdinePerQuantitaEccedente() throws ServletException, IOException, MyServletException {
        Utente utente = new Utente();
        utente.setIdUtente(50);
        utente.setRuolo(false);
        when(sessionMock.getAttribute("utente")).thenReturn(utente);

        // Carrello con due item, uno con quantità eccedente
        ItemCarrello item1 = new ItemCarrello();
        item1.setIdProdotto(500);
        item1.setQuantità(5); // Quantità valida

        ItemCarrello item2 = new ItemCarrello();
        item2.setIdProdotto(501);
        item2.setQuantità(10); // Quantità eccedente

        List<ItemCarrello> carrello = new ArrayList<>();
        carrello.add(item1);
        carrello.add(item2);
        when(sessionMock.getAttribute("carrello")).thenReturn(carrello);

        // Imposta il path per procedi al pagamento
        when(requestMock.getServletPath()).thenReturn("/procedi-al-pagamento");

        // Parametri validi per spedizione e pagamento
        when(requestMock.getParameter("nome")).thenReturn("Carlo");
        when(requestMock.getParameter("cognome")).thenReturn("Bianchi");
        when(requestMock.getParameter("via")).thenReturn("Via Libertà");
        when(requestMock.getParameter("numerocivico")).thenReturn("22");
        when(requestMock.getParameter("cap")).thenReturn("33333");
        when(requestMock.getParameter("città")).thenReturn("Napoli");
        when(requestMock.getParameter("telefono")).thenReturn("+391234567893");
        when(requestMock.getParameter("numeroCarta")).thenReturn("1234567812345678");
        when(requestMock.getParameter("nomeIntestatario")).thenReturn("Carlo Bianchi");
        when(requestMock.getParameter("scadenzaCarta")).thenReturn("10/30");
        when(requestMock.getParameter("cvv")).thenReturn("789");
        when(requestMock.getParameter("metodoSpedizione")).thenReturn("Standard");

        // Simula il metodo di spedizione
        MetodoSpedizione metodo = new MetodoSpedizione();
        metodo.setNome("Standard");
        metodo.setCosto(5.0);
        when(serviceMock.fetchMetodoSpedizioneByNome("Standard")).thenReturn(metodo);

        // Indirizzo verificato
        when(serviceMock.verificaIndirizzo("Via Libertà", "33333", "Napoli")).thenReturn(true);

        // Simula i prodotti
        Prodotto prodotto1 = new Prodotto();
        prodotto1.setQuantità(10); // Quantità sufficiente
        prodotto1.setPrezzo(50.0);

        Prodotto prodotto2 = new Prodotto();
        prodotto2.setQuantità(5); // Quantità insufficiente
        prodotto2.setPrezzo(100.0);

        when(serviceMock.fetchByIdProdotto(500)).thenReturn(prodotto1);
        when(serviceMock.fetchByIdProdotto(501)).thenReturn(prodotto2);

        try {
            servlet.doGet(requestMock, responseMock);
            fail("MyServletException attesa per quantità eccedente");
        } catch (MyServletException ex) {
            assertEquals("Prodotto non più disponibile nelle quantità inserite dall'utente, ordine annullato", ex.getMessage());
        }

        // Si verifica che nessun ordine sia stato salvato
        verify(serviceMock, never()).saveOrdine(any(Ordine.class));
        // Si verifica che il carrello non sia stato rimosso
        verify(sessionMock, never()).removeAttribute("carrello");
    }

    /**
     * Caso procedi al pagamento: ordine confermato.
     * Simula la situazione in cui tutti i controlli siano superati, l'ordine venga salvato,
     * il carrello svuotato e si effettui il forward alla pagina di conferma.
     *
     * @throws ServletException in caso di errore nella servlet
     * @throws IOException      in caso di errore di input/output
     * @throws MyServletException se si verificano errori nelle logiche di business della servlet
     */
    @Test
    public void confermaOrdineCorrettamente() throws ServletException, IOException, MyServletException {
        Utente utente = new Utente();
        utente.setIdUtente(40);
        utente.setRuolo(false);
        when(sessionMock.getAttribute("utente")).thenReturn(utente);

        // Carrello con due item
        ItemCarrello item1 = new ItemCarrello();
        item1.setIdProdotto(400);
        item1.setQuantità(1);
        ItemCarrello item2 = new ItemCarrello();
        item2.setIdProdotto(401);
        item2.setQuantità(2);
        List<ItemCarrello> carrello = new ArrayList<>();
        carrello.add(item1);
        carrello.add(item2);
        when(sessionMock.getAttribute("carrello")).thenReturn(carrello);

        // Imposta il path per procedi al pagamento
        when(requestMock.getServletPath()).thenReturn("/procedi-al-pagamento");

        // Parametri validi per spedizione e pagamento
        when(requestMock.getParameter("nome")).thenReturn("Anna");
        when(requestMock.getParameter("cognome")).thenReturn("Verdi");
        when(requestMock.getParameter("via")).thenReturn("Via Nazioni");
        when(requestMock.getParameter("numerocivico")).thenReturn("20");
        when(requestMock.getParameter("cap")).thenReturn("11111");
        when(requestMock.getParameter("città")).thenReturn("Roma");
        when(requestMock.getParameter("telefono")).thenReturn("+391234567892");
        when(requestMock.getParameter("numeroCarta")).thenReturn("1111222233334444");
        when(requestMock.getParameter("nomeIntestatario")).thenReturn("Anna Verdi");
        // Imposta una data futura
        when(requestMock.getParameter("scadenzaCarta")).thenReturn("12/30");
        when(requestMock.getParameter("cvv")).thenReturn("456");
        when(requestMock.getParameter("metodoSpedizione")).thenReturn("Standard");

        // Simula il metodo di spedizione
        MetodoSpedizione metodo = new MetodoSpedizione();
        metodo.setNome("Standard");
        metodo.setCosto(5.0);
        when(serviceMock.fetchMetodoSpedizioneByNome("Standard")).thenReturn(metodo);
        // Indirizzo verificato
        when(serviceMock.verificaIndirizzo("Via Nazioni", "11111", "Roma")).thenReturn(true);

        // Simula i prodotti
        Prodotto prodotto1 = new Prodotto();
        prodotto1.setQuantità(10);
        prodotto1.setPrezzo(30.0);
        prodotto1.setNome("Prodotto1");
        prodotto1.setImmagine("img1.jpg");

        Prodotto prodotto2 = new Prodotto();
        prodotto2.setQuantità(10);
        prodotto2.setPrezzo(40.0);
        prodotto2.setNome("Prodotto2");
        prodotto2.setImmagine("img2.jpg");

        when(serviceMock.fetchByIdProdotto(400)).thenReturn(prodotto1);
        when(serviceMock.fetchByIdProdotto(401)).thenReturn(prodotto2);

        // Simula il salvataggio dell'ordine ritornando un id fittizio
        when(serviceMock.saveOrdine(any(Ordine.class))).thenReturn(999);

        // Simula il salvataggio degli itemOrdine (senza logica particolare)
        doNothing().when(serviceMock).saveItemOrdine(any(ItemOrdine.class));
        // Simula l'aggiornamento della quantità e rimozione del carrello
        doNothing().when(serviceMock).updateQuantitàProdotto(any(Prodotto.class));
        doNothing().when(serviceMock).rimuoviCarrelloServletByIdUtente(40);

        when(requestMock.getRequestDispatcher("/WEB-INF/results/confirm-order.jsp")).thenReturn(requestDispatcherMock);

        servlet.doGet(requestMock, responseMock);

        // Si verifica che, dopo aver effettuato l'ordine, il carrello venga rimosso dalla sessione
        verify(sessionMock).removeAttribute("carrello");

        // Si verifica che il forward avvenga verso la pagina di conferma
        verify(requestDispatcherMock).forward(requestMock, responseMock);

        // Acquisizione dell'ordine impostato come attributo per ulteriori controlli
        ArgumentCaptor<Ordine> ordineCaptor = ArgumentCaptor.forClass(Ordine.class);
        verify(requestMock).setAttribute(eq("ordine"), ordineCaptor.capture());
        Ordine ordineSalvato = ordineCaptor.getValue();
        assertNotNull(ordineSalvato);
        // Si controlla che il totale sia corretto:
        // Totale = costo spedizione (5.0) + (1*30.0) + (2*40.0) = 5 + 30 + 80 = 115.0
        assertEquals(115.0, ordineSalvato.getTotale(), 0.001);
    }

    /**
     * Metodo helper per evitare di impostare tutti i parametri ogni volta e impostare solo lo stretto necessario.
     * Imposta l'utente, il carrello e i parametri validi per il pagamento.
     */
    private void setupValidPagamentoRequest() {
        // Imposta l'utente valido
        Utente utente = new Utente();
        utente.setIdUtente(61);
        utente.setRuolo(false);
        when(sessionMock.getAttribute("utente")).thenReturn(utente);

        // Imposta il carrello valido (almeno un item)
        ItemCarrello item = new ItemCarrello();
        item.setIdProdotto(601);
        item.setQuantità(1);
        List<ItemCarrello> carrello = new ArrayList<>();
        carrello.add(item);
        when(sessionMock.getAttribute("carrello")).thenReturn(carrello);

        // Imposta i parametri validi per il pagamento
        when(requestMock.getParameter("nome")).thenReturn("Giovanni");
        when(requestMock.getParameter("cognome")).thenReturn("Rossi");
        when(requestMock.getParameter("via")).thenReturn("Via Roma");
        when(requestMock.getParameter("numerocivico")).thenReturn("10");
        when(requestMock.getParameter("cap")).thenReturn("12345");
        when(requestMock.getParameter("città")).thenReturn("Roma");
        when(requestMock.getParameter("telefono")).thenReturn("+391234567801");
        when(requestMock.getParameter("numeroCarta")).thenReturn("1234567812345678");
        when(requestMock.getParameter("nomeIntestatario")).thenReturn("Giovanni Rossi");
        when(requestMock.getParameter("scadenzaCarta")).thenReturn("12/30");
        when(requestMock.getParameter("cvv")).thenReturn("123");
        when(requestMock.getParameter("metodoSpedizione")).thenReturn("Standard");

        // Imposta il metodo di spedizione valido
        MetodoSpedizione metodo = new MetodoSpedizione();
        metodo.setNome("Standard");
        metodo.setCosto(5.0);
        when(serviceMock.fetchMetodoSpedizioneByNome("Standard")).thenReturn(metodo);

        // Imposta il percorso della servlet
        when(requestMock.getServletPath()).thenReturn("/procedi-al-pagamento");
    }

    /**
     * Caso procedi al pagamento: Test per il formato non valido del campo "nome" della spedizione.
     *
     * @throws ServletException in caso di errore nella servlet
     * @throws IOException      in caso di errore di input/output
     */
    @Test
    public void pagamentoConNomeFormatoNonValido() throws ServletException, IOException {
        setupValidPagamentoRequest();
        // Sovrascrive il parametro "nome" con un valore non valido (contiene caratteri numerici)
        when(requestMock.getParameter("nome")).thenReturn("Giovanni123");

        try {
            servlet.doGet(requestMock, responseMock);
            fail("MyServletException attesa per nome non valido");
        } catch (MyServletException ex) {
            assertEquals("Nome non valido. Deve contenere solo lettere, spazi e apostrofi, con una lunghezza massima di 255 caratteri.", ex.getMessage());
        }
    }

    /**
     * Caso procedi al pagamento: Test per il formato non valido del campo "cognome" della spedizione.
     *
     * @throws ServletException in caso di errore nella servlet
     * @throws IOException      in caso di errore di input/output
     */
    @Test
    public void pagamentoConCognomeFormatoNonValido() throws ServletException, IOException {
        setupValidPagamentoRequest();
        // Sovrascrive il parametro "cognome" con un valore non valido (contiene numeri)
        when(requestMock.getParameter("cognome")).thenReturn("Bianchi123");
        // Imposta un metodo di spedizione differente se necessario
        when(requestMock.getParameter("metodoSpedizione")).thenReturn("Express");
        MetodoSpedizione metodo = new MetodoSpedizione();
        metodo.setNome("Express");
        metodo.setCosto(10.0);
        when(serviceMock.fetchMetodoSpedizioneByNome("Express")).thenReturn(metodo);

        try {
            servlet.doGet(requestMock, responseMock);
            fail("MyServletException attesa per cognome non valido");
        } catch (MyServletException ex) {
            assertEquals("Cognome non valido. Deve contenere solo lettere, spazi e apostrofi, con una lunghezza massima di 255 caratteri.", ex.getMessage());
        }
    }

    /**
     * Caso procedi al pagamento: Test per il formato non valido del campo "via".
     *
     * @throws ServletException in caso di errore nella servlet
     * @throws IOException      in caso di errore di input/output
     */
    @Test
    public void pagamentoConViaFormatoNonValido() throws ServletException, IOException {
        setupValidPagamentoRequest();
        // Sovrascrive il parametro "via" con un valore non valido (stringa vuota)
        when(requestMock.getParameter("via")).thenReturn("");

        try {
            servlet.doGet(requestMock, responseMock);
            fail("MyServletException attesa per via non valida");
        } catch (MyServletException ex) {
            assertEquals("La via deve contenere tra 1 e 255 caratteri.", ex.getMessage());
        }
    }

    /**
     * Caso procedi al pagamento: Test per il formato non valido del campo "cap".
     *
     * @throws ServletException in caso di errore nella servlet
     * @throws IOException      in caso di errore di input/output
     */
    @Test
    public void pagamentoConCapFormatoNonValido() throws ServletException, IOException {
        setupValidPagamentoRequest();
        // Sovrascrive il parametro "cap" con un valore non valido (lunghezza diversa da 5)
        when(requestMock.getParameter("cap")).thenReturn("1234");

        try {
            servlet.doGet(requestMock, responseMock);
            fail("MyServletException attesa per CAP non valido");
        } catch (MyServletException ex) {
            assertEquals("CAP non valido. Deve contenere esattamente 5 caratteri numerici", ex.getMessage());
        }
    }

    /**
     * Caso procedi al pagamento: Test per il formato non valido del campo "città".
     *
     * @throws ServletException in caso di errore nella servlet
     * @throws IOException      in caso di errore di input/output
     */
    @Test
    public void pagamentoConCittaFormatoNonValido() throws ServletException, IOException {
        setupValidPagamentoRequest();
        // Sovrascrive il parametro "città" con un valore non valido (contiene numeri)
        when(requestMock.getParameter("città")).thenReturn("Roma123");

        try {
            servlet.doGet(requestMock, responseMock);
            fail("MyServletException attesa per città non valida");
        } catch (MyServletException ex) {
            assertEquals("Il nome della città non è valido. Deve contenere solo lettere, spazi e apostrofi, con una lunghezza massima di 255 caratteri.", ex.getMessage());
        }
    }

    /**
     * Caso procedi al pagamento: Test per il formato non valido del campo "numeroCivico".
     *
     * @throws ServletException in caso di errore nella servlet
     * @throws IOException      in caso di errore di input/output
     */
    @Test
    public void pagamentoConNumeroCivicoFormatoNonValido() throws ServletException, IOException {
        setupValidPagamentoRequest();
        // Sovrascrive il parametro "numerocivico" con un valore non valido (non numerico)
        when(requestMock.getParameter("numerocivico")).thenReturn("ABC");

        try {
            servlet.doGet(requestMock, responseMock);
            fail("MyServletException attesa per numero civico non valido");
        } catch (MyServletException ex) {
            assertEquals("Numero civico non valido.", ex.getMessage());
        }
    }

    /**
     * Caso procedi al pagamento: Test per il formato non valido del campo "telefono".
     *
     * @throws ServletException in caso di errore nella servlet
     * @throws IOException      in caso di errore di input/output
     */
    @Test
    public void pagamentoConTelefonoFormatoNonValido() throws ServletException, IOException {
        setupValidPagamentoRequest();
        // Sovrascrive il parametro "telefono" con un valore non valido
        when(requestMock.getParameter("telefono")).thenReturn("123k45");

        try {
            servlet.doGet(requestMock, responseMock);
            fail("MyServletException attesa per telefono non valido");
        } catch (MyServletException ex) {
            assertEquals("Numero di telefono non valido.", ex.getMessage());
        }
    }

    /**
     * Caso procedi al pagamento: Test per il formato non valido del campo "nomeIntestatario" della carta di credito.
     *
     * @throws ServletException in caso di errore nella servlet
     * @throws IOException      in caso di errore di input/output
     */
    @Test
    public void pagamentoConNomeIntestatarioFormatoNonValido() throws ServletException, IOException {
        setupValidPagamentoRequest();
        // Sovrascrive il parametro "nomeIntestatario" con un valore non valido (contiene numeri)
        when(requestMock.getParameter("nomeIntestatario")).thenReturn("Federico99");

        try {
            servlet.doGet(requestMock, responseMock);
            fail("MyServletException attesa per nome intestatario non valido");
        } catch (MyServletException ex) {
            assertEquals("Nome dell'intestatario della carta non valido.Deve contenere solo lettere, spazi e apostrofi, con una lunghezza massima di 255 caratteri.", ex.getMessage());
        }
    }

    /**
     * Caso procedi al pagamento: Test per il formato non valido del campo "cvv".
     *
     * @throws ServletException in caso di errore nella servlet
     * @throws IOException      in caso di errore di input/output
     */
    @Test
    public void pagamentoConCvvFormatoNonValido() throws ServletException, IOException {
        setupValidPagamentoRequest();
        // Sovrascrive il parametro "cvv" con un valore non valido (4 cifre anziché 3)
        when(requestMock.getParameter("cvv")).thenReturn("1234");

        try {
            servlet.doGet(requestMock, responseMock);
            fail("MyServletException attesa per CVV non valido");
        } catch (MyServletException ex) {
            assertEquals("CVV non valido. Deve essere di 3 cifre numeriche.", ex.getMessage());
        }
    }

    /**
     * Caso procedi al pagamento: Test per il formato non valido del campo "numeroCarta".
     *
     * @throws ServletException in caso di errore nella servlet
     * @throws IOException      in caso di errore di input/output
     */
    @Test
    public void pagamentoConNumeroCartaFormatoNonValido() throws ServletException, IOException {
        setupValidPagamentoRequest();
        // Sovrascrive il parametro "numeroCarta" con un valore non valido (meno di 16 cifre)
        when(requestMock.getParameter("numeroCarta")).thenReturn("1234 5678 1234 567");

        try {
            servlet.doGet(requestMock, responseMock);
            fail("MyServletException attesa per numero carta non valido");
        } catch (MyServletException ex) {
            assertEquals("Numero della carta di credito non valido. Deve essere di 16 cifre.", ex.getMessage());
        }
    }

    /**
     * Caso procedi al pagamento: Test per il formato non valido della data di scadenza della carta.
     *
     * @throws ServletException in caso di errore nella servlet
     * @throws IOException      in caso di errore di input/output
     */
    @Test
    public void pagamentoConScadenzaCartaFormatoNonValido() throws ServletException, IOException {
        setupValidPagamentoRequest();
        // Sovrascrive il parametro "scadenzaCarta" con un formato non valido (uso di '-' anziché '/')
        when(requestMock.getParameter("scadenzaCarta")).thenReturn("12-30");

        try {
            servlet.doGet(requestMock, responseMock);
            fail("MyServletException attesa per scadenza carta non valida");
        } catch (MyServletException ex) {
            assertEquals("Data di scadenza della carta non valida. Deve essere nel formato MM/YY.", ex.getMessage());
        }
    }

    /**
     * Caso procedi al pagamento: Test per verificare che venga lanciata l'eccezione se la carta di credito è scaduta.
     *
     * @throws ServletException in caso di errore nella servlet
     * @throws IOException      in caso di errore di input/output
     */
    @Test
    public void pagamentoConCartaScaduta() throws ServletException, IOException {
        setupValidPagamentoRequest();
        // Sovrascrive il parametro "scadenzaCarta" con una data passata
        when(requestMock.getParameter("scadenzaCarta")).thenReturn("01/20");

        try {
            servlet.doGet(requestMock, responseMock);
            fail("MyServletException attesa per carta scaduta");
        } catch (MyServletException ex) {
            assertEquals("La carta di credito è scaduta.", ex.getMessage());
        }
    }

}
