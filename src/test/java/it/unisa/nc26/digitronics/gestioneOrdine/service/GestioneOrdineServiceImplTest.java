package it.unisa.nc26.digitronics.gestioneOrdine.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import it.unisa.nc26.digitronics.gestioneOrdine.service.IndirizzoAdapter.VerificaIndirizzoApiAdapter;
import it.unisa.nc26.digitronics.model.bean.ItemOrdine;
import it.unisa.nc26.digitronics.model.bean.MetodoSpedizione;
import it.unisa.nc26.digitronics.model.bean.Ordine;
import it.unisa.nc26.digitronics.model.bean.Prodotto;
import it.unisa.nc26.digitronics.model.dao.*;
import it.unisa.nc26.digitronics.utils.MyServletException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Classe di test per la classe {@link GestioneOrdineServiceImpl}.
 * Verifica il corretto funzionamento dei metodi implementati per la gestione degli ordini.
 */
public class GestioneOrdineServiceImplTest {

    private GestioneOrdineServiceImpl service;

    @Mock
    private ProdottoDAO prodottoDAO;

    @Mock
    private MetodoSpedizioneDAO metodoSpedizioneDAO;

    @Mock
    private OrdineDAO ordineDAO;

    @Mock
    private ItemCarrelloDAO itemCarrelloDAO;

    @Mock
    private ItemOrdineDAO itemOrdineDAO;

    @Mock
    private VerificaIndirizzoApiAdapter verificaIndirizzoApiAdapter;

    /**
     * Inizializza i mock e la service da testare.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        service = new GestioneOrdineServiceImpl();

        // Iniettare i mock
        service.setProdottoDAO(prodottoDAO);
        service.setMetodoSpedizioneDAO(metodoSpedizioneDAO);
        service.setOrdineDAO(ordineDAO);
        service.setItemCarrelloDAO(itemCarrelloDAO);
        service.setItemOrdineDAO(itemOrdineDAO);
        service.setVerificaIndirizzoApiAdapter(verificaIndirizzoApiAdapter);
    }

    /**
     * Testa il metodo {@link GestioneOrdineServiceImpl#fetchByIdProdotto(int)} per un prodotto esistente.
     * Verifica che venga restituito il prodotto corretto.
     *
     * @throws MyServletException se il prodotto non esiste
     */
    @Test
    public void testFetchByIdProdotto_esistente() throws MyServletException {
        int idProdotto = 1;
        Prodotto prodotto = new Prodotto();
        when(prodottoDAO.doRetrieveById(idProdotto)).thenReturn(prodotto);

        Prodotto result = service.fetchByIdProdotto(idProdotto);
        assertNotNull("Il prodotto non deve essere null", result);
        assertEquals("Il prodotto restituito deve essere quello atteso", prodotto, result);
        verify(prodottoDAO).doRetrieveById(idProdotto);
    }

    /**
     * Testa il metodo {@link GestioneOrdineServiceImpl#fetchByIdProdotto(int)} per un prodotto non esistente.
     * Verifica che venga lanciata un'eccezione {@link MyServletException}.
     */
    @Test
    public void testFetchByIdProdotto_nonEsistente() {
        int idProdotto = 999;
        when(prodottoDAO.doRetrieveById(idProdotto)).thenReturn(null);

        try {
            service.fetchByIdProdotto(idProdotto);
            fail("Eccezione MyServletException attesa per prodotto non esistente.");
        } catch (MyServletException ex) {
            assertEquals("Prodotto con ID " + idProdotto + " non esistente.", ex.getMessage());
        }
    }

    /**
     * Testa il metodo {@link GestioneOrdineServiceImpl#fetchAllMetodiSpedizione()}.
     * Verifica che venga restituita la lista completa dei metodi di spedizione.
     */
    @Test
    public void testFetchAllMetodiSpedizione() {
        List<MetodoSpedizione> listaMetodi = Arrays.asList(new MetodoSpedizione(), new MetodoSpedizione());
        when(metodoSpedizioneDAO.doRetrieveAll()).thenReturn(listaMetodi);

        List<MetodoSpedizione> result = service.fetchAllMetodiSpedizione();
        assertNotNull(result);
        assertEquals("La lista dei metodi spedizione deve avere la dimensione attesa",
                listaMetodi.size(), result.size());
        verify(metodoSpedizioneDAO).doRetrieveAll();
    }

