package it.unisa.nc26.digitronics.gestioneRecensione.service;

import it.unisa.nc26.digitronics.model.bean.Recensione;
import it.unisa.nc26.digitronics.model.dao.ProdottoDAO;
import it.unisa.nc26.digitronics.model.dao.RecensioneDAO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test della classe {@link RecensioneServiceImpl}.
 *
 * Questa classe verifica il corretto funzionamento dei metodi di business della classe di servizio
 * che gestisce le recensioni, utilizzando Mockito per simulare il comportamento
 * dei DAO sottostanti.
 */
@RunWith(MockitoJUnitRunner.class)
public class RecensioneServiceImplTest {
    /** Istanza del servizio sotto test. */
    private RecensioneServiceImpl recensioneService;

    /** Mock del DAO per la gestione delle recensioni. */
    @Mock
    private RecensioneDAO mockRecensioneDAO;

    /** Mock del DAO per la gestione dei prodotti. */
    @Mock
    private ProdottoDAO mockProdottoDAO;

    /**
     * Configura l'ambiente di test prima di ogni esecuzione.
     * Inizializza il servizio e assegna i DAO simulati.
     */
    @Before
    public void setUp() {
        recensioneService = new RecensioneServiceImpl();
        recensioneService.setRecensioneDAO(mockRecensioneDAO);
        recensioneService.setProdottoDAO(mockProdottoDAO);
    }

    /**
     * Verifica che il metodo {@link RecensioneServiceImpl#saveRecensione(Recensione)}
     * invochi correttamente il metodo di salvataggio sul DAO.
     *
     * @throws SQLException se si verifica un errore durante l'esecuzione del metodo DAO.
     */
    @Test
    public void testSaveRecensione() throws SQLException {
        Recensione recensione = new Recensione();
        recensioneService.saveRecensione(recensione);
        verify(mockRecensioneDAO, times(1)).doSave(recensione);
    }

    /**
     * Verifica che il metodo {@link RecensioneServiceImpl#fetchByProduct(int)}
     * recuperi correttamente le recensioni di un prodotto specifico.
     *
     * @throws SQLException se si verifica un errore durante l'esecuzione del metodo DAO.
     */
    @Test
    public void testFetchByProduct() throws SQLException {
        int idProdotto = 1;
        Collection<Recensione> expectedRecensioni = new ArrayList<>();
        when(mockRecensioneDAO.doRetrieveByProduct(idProdotto)).thenReturn(expectedRecensioni);

        Collection<Recensione> actualRecensioni = recensioneService.fetchByProduct(idProdotto);

        assertEquals(expectedRecensioni, actualRecensioni);
        verify(mockRecensioneDAO, times(1)).doRetrieveByProduct(idProdotto);
    }

    /**
     * Verifica che il metodo {@link RecensioneServiceImpl#fetchByUtente(int)}
     * recuperi correttamente le recensioni di un utente specifico.
     *
     * @throws SQLException se si verifica un errore durante l'esecuzione del metodo DAO.
     */
    @Test
    public void testFetchByUtente() throws SQLException {
        int idUtente = 1;
        Collection<Recensione> expectedRecensioni = new ArrayList<>();
        when(mockRecensioneDAO.doRetrieveByUtente(idUtente)).thenReturn(expectedRecensioni);

        Collection<Recensione> actualRecensioni = recensioneService.fetchByUtente(idUtente);

        assertEquals(expectedRecensioni, actualRecensioni);
        verify(mockRecensioneDAO, times(1)).doRetrieveByUtente(idUtente);
    }

    /**
     * Verifica che il metodo {@link RecensioneServiceImpl#saveRecensione(Recensione)}
     * sollevi una {@link SQLException} in caso di errore nel DAO.
     *
     * @throws SQLException se si verifica un errore simulato.
     */
    @Test(expected = SQLException.class)
    public void testSaveRecensioneThrowsSQLException() throws SQLException {
        Recensione recensione = new Recensione();
        doThrow(new SQLException()).when(mockRecensioneDAO).doSave(recensione);

        recensioneService.saveRecensione(recensione);
    }

    /**
     * Verifica che il metodo {@link RecensioneServiceImpl#fetchByProduct(int)}
     * sollevi una {@link SQLException} in caso di errore nel DAO.
     *
     * @throws SQLException se si verifica un errore simulato.
     */
    @Test(expected = SQLException.class)
    public void testFetchByProductThrowsSQLException() throws SQLException {
        int idProdotto = 1;
        when(mockRecensioneDAO.doRetrieveByProduct(idProdotto)).thenThrow(new SQLException());

        recensioneService.fetchByProduct(idProdotto);
    }

    /**
     * Verifica che il metodo {@link RecensioneServiceImpl#fetchByUtente(int)}
     * sollevi una {@link SQLException} in caso di errore nel DAO.
     *
     * @throws SQLException se si verifica un errore simulato.
     */
    @Test(expected = SQLException.class)
    public void testFetchByUtenteThrowsSQLException() throws SQLException {
        int idUtente = 1;
        when(mockRecensioneDAO.doRetrieveByUtente(idUtente)).thenThrow(new SQLException());

        recensioneService.fetchByUtente(idUtente);
    }
}

