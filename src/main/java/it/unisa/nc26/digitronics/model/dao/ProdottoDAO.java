package it.unisa.nc26.digitronics.model.dao;

import it.unisa.nc26.digitronics.model.bean.Prodotto;
import it.unisa.nc26.digitronics.utils.ConPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProdottoDAO {
    public List<Prodotto> doRetrieveAll() {

        try (Connection con = ConPool.getConnection()) {

            PreparedStatement ps = con.prepareStatement("select idProdotto, nome, prezzo, descrizione, immagine, vetrina, quantità, nomecategoria from prodotto");
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
    //Restituisce tutti i prodotti di una determinata categoria
    public List<Prodotto> doRetrieveByCategory(String nomeCategoria){
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement("select idProdotto, nome, prezzo, descrizione, immagine, vetrina, quantità, nomecategoria from prodotto where nomecategoria=?");
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
    //restituisce gli elementi della vetrina
    public List<Prodotto> doRetrieveVetrina(){
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT idProdotto, nome, prezzo, descrizione, immagine, vetrina, quantità, nomecategoria FROM prodotto WHERE vetrina = true ORDER BY RAND() LIMIT 8");
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
    //Prende i dati di un singolo prodotto dal database
    public Prodotto doRetrieveById(int idProdotto){
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement("select idProdotto, nome, prezzo, descrizione, immagine, vetrina, quantità, nomecategoria from prodotto where idProdotto=?");
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
    //Salva un prodotto nel database
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
    //Aggiorna un prodotto nel database
    public synchronized void doUpdate(Prodotto prodotto){
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement("UPDATE Prodotto SET nome=?, prezzo=?, descrizione=?, immagine=?, vetrina=?, quantità=?, nomecategoria=? WHERE IdProdotto=?");
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
    public List<Prodotto> doRetrieveByName(String nomeProdotto) {
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement("select idProdotto, nome, prezzo, descrizione, immagine, vetrina, quantità, nomecategoria from prodotto where nome LIKE ?");
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

    public synchronized void doDelete(int idProdotto) {
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement("DELETE FROM prodotto WHERE idProdotto = ?");
            ps.setInt(1, idProdotto);

            if (ps.executeUpdate() != 1) {
                throw new RuntimeException("DELETE error.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Errore durante la cancellazione del prodotto dal database, riprova più tardi");
        }
    }

}