    /**
     * Testa il metodo {@link GestioneOrdineServiceImpl#fetchMetodoSpedizioneByNome(String)}.
     * Verifica che venga restituito il metodo di spedizione corretto in base al nome.
     */
    @Test
    public void testFetchMetodoSpedizioneByNome() {
        String nome = "Standard";
        MetodoSpedizione metodo = new MetodoSpedizione();
        when(metodoSpedizioneDAO.doRetrieveByName(nome)).thenReturn(metodo);

        MetodoSpedizione result = service.fetchMetodoSpedizioneByNome(nome);
        assertNotNull(result);
        assertEquals(metodo, result);
        verify(metodoSpedizioneDAO).doRetrieveByName(nome);
    }

    /**
     * Testa il metodo {@link GestioneOrdineServiceImpl#saveOrdine(Ordine)}.
     * Verifica che l'ordine venga salvato e che venga restituito l'id generato.
     */
    @Test
    public void testSaveOrdine() {
        Ordine ordine = new Ordine();
        int generatedId = 10;
        when(ordineDAO.doSave(ordine)).thenReturn(generatedId);

        int resultId = service.saveOrdine(ordine);
        assertEquals(generatedId, resultId);
        verify(ordineDAO).doSave(ordine);
    }

    /**
     * Testa il metodo {@link GestioneOrdineServiceImpl#updateQuantitàProdotto(Prodotto)}.
     * Verifica che il metodo update del DAO venga invocato correttamente.
     */
    @Test
    public void testUpdateQuantitàProdotto() {
        Prodotto prodotto = new Prodotto();

        service.updateQuantitàProdotto(prodotto);
        // Qui non essendoci ritorni dalla parte del metodo updateQuantitàProdotto si è solo verificato che il metodo relativo del dao è stato chiamato
        verify(prodottoDAO).doUpdate(prodotto);
    }

    /**
     * Testa il metodo {@link GestioneOrdineServiceImpl#rimuoviCarrelloServletByIdUtente(int)}.
     * Verifica che il metodo delete del DAO venga invocato correttamente per l'ID utente.
     */
    @Test
    public void testRimuoviCarrelloServletByIdUtente() {
        int idUtente = 5;
        service.rimuoviCarrelloServletByIdUtente(idUtente);
        verify(itemCarrelloDAO).doDelete(idUtente);
    }

    /**
     * Testa il metodo {@link GestioneOrdineServiceImpl#saveItemOrdine(ItemOrdine)}.
     * Verifica che il metodo save del DAO venga invocato correttamente per l'oggetto {@link ItemOrdine}.
     */
    @Test
    public void testSaveItemOrdine() {
        ItemOrdine itemOrdine = new ItemOrdine();
        service.saveItemOrdine(itemOrdine);
        verify(itemOrdineDAO).doSave(itemOrdine);
    }

    /**
     * Testa il metodo {@link GestioneOrdineServiceImpl#verificaIndirizzo(String, String, String)} con un indirizzo valido.
     * Verifica che venga restituito {@code true}.
     */
    @Test
    public void testVerificaIndirizzo_true() {
        String via = "Via esistente";
        String cap = "00001";
        String città = "Roma";
        when(verificaIndirizzoApiAdapter.verifica(via, cap, città)).thenReturn(true);

        boolean result = service.verificaIndirizzo(via, cap, città);
        assertTrue("L'indirizzo dovrebbe essere verificato come valido", result);
        verify(verificaIndirizzoApiAdapter).verifica(via, cap, città);
    }

    /**
     * Testa il metodo {@link GestioneOrdineServiceImpl#verificaIndirizzo(String, String, String)} con un indirizzo non valido.
     * Verifica che venga restituito {@code false}.
     */
    @Test
    public void testVerificaIndirizzo_false() {
        String via = "Via Inesistente";
        String cap = "00000";
        String città = "Nessuna";
        when(verificaIndirizzoApiAdapter.verifica(via, cap, città)).thenReturn(false);

        boolean result = service.verificaIndirizzo(via, cap, città);
        assertFalse("L'indirizzo dovrebbe essere verificato come non valido", result);
        verify(verificaIndirizzoApiAdapter).verifica(via, cap, città);
    }

