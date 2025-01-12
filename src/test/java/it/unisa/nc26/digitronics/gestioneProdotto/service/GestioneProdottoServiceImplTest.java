package it.unisa.nc26.digitronics.gestioneProdotto.service;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import it.unisa.nc26.digitronics.model.bean.Categoria;
import it.unisa.nc26.digitronics.model.bean.Prodotto;
import it.unisa.nc26.digitronics.model.dao.CategoriaDAO;
import it.unisa.nc26.digitronics.model.dao.ProdottoDAO;
import it.unisa.nc26.digitronics.utils.MyServletException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Test della classe {@link GestioneProdottoServiceImpl}.
 *
 * Questa classe utilizza Mockito per simulare il comportamento dei DAO
 * (Data Access Object) e testare i metodi del servizio di gestione dei prodotti.
 * Include vari test unitari per verificare il comportamento con input validi
 * e non validi, assicurandosi che i metodi del servizio funzionino come previsto.
 */
@RunWith(MockitoJUnitRunner.class)
public class GestioneProdottoServiceImplTest {

    /** Oggetto del servizio da testare, con mock iniettati. */
    @InjectMocks
    private GestioneProdottoServiceImpl gestioneProdottoService;

    /** Mock del DAO per la gestione delle categorie. */
    @Mock
    private CategoriaDAO categoriaDAOMock;

    /** Mock del DAO per la gestione dei prodotti. */
    @Mock
    private ProdottoDAO prodottoDAOMock;

    /**
     * Test per verificare che la modifica di un prodotto esistente aggiorni correttamente
     * i dati, mantenendo eventuali informazioni mancanti come l'immagine.
     */
    @Test
    public void testModificaProdotto_Successo() throws Exception {
        Prodotto prodottoEsistente = new Prodotto();
        prodottoEsistente.setIdProdotto(1);
        prodottoEsistente.setNomeCategoria("Elettronica");
        prodottoEsistente.setImmagine("immagine1.jpg");

        Prodotto prodottoModificato = new Prodotto();
        prodottoModificato.setIdProdotto(1);
        prodottoModificato.setNomeCategoria("Elettronica");

        Categoria categoria = new Categoria();
        categoria.setNome("Elettronica");

        when(prodottoDAOMock.doRetrieveById(1)).thenReturn(prodottoEsistente);
        when(categoriaDAOMock.doRetrieveByNomeCategoria("Elettronica")).thenReturn(categoria);

        gestioneProdottoService.modificaProdotto(prodottoModificato);

        assertEquals("immagine1.jpg", prodottoModificato.getImmagine());
        verify(prodottoDAOMock).doUpdate(prodottoModificato);
    }

    /**
     * Test per verificare che venga lanciata un'eccezione {@link MyServletException}
     * se si tenta di modificare un prodotto inesistente.
     */
    @Test
    public void testModificaProdotto_ProdottoNonEsistente() {
        Prodotto prodottoModificato = new Prodotto();
        prodottoModificato.setIdProdotto(1);

        when(prodottoDAOMock.doRetrieveById(1)).thenReturn(null);

        try {
            gestioneProdottoService.modificaProdotto(prodottoModificato);
            fail("Doveva essere lanciata un'eccezione");
        } catch (MyServletException e) {
            assertEquals("Prodotto non esistente", e.getMessage());
        }
    }

    /**
     * Test per verificare che venga lanciata un'eccezione {@link MyServletException}
     * se si tenta di modificare un prodotto associato a una categoria non esistente.
     */
    @Test
    public void testModificaProdotto_CategoriaNonEsistente() {
        Prodotto prodottoEsistente = new Prodotto();
        prodottoEsistente.setIdProdotto(1);

        Prodotto prodottoModificato = new Prodotto();
        prodottoModificato.setIdProdotto(1);
        prodottoModificato.setNomeCategoria("CategoriaInesistente");

        when(prodottoDAOMock.doRetrieveById(1)).thenReturn(prodottoEsistente);
        when(categoriaDAOMock.doRetrieveByNomeCategoria("CategoriaInesistente")).thenReturn(null);

        try {
            gestioneProdottoService.modificaProdotto(prodottoModificato);
            fail("Doveva essere lanciata un'eccezione");
        } catch (MyServletException e) {
            assertEquals("Categoria non esistente", e.getMessage());
        }
    }

