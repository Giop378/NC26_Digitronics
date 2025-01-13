package it.unisa.nc26.digitronics.model.dao;

import it.unisa.nc26.digitronics.model.bean.Prodotto;
import it.unisa.nc26.digitronics.utils.ConPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe Data Access Object (DAO) per gestire le operazioni sui prodotti.
 * Fornisce metodi per recuperare, salvare, aggiornare e cancellare prodotti dal database.
 */
public class ProdottoDAO {

    /**
     * Recupera tutti i prodotti dal database.
     *
     * @return Una lista di oggetti {@link Prodotto} contenenti i dettagli di tutti i prodotti.
     * @throws RuntimeException In caso di errore durante l'accesso al database.
     */
    public List<Prodotto> doRetrieveAll() {

        try (Connection con = ConPool.getConnection()) {

            PreparedStatement ps = con.prepareStatement("select IdProdotto, nome, prezzo, descrizione, immagine, vetrina, quantità, nomecategoria from prodotto");
            ResultSet rs = ps.executeQuery();
            List<Prodotto> prodotti = new ArrayList<>();

            while (rs.next()) {

                Prodotto p = new Prodotto();
                p.setIdProdotto(rs.getInt(1));
                p.setNome(rs.getString(2));
                p.setPrezzo(rs.getDouble(3));
                p.setDescrizione(rs.getString(4));
                p.setImmagine(rs.getString(5));
                p.setVetrina(rs.getBoolean(6));
                p.setQuantità(rs.getInt(7));
                p.setNomeCategoria(rs.getString(8));
                prodotti.add(p);
            }

            return prodotti;

        } catch (SQLException s) {

            throw new RuntimeException("Errore durante il recupero di tutti i prodotti, riprova più tardi");
        }
    }


