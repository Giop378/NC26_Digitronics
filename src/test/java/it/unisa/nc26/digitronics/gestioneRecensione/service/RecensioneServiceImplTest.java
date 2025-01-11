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

@RunWith(MockitoJUnitRunner.class)
public class RecensioneServiceImplTest {
    private RecensioneServiceImpl recensioneService;
    @Mock
    private RecensioneDAO mockRecensioneDAO;
    @Mock
    private ProdottoDAO mockProdottoDAO;
    @Before
    public void setUp() {
        recensioneService = new RecensioneServiceImpl();
        recensioneService.setRecensioneDAO(mockRecensioneDAO);
        recensioneService.setProdottoDAO(mockProdottoDAO);
    }

    @Test
    public void testSaveRecensione() throws SQLException {
        Recensione recensione = new Recensione();
        recensioneService.saveRecensione(recensione);
        verify(mockRecensioneDAO, times(1)).doSave(recensione);
    }

    @Test
    public void testFetchByProduct() throws SQLException {
        int idProdotto = 1;
        Collection<Recensione> expectedRecensioni = new ArrayList<>();
        when(mockRecensioneDAO.doRetrieveByProduct(idProdotto)).thenReturn(expectedRecensioni);

        Collection<Recensione> actualRecensioni = recensioneService.fetchByProduct(idProdotto);

        assertEquals(expectedRecensioni, actualRecensioni);
        verify(mockRecensioneDAO, times(1)).doRetrieveByProduct(idProdotto);
    }


    @Test
    public void testFetchByUtente() throws SQLException {
        int idUtente = 1;
        Collection<Recensione> expectedRecensioni = new ArrayList<>();
        when(mockRecensioneDAO.doRetrieveByUtente(idUtente)).thenReturn(expectedRecensioni);

        Collection<Recensione> actualRecensioni = recensioneService.fetchByUtente(idUtente);

        assertEquals(expectedRecensioni, actualRecensioni);
        verify(mockRecensioneDAO, times(1)).doRetrieveByUtente(idUtente);
    }

    @Test(expected = SQLException.class)
    public void testSaveRecensioneThrowsSQLException() throws SQLException {
        Recensione recensione = new Recensione();
        doThrow(new SQLException()).when(mockRecensioneDAO).doSave(recensione);

        recensioneService.saveRecensione(recensione);
    }

    @Test(expected = SQLException.class)
    public void testFetchByProductThrowsSQLException() throws SQLException {
        int idProdotto = 1;
        when(mockRecensioneDAO.doRetrieveByProduct(idProdotto)).thenThrow(new SQLException());

        recensioneService.fetchByProduct(idProdotto);
    }

    @Test(expected = SQLException.class)
    public void testFetchByUtenteThrowsSQLException() throws SQLException {
        int idUtente = 1;
        when(mockRecensioneDAO.doRetrieveByUtente(idUtente)).thenThrow(new SQLException());

        recensioneService.fetchByUtente(idUtente);
    }
    
}
