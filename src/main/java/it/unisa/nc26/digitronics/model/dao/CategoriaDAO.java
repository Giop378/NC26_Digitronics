package it.unisa.nc26.digitronics.model.dao;

import it.unisa.nc26.digitronics.model.bean.Categoria;
import it.unisa.nc26.digitronics.utils.ConPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {
    //Prende tutte le categorie
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

            throw new RuntimeException(e);
        }
    }
    //restituisce un oggetto categoria dando il nome
    public Categoria doRetrieveByNomeCategoria(String nomeCategoria){
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
            throw new RuntimeException(e);
        }
    }
    //Salva una categoria
    public void doSave(Categoria categoria){
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement("INSERT INTO utente () VALUES(?,?,?)");
            ps.setString(1, categoria.getNome());
            ps.setString(2, categoria.getDescrizione());
            ps.setString(3, categoria.getImmagine());
            if (ps.executeUpdate() != 1) {
                throw new RuntimeException("INSERT error.");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