    /**
     * Recupera tutti i prodotti di una determinata categoria.
     *
     * @param nomeCategoria Il nome della categoria di cui recuperare i prodotti.
     * @return Una lista di oggetti {@link Prodotto} appartenenti alla categoria specificata.
     * @throws RuntimeException In caso di errore durante l'accesso al database.
     */
    public List<Prodotto> doRetrieveByCategory(String nomeCategoria){
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement("select IdProdotto, nome, prezzo, descrizione, immagine, vetrina, quantità, nomecategoria from prodotto where nomecategoria=?");
            ps.setString(1, nomeCategoria);
            ResultSet rs = ps.executeQuery();
            List<Prodotto> prodotti = new ArrayList<>();
            while (rs.next()) {

                Prodotto p = new Prodotto();
                p.setIdProdotto(rs.getInt(1));
                p.setNome(rs.getString(2));
                p.setPrezzo(rs.getDouble(3));
                p.setDescrizione(rs.getString(4));
                p.setImmagine(rs.getString(5));
                p.setVetrina(rs.getBoolean(6));
                p.setQuantità(rs.getInt(7));
                p.setNomeCategoria(rs.getString(8));
                prodotti.add(p);
            }

            return prodotti;

        } catch (SQLException s) {

            throw new RuntimeException("Errore durante l'accesso dei prodotti di una determinata categoria, riprova più tardi");
        }
    }

    /**
     * Recupera i prodotti che fanno parte della vetrina.
     *
     * @return Una lista di oggetti {@link Prodotto} selezionati per la vetrina.
     * @throws RuntimeException In caso di errore durante l'accesso al database.
     */
    public List<Prodotto> doRetrieveVetrina(){
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT IdProdotto, nome, prezzo, descrizione, immagine, vetrina, quantità, nomecategoria FROM prodotto WHERE vetrina = true ORDER BY RAND() LIMIT 8");
            ResultSet rs = ps.executeQuery();
            List<Prodotto> prodotti = new ArrayList<>();

            while (rs.next()) {

                Prodotto p = new Prodotto();
                p.setIdProdotto(rs.getInt(1));
                p.setNome(rs.getString(2));
                p.setPrezzo(rs.getDouble(3));
                p.setDescrizione(rs.getString(4));
                p.setImmagine(rs.getString(5));
                p.setVetrina(rs.getBoolean(6));
                p.setQuantità(rs.getInt(7));
                p.setNomeCategoria(rs.getString(8));
                prodotti.add(p);
            }

            return prodotti;

        } catch (SQLException s) {

            throw new RuntimeException("Errore durante il recupero dei prodotti della home, riprova più tardi");
        }
    }

    /**
     * Recupera i dettagli di un prodotto dato il suo ID.
     *
     * @param idProdotto L'ID del prodotto da recuperare.
     * @return Un oggetto {@link Prodotto} contenente i dettagli del prodotto, oppure {@code null} se il prodotto non esiste.
     * @throws RuntimeException In caso di errore durante l'accesso al database.
     */
    public Prodotto doRetrieveById(int idProdotto){
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement("select IdProdotto, nome, prezzo, descrizione, immagine, vetrina, quantità, nomecategoria from prodotto where IdProdotto=?");
            ps.setInt(1, idProdotto);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                Prodotto p = new Prodotto();
                p.setIdProdotto(rs.getInt(1));
                p.setNome(rs.getString(2));
                p.setPrezzo(rs.getDouble(3));
                p.setDescrizione(rs.getString(4));
                p.setImmagine(rs.getString(5));
                p.setVetrina(rs.getBoolean(6));
                p.setQuantità(rs.getInt(7));
                p.setNomeCategoria(rs.getString(8));
                return p;
            }
            return null;

        } catch (SQLException s) {

            throw new RuntimeException("Errore durante il recupero di un prodotto tramite id, riprova più tardi");
        }
    }

    /**
     * Salva un prodotto nel database.
     *
     * @param prodotto L'oggetto {@link Prodotto} da salvare.
     * @throws RuntimeException Se il salvataggio fallisce.
     */
    public synchronized void doSave(Prodotto prodotto){
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement("INSERT INTO prodotto (nome, prezzo, descrizione, immagine, vetrina, quantità, nomecategoria) VALUES(?,?,?,?,?,?,?)");
            ps.setString(1, prodotto.getNome());
            ps.setDouble(2, prodotto.getPrezzo());
            ps.setString(3, prodotto.getDescrizione());
            ps.setString(4, prodotto.getImmagine());
            ps.setBoolean(5, prodotto.isVetrina());
            ps.setInt(6, prodotto.getQuantità());
            ps.setString(7, prodotto.getNomeCategoria());

            if (ps.executeUpdate() != 1) {
                throw new RuntimeException("Errore nel salvataggio del prodotto");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Errore durante il salvataggio del prodotto nel database, riprova più tardi");
        }
    }

    /**
     * Aggiorna un prodotto esistente nel database.
     *
     * @param prodotto L'oggetto {@link Prodotto} contenente i dettagli aggiornati.
     * @throws RuntimeException In caso di errore durante l'aggiornamento.
     */
    public synchronized void doUpdate(Prodotto prodotto){
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement("UPDATE prodotto SET nome=?, prezzo=?, descrizione=?, immagine=?, vetrina=?, quantità=?, nomecategoria=? WHERE IdProdotto=?");
            ps.setString(1, prodotto.getNome());
            ps.setDouble(2, prodotto.getPrezzo());
            ps.setString(3, prodotto.getDescrizione());
            ps.setString(4, prodotto.getImmagine());
            ps.setBoolean(5, prodotto.isVetrina());
            ps.setInt(6, prodotto.getQuantità());
            ps.setString(7, prodotto.getNomeCategoria());
            ps.setInt(8, prodotto.getIdProdotto());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante la modifica del prodotto nel database, riprova più tardi");
        }
    }

    /**
     * Recupera tutti i prodotti il cui nome contiene una specifica stringa.
     *
     * @param nomeProdotto La stringa da cercare nel nome dei prodotti.
     * @return Una lista di oggetti {@link Prodotto} che corrispondono al criterio di ricerca.
     * @throws RuntimeException In caso di errore durante il recupero dei dati.
     */
    public List<Prodotto> doRetrieveByName(String nomeProdotto) {
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement("select IdProdotto, nome, prezzo, descrizione, immagine, vetrina, quantità, nomecategoria from prodotto where nome LIKE ?");
            ps.setString(1, "%" + nomeProdotto + "%");
            ResultSet rs = ps.executeQuery();
            List<Prodotto> prodotti = new ArrayList<>();

            while (rs.next()) {

                Prodotto p = new Prodotto();
                p.setIdProdotto(rs.getInt(1));
                p.setNome(rs.getString(2));
                p.setPrezzo(rs.getDouble(3));
                p.setDescrizione(rs.getString(4));
                p.setImmagine(rs.getString(5));
                p.setVetrina(rs.getBoolean(6));
                p.setQuantità(rs.getInt(7));
                p.setNomeCategoria(rs.getString(8));
                prodotti.add(p);
            }

            return prodotti;

        } catch (SQLException s) {

            throw new RuntimeException("Errore durante il recupero del prodotto dal database tramite nome, riprova più tardi");
        }
    }

    /**
     * Cancella un prodotto dal database dato il suo ID.
     *
     * @param idProdotto L'ID del prodotto da eliminare.
     * @throws RuntimeException In caso di errore durante la cancellazione.
     */
    public synchronized void doDelete(int idProdotto) {
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement("DELETE FROM prodotto WHERE IdProdotto = ?");
            ps.setInt(1, idProdotto);

            if (ps.executeUpdate() != 1) {
                throw new RuntimeException("DELETE error.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Errore durante la cancellazione del prodotto dal database, riprova più tardi");
        }
    }

}