    /**
     * Test per verificare che l'aggiunta di un prodotto con immagine nulla o vuota
     * venga gestita correttamente.
     */
    @Test
    public void testAggiungiProdotto_ImmagineNulla() throws Exception {
        Prodotto nuovoProdotto = new Prodotto();
        nuovoProdotto.setNomeCategoria("Elettronica");
        nuovoProdotto.setImmagine(null);

        Categoria categoria = new Categoria();
        categoria.setNome("Elettronica");

        when(categoriaDAOMock.doRetrieveByNomeCategoria("Elettronica")).thenReturn(categoria);
        gestioneProdottoService.aggiungiProdotto(nuovoProdotto);

        verify(prodottoDAOMock).doSave(nuovoProdotto);
    }

    /**
     * Test per verificare che l'aggiunta di un prodotto con immagine vuota
     * venga gestita correttamente.
     */
    @Test
    public void testAggiungiProdotto_ImmagineVuota() throws Exception {
        Prodotto nuovoProdotto = new Prodotto();
        nuovoProdotto.setNomeCategoria("Elettronica");
        nuovoProdotto.setImmagine("");

        Categoria categoria = new Categoria();
        categoria.setNome("Elettronica");

        when(categoriaDAOMock.doRetrieveByNomeCategoria("Elettronica")).thenReturn(categoria);
        gestioneProdottoService.aggiungiProdotto(nuovoProdotto);

        verify(prodottoDAOMock).doSave(nuovoProdotto);
    }

    /**
     * Test per verificare che un prodotto venga aggiunto correttamente
     * con parametri validi.
     */
    @Test
    public void testAggiungiProdotto_Successo() throws Exception {
        Prodotto nuovoProdotto = new Prodotto();
        nuovoProdotto.setNomeCategoria("Elettronica");

        Categoria categoria = new Categoria();
        categoria.setNome("Elettronica");

        when(categoriaDAOMock.doRetrieveByNomeCategoria("Elettronica")).thenReturn(categoria);
        gestioneProdottoService.aggiungiProdotto(nuovoProdotto);

        verify(prodottoDAOMock).doSave(nuovoProdotto);
    }

    /**
     * Test per verificare che venga lanciata un'eccezione {@link MyServletException}
     * se si tenta di aggiungere un prodotto associato a una categoria non esistente.
     */
    @Test
    public void testAggiungiProdotto_CategoriaNonEsistente() {
        Prodotto nuovoProdotto = new Prodotto();
        nuovoProdotto.setNomeCategoria("CategoriaInesistente");

        when(categoriaDAOMock.doRetrieveByNomeCategoria("CategoriaInesistente")).thenReturn(null);

        try {
            gestioneProdottoService.aggiungiProdotto(nuovoProdotto);
            fail("Doveva essere lanciata un'eccezione");
        } catch (MyServletException e) {
            assertEquals("Categoria non esistente", e.getMessage());
        }
    }

    /**
     * Test per verificare che l'eliminazione di un prodotto avvenga correttamente
     * se l'ID è valido.
     */
    @Test
    public void testEliminaProdotto_Successo() {
        int idProdotto = 1;

        gestioneProdottoService.eliminaProdotto(idProdotto);

        verify(prodottoDAOMock).doDelete(idProdotto);
    }

    /**
     * Test per verificare che venga gestito correttamente un errore
     * quando si tenta di eliminare un prodotto inesistente.
     */
    @Test
    public void testEliminaProdotto_IDInesistente() {
        doThrow(new RuntimeException("Prodotto non trovato")).when(prodottoDAOMock).doDelete(999);

        try {
            gestioneProdottoService.eliminaProdotto(999);
            fail("Doveva essere lanciata un'eccezione");
        } catch (RuntimeException e) {
            assertEquals("Prodotto non trovato", e.getMessage());
        }

        verify(prodottoDAOMock, times(1)).doDelete(999);
    }

    /**
     * Test per verificare che venga gestito correttamente un errore
     * quando si tenta di eliminare un prodotto già eliminato.
     */
    @Test
    public void testEliminaProdotto_ProdottoGiaEliminato() {
        doThrow(new RuntimeException("Prodotto già eliminato")).when(prodottoDAOMock).doDelete(2);

        try {
            gestioneProdottoService.eliminaProdotto(2);
            fail("Doveva essere lanciata un'eccezione");
        } catch (RuntimeException e) {
            assertEquals("Prodotto già eliminato", e.getMessage());
        }

        verify(prodottoDAOMock, times(1)).doDelete(2);
    }
}

