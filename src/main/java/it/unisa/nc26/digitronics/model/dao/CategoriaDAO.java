package it.unisa.nc26.digitronics.model.dao;

import it.unisa.nc26.digitronics.model.bean.Categoria;
import it.unisa.nc26.digitronics.utils.ConPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe Data Access Object (DAO) per l'entità {@link Categoria}.
 *
 * <p>La classe {@code CategoriaDAO} fornisce metodi per interagire con il database
 * e gestire le operazioni CRUD (Create, Read, Update, Delete) per l'entità {@link Categoria}.
 * Le operazioni implementate includono il recupero di tutte le categorie, il recupero di
 * una categoria per nome e il salvataggio di una nuova categoria nel database.</p>
 */
public class CategoriaDAO {

    /**
     * Recupera tutte le categorie dal database.
     *
     * @return una lista di {@link Categoria} contenente tutte le categorie nel database
     * @throws RuntimeException se si verifica un errore durante l'accesso al database
     */
    public List<Categoria> doRetrieveAll() {
        try (Connection con = ConPool.getConnection()) {

            PreparedStatement ps = con.prepareStatement("select nome, descrizione, immagine from categoria");
            ResultSet rs = ps.executeQuery();

            List<Categoria> categorie = new ArrayList<>();

            while (rs.next()) {

                Categoria c = new Categoria();

                c.setNome(rs.getString(1));
                c.setDescrizione(rs.getString(2));
                c.setImmagine(rs.getString(3));

                categorie.add(c);
            }

            return categorie;

        } catch (SQLException e) {

            throw new RuntimeException("Errore durante l'accesso alle categorie, riprova più tardi");
        }
    }

    /**
     * Recupera una categoria dal database in base al nome della categoria.
     *
     * @param nomeCategoria il nome della categoria da cercare
     * @return un oggetto {@link Categoria} corrispondente al nome, o {@code null} se la categoria non esiste
     * @throws RuntimeException se si verifica un errore durante l'accesso al database
     */
    public Categoria doRetrieveByNomeCategoria(String nomeCategoria) {
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM Categoria WHERE nome = ?");
            ps.setString(1, nomeCategoria);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Categoria c = new Categoria();

                c.setNome(rs.getString(1));
                c.setDescrizione(rs.getString(2));
                c.setImmagine(rs.getString(3));
                return c;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante l'accesso alle categorie, riprova più tardi");
        }
    }

    /**
     * Salva una nuova categoria nel database.
     *
     * @param categoria l'oggetto {@link Categoria} da salvare
     * @throws RuntimeException se si verifica un errore durante l'accesso al database
     */
    public void doSave(Categoria categoria) {
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement("INSERT INTO utente () VALUES(?,?,?)");
            ps.setString(1, categoria.getNome());
            ps.setString(2, categoria.getDescrizione());
            ps.setString(3, categoria.getImmagine());
            if (ps.executeUpdate() != 1) {
                throw new RuntimeException("INSERT error.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Errore durante l'accesso alle categorie, riprova più tardi");
        }
    }
}

