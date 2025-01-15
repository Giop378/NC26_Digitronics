package it.unisa.nc26.digitronics.gestioneOrdine.controller;

import it.unisa.nc26.digitronics.gestioneOrdine.service.GestioneOrdineServiceImpl;
import it.unisa.nc26.digitronics.model.bean.*;
import it.unisa.nc26.digitronics.utils.ConPool;
import it.unisa.nc26.digitronics.utils.MyServletException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
/**
 * Classe di test d'integrazione per la servlet GestioneOrdineServlet.
 * Questa classe testa il comportamento della servlet in diversi scenari,
 * utilizzando un database H2 in memoria per simulare il backend dei dati.
 */
public class GestioneOrdineServletIntegrationTest {
    /**
     * Oggetto GestioneOrdineServlet da testare.
     */
    private GestioneOrdineServlet servlet;
    /**
     * Vari mock che servono per simulare l'interfaccia con http
     */
    private HttpServletRequest requestMock;
    private HttpServletResponse responseMock;
    private HttpSession sessionMock;
    private RequestDispatcher requestDispatcherMock;


    /**
     * Configurazione iniziale dei test.
     * Inizializza la servlet, i mock e il database H2 in memoria.
     *
     * @throws SQLException in caso di errori nella configurazione del database.
     */
    @Before
    public void setUp() throws SQLException {
        // Inizializza la servlet
        servlet = new GestioneOrdineServlet();

        // Inizializza i mock per request, response, session e dispatcher
        requestMock = mock(HttpServletRequest.class);
        responseMock = mock(HttpServletResponse.class);
        sessionMock = mock(HttpSession.class);
        requestDispatcherMock = mock(RequestDispatcher.class);

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(requestMock.getRequestDispatcher(anyString())).thenReturn(requestDispatcherMock);

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
    }

    /**
     * Testa il caso in cui inizio-checkout nella servlet GestioneOrdineServlet va a buon fine e quindi rimanda al
     * pagina contenente il form con tutti i parametri da inserire per svolgere l'ordine
     *
     * @throws ServletException in caso di errore nella servlet.
     * @throws IOException in caso di errore di input/output.
     * @throws MyServletException in caso di errori specifici dell'applicazione.
     */
    @Test
    public void inizioCheckoutCorretto() throws ServletException, IOException, MyServletException {
        Utente utente = new Utente();
        utente.setIdUtente(10);
        utente.setRuolo(false);
        when(sessionMock.getAttribute("utente")).thenReturn(utente);

        // Costruisce un carrello con un item
        ItemCarrello item = new ItemCarrello();
        item.setIdProdotto(1);
        item.setQuantità(2);
        List<ItemCarrello> carrello = new ArrayList<>();
        carrello.add(item);
        when(sessionMock.getAttribute("carrello")).thenReturn(carrello);

        // Imposta il path per la servlet
        when(requestMock.getServletPath()).thenReturn("/inizio-checkout");
        when(requestMock.getRequestDispatcher("/WEB-INF/results/checkout.jsp")).thenReturn(requestDispatcherMock);

        servlet.doGet(requestMock, responseMock);

        // Si verifica che l'attributo prezzoTotale sia stato impostato correttamente: 2 * 10.0 = 20.0
        verify(requestMock).setAttribute("prezzoTotale", 20.0);
        // Si verifica il forward alla pagina di checkout
        verify(requestDispatcherMock).forward(requestMock, responseMock);
    }

    /**
     * Testa il caso in cui procedi-al-pagamento va a buon fine e quindi l'ordine viene salvato, il carrello svuotato
     * e si verifica che viene fatto il forward alla pagina di conferma
     *
     * @throws ServletException in caso di errore nella servlet.
     * @throws IOException in caso di errore di input/output.
     * @throws MyServletException in caso di errori specifici dell'applicazione.
     */
    @Test
    public void procediPagamentoCorretto() throws ServletException, IOException, MyServletException {
        Utente utente = new Utente();
        utente.setIdUtente(1);
        utente.setRuolo(false);
        when(sessionMock.getAttribute("utente")).thenReturn(utente);

        // Carrello con due item
        ItemCarrello item1 = new ItemCarrello();
        item1.setIdProdotto(1);
        item1.setQuantità(1);
        ItemCarrello item2 = new ItemCarrello();
        item2.setIdProdotto(2);
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
        when(requestMock.getParameter("via")).thenReturn("Piazza Duomo");
        when(requestMock.getParameter("numerocivico")).thenReturn("20");
        when(requestMock.getParameter("cap")).thenReturn("84011");
        when(requestMock.getParameter("città")).thenReturn("Pogerola");
        when(requestMock.getParameter("telefono")).thenReturn("+391234567892");
        when(requestMock.getParameter("numeroCarta")).thenReturn("1111222233334444");
        when(requestMock.getParameter("nomeIntestatario")).thenReturn("Anna Verdi");
        // Imposta una data futura
        when(requestMock.getParameter("scadenzaCarta")).thenReturn("12/30");
        when(requestMock.getParameter("cvv")).thenReturn("456");
        when(requestMock.getParameter("metodoSpedizione")).thenReturn("Standard");

        when(requestMock.getRequestDispatcher("/WEB-INF/results/confirm-order.jsp")).thenReturn(requestDispatcherMock);

        servlet.doGet(requestMock, responseMock);

        // Si verifica che, dopo aver effettuato l'ordine, il carrello venga rimosso dalla sessione
        verify(sessionMock).removeAttribute("carrello");

        // Si verifica che il forward avvenga verso la pagina di conferma
        verify(requestDispatcherMock).forward(requestMock, responseMock);

        ArgumentCaptor<Ordine> ordineCaptor = ArgumentCaptor.forClass(Ordine.class);
        verify(requestMock).setAttribute(eq("ordine"), ordineCaptor.capture());
        Ordine ordineSalvato = ordineCaptor.getValue();
        assertNotNull(ordineSalvato);
        // Si controlla che il totale sia corretto:
        assertEquals(65.00, ordineSalvato.getTotale(), 0.001);
    }

}
