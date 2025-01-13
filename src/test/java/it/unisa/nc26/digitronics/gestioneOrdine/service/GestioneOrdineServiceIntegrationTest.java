package it.unisa.nc26.digitronics.gestioneOrdine.service;

import it.unisa.nc26.digitronics.model.bean.ItemOrdine;
import it.unisa.nc26.digitronics.model.bean.MetodoSpedizione;
import it.unisa.nc26.digitronics.model.bean.Ordine;
import it.unisa.nc26.digitronics.model.bean.Prodotto;
import it.unisa.nc26.digitronics.utils.ConPool;
import it.unisa.nc26.digitronics.utils.MyServletException;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

public class GestioneOrdineServiceIntegrationTest {

    private static GestioneOrdineServiceImpl service;

    /**
     * Questo metodo viene eseguito una sola volta prima di tutti i test e
     * inserisce i dati necessari seguendo l’ordine di inserimento specificato.
     */
    @BeforeClass
    public static void globalSetUp() throws SQLException {
        // Configurazione del datasource H2 in memoria
        PoolProperties p = new PoolProperties();
        // Si assicuri che il file database.sql sia coerente con quanto riportato qui
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

        // Inizializza il service
        service = new GestioneOrdineServiceImpl();

        // Inserimenti comuni per tutti i test
        // 1. Inserimento categoria
        try (Connection connection = ConPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "INSERT INTO categoria (nome, descrizione, immagine) VALUES (?, ?, ?)")) {
            ps.setString(1, "categoria");
            ps.setString(2, "categoria descrizione");
            ps.setString(3, "categoria immagine");
            ps.executeUpdate();
        }

        // 2. Inserimento prodotti
        try (Connection connection = ConPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "INSERT INTO prodotto (IdProdotto, nome, prezzo, descrizione, immagine, vetrina, quantità, nomecategoria) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
            ps.setInt(1, 1);
            ps.setString(2, "Prodotto1");
            ps.setDouble(3, 10.50);
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
            ps.setDouble(3, 23.50);
            ps.setString(4, "Prodotto2 descrizione");
            ps.setString(5, "Prodotto2 immagine");
            ps.setBoolean(6, false);
            ps.setInt(7, 100);
            ps.setString(8, "categoria");
            ps.executeUpdate();
        }
        try (Connection connection = ConPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "INSERT INTO prodotto (IdProdotto, nome, prezzo, descrizione, immagine, vetrina, quantità, nomecategoria) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
            ps.setInt(1, 3);
            ps.setString(2, "Prodotto3");
            ps.setDouble(3, 100.50);
            ps.setString(4, "Prodotto3 descrizione");
            ps.setString(5, "Prodotto3 immagine");
            ps.setBoolean(6, false);
            ps.setInt(7, 100);
            ps.setString(8, "categoria");
            ps.executeUpdate();
        }