    /**
     * Testa il metodo {@link GestioneOrdineServiceImpl#fetchByIdUtente(int)}.
     * Verifica che venga restituita la lista corretta degli ordini per un dato utente.
     */
    @Test
    public void testFetchByIdUtente() {
        int idUtente = 3;
        List<Ordine> listaOrdini = Arrays.asList(new Ordine(), new Ordine());
        when(ordineDAO.doRetrieveByCustomer(idUtente)).thenReturn(listaOrdini);

        List<Ordine> result = service.fetchByIdUtente(idUtente);
        assertNotNull("Il metodo deve restituire una lista non nulla", result);
        assertEquals("Il risultato deve avere la stessa dimensione della lista attesa", listaOrdini.size(), result.size());
        assertEquals("Il risultato deve essere uguale alla lista attesa", listaOrdini, result);
        verify(ordineDAO).doRetrieveByCustomer(idUtente);
    }

    /**
     * Testa il metodo {@link GestioneOrdineServiceImpl#fetchAllOrders()}.
     * Verifica che venga restituita la lista completa degli ordini.
     */
    @Test
    public void testFetchAllOrders() {
        List<Ordine> listaOrdini = Arrays.asList(new Ordine(), new Ordine(), new Ordine());
        when(ordineDAO.doRetrieveAll()).thenReturn(listaOrdini);

        List<Ordine> result = service.fetchAllOrders();
        assertNotNull(result);
        assertEquals(listaOrdini.size(), result.size());
        verify(ordineDAO).doRetrieveAll();
    }

    /**
     * Testa il metodo {@link GestioneOrdineServiceImpl#fetchItemOrder(int)} per un ordine esistente.
     * Verifica che venga restituita la lista corretta degli item d'ordine.
     *
     * @throws MyServletException se la lista degli item è vuota
     */
    @Test
    public void testFetchItemOrder_esistente() throws MyServletException {
        int idOrdine = 7;
        List<ItemOrdine> itemsOrdine = Arrays.asList(new ItemOrdine(), new ItemOrdine());
        when(itemOrdineDAO.doRetrieveByOrdine(idOrdine)).thenReturn(itemsOrdine);

        List<ItemOrdine> result = service.fetchItemOrder(idOrdine);
        assertNotNull(result);
        assertFalse("La lista degli item deve non essere vuota", result.isEmpty());
        verify(itemOrdineDAO).doRetrieveByOrdine(idOrdine);
    }

    /**
     * Testa il metodo {@link GestioneOrdineServiceImpl#fetchItemOrder(int)} per un ordine inesistente.
     * Verifica che venga lanciata un'eccezione {@link MyServletException} in caso di lista vuota.
     */
    @Test
    public void testFetchItemOrder_nonEsistente() {
        int idOrdine = 50;
        when(itemOrdineDAO.doRetrieveByOrdine(idOrdine)).thenReturn(Collections.emptyList());

        try {
            service.fetchItemOrder(idOrdine);
            fail("Doveva lanciare MyServletException per lista vuota.");
        } catch (MyServletException ex) {
            assertEquals("Nessun elemento trovato per l'ordine con ID " + idOrdine + ".", ex.getMessage());
        }
    }

    /**
     * Testa il metodo {@link GestioneOrdineServiceImpl#fetchByIdOrder(int)} per un ordine esistente.
     * Verifica che venga restituito l'ordine corretto.
     *
     * @throws MyServletException se l'ordine non esiste
     */
    @Test
    public void testFetchByIdOrder_esistente() throws MyServletException {
        int idOrdine = 20;
        Ordine ordine = new Ordine();
        when(ordineDAO.doRetrieveByIdOrder(idOrdine)).thenReturn(ordine);

        Ordine result = service.fetchByIdOrder(idOrdine);
        assertNotNull(result);
        assertEquals(ordine, result);
        verify(ordineDAO).doRetrieveByIdOrder(idOrdine);
    }

    /**
     * Testa il metodo {@link GestioneOrdineServiceImpl#fetchByIdOrder(int)} per un ordine inesistente.
     * Verifica che venga lanciata un'eccezione {@link MyServletException}.
     */
    @Test
    public void testFetchByIdOrder_nonEsistente() {
        int idOrdine = 100;
        when(ordineDAO.doRetrieveByIdOrder(idOrdine)).thenReturn(null);

        try {
            service.fetchByIdOrder(idOrdine);
            fail("Doveva lanciare MyServletException per ordine non esistente.");
        } catch (MyServletException ex) {
            assertEquals("Ordine con ID " + idOrdine + " non esistente.", ex.getMessage());
        }
    }
}