        // 3. Inserimento utenti
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
        try (Connection connection = ConPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "INSERT INTO metodospedizione (nome, descrizione, costo) VALUES (?, ?, ?)")) {
            ps.setString(1, "Espress");
            ps.setString(2, "Spedizione Espress");
            ps.setDouble(3, 10.0);
            ps.executeUpdate();
        }

        // 5. Inserimento di alcuni item nel carrello
        try (Connection connection = ConPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "INSERT INTO itemCarrello (IdProdotto, IdUtente, quantità) VALUES (?, ?, ?)")) {
            ps.setInt(1, 2);
            ps.setInt(2, 1);
            ps.setInt(3, 3);
            ps.executeUpdate();
        }
        try (Connection connection = ConPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "INSERT INTO itemCarrello (IdProdotto, IdUtente, quantità) VALUES (?, ?, ?)")) {
            ps.setInt(1, 1);
            ps.setInt(2, 1);
            ps.setInt(3, 5);
            ps.executeUpdate();
        }

        // A questo punto il database è popolato con tutti i dati necessari per i test.
    }

    /**
     * Test per il recupero di un prodotto esistente.
     */
    @Test
    public void testFetchByIdProdottoSuccess() throws MyServletException {
        Prodotto prodotto = service.fetchByIdProdotto(1);
        assertNotNull(prodotto);
        assertEquals("Prodotto1", prodotto.getNome());
    }

    /**
     * Test per il recupero di un prodotto inesistente.
     */
    @Test(expected = MyServletException.class)
    public void testFetchByIdProdottoFail() throws MyServletException {
        service.fetchByIdProdotto(999); // id inesistente
    }

    /**
     * Test per il recupero di tutti i metodi di spedizione.
     */
    @Test
    public void testFetchAllMetodiSpedizione() {
        List<MetodoSpedizione> metodi = service.fetchAllMetodiSpedizione();
        assertNotNull(metodi);
        assertEquals(2, metodi.size());
    }

    /**
     * Test per il recupero di un metodo di spedizione tramite nome.
     */
    @Test
    public void testFetchMetodoSpedizioneByNome() {
        MetodoSpedizione ms = service.fetchMetodoSpedizioneByNome("Standard");
        assertNotNull(ms);
        assertEquals("Standard", ms.getNome());
    }

    /**
     * Test per il salvataggio di un ordine.
     */
    @Test
    public void testSaveOrdineAndFetchOrder() throws MyServletException {
        Ordine ordine = new Ordine();
        ordine.setTotale(10.23);
        ordine.setCap("01245");
        ordine.setNumeroCivico("22");
        ordine.setNome("nome");
        ordine.setCognome("cognome");
        ordine.setVia("dfssdfd");
        ordine.setTelefono("53443545344534");
        ordine.setCittà("città");
        ordine.setNomeMetodoSpedizione("Standard");
        ordine.setDataOrdine(Date.valueOf(LocalDate.now()));
        ordine.setIdUtente(1);  // Utente esistente
        ordine.setDataOrdine(Date.valueOf(LocalDate.now()));
        // inserire eventuali altri campi obbligatori
        int idOrdine = service.saveOrdine(ordine);
        assertTrue(idOrdine > 0);

        // Recupero l'ordine salvato
        Ordine retrieved = service.fetchByIdOrder(idOrdine);
        assertNotNull(retrieved);
        assertEquals(1, retrieved.getIdUtente());
    }

    /**
     * Test per l'aggiornamento della quantità di un prodotto.
     */
    @Test
    public void testUpdateQuantitaProdotto() throws MyServletException {
        Prodotto prodotto = service.fetchByIdProdotto(1);
        int quantitaIniziale = prodotto.getQuantità();
        prodotto.setQuantità(quantitaIniziale - 1);
        service.updateQuantitàProdotto(prodotto);

        Prodotto aggiornato = service.fetchByIdProdotto(1);
        assertEquals(quantitaIniziale - 1, aggiornato.getQuantità());
    }

    /**
     * Test per la rimozione del carrello di un utente.
     * In questo caso si verifica che, dopo la rimozione, la query sui record del carrello
     * restituisca zero risultati.
     */
    @Test
    public void testRimuoviCarrelloServletByIdUtente() throws SQLException {
        // Verifica che esistano degli item prima della rimozione per l'utente 1
        try (Connection connection = ConPool.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) AS cnt FROM itemCarrello WHERE IdUtente=?")) {
            ps.setInt(1, 1);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    assertTrue(rs.getInt("cnt") > 0);
                }
            }
        }
        service.rimuoviCarrelloServletByIdUtente(1);
        // Verifica che il carrello sia stato svuotato
        try (Connection connection = ConPool.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) AS cnt FROM itemCarrello WHERE IdUtente=?")) {
            ps.setInt(1, 1);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    assertEquals(0, rs.getInt("cnt"));
                }
            }
        }
    }

    /**
     * Test per il salvataggio di un item d'ordine.
     */
    @Test
    public void testSaveItemOrdine() throws MyServletException, SQLException {
        // Preliminarmente creo ed eseguo il salvataggio di un ordine per l'utente 1
        Ordine ordine = new Ordine();
        ordine.setTotale(10.23);
        ordine.setCap("01245");
        ordine.setNumeroCivico("22");
        ordine.setNome("nome");
        ordine.setCognome("cognome");
        ordine.setVia("dfssdfd");
        ordine.setTelefono("53443545344534");
        ordine.setCittà("città");
        ordine.setNomeMetodoSpedizione("Standard");
        ordine.setDataOrdine(Date.valueOf(LocalDate.now()));
        ordine.setIdUtente(1);  // Utente esistente
        ordine.setDataOrdine(Date.valueOf(LocalDate.now()));
        int idOrdine = service.saveOrdine(ordine);
        assertTrue(idOrdine > 0);

        // Creo un nuovo item d'ordine e lo salvo
        ItemOrdine item = new ItemOrdine();
        item.setIdOrdine(idOrdine);
        item.setNome("Prodotto1");
        item.setImmagine("Prodotto1 immagine");
        item.setPrezzo(10.50);
        item.setIdProdotto(1);
        item.setQuantità(5);
        service.saveItemOrdine(item);

        // Verifica che l'item sia stato salvato: uso il metodo fetchItemOrder
        List<ItemOrdine> items = service.fetchItemOrder(idOrdine);
        assertNotNull(items);
        assertFalse(items.isEmpty());
        assertEquals(1, items.size());
        assertEquals(5, items.get(0).getQuantità());
    }

    /**
     * Test per la verifica dell'indirizzo.
     * In questo esempio, si assume che il metodo di verifica tramite
     * {@code OpenStreetMapVerificaIndirizzoApiAdapterImpl} ritorni true per input validi.
     */
    @Test
    public void testVerificaIndirizzo() {
        boolean isValid = service.verificaIndirizzo("Piazza Duomo", "84011", "Pogerola");
        assertTrue(isValid);
    }
}

