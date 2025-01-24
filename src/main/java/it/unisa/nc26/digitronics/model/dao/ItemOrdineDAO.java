package it.unisa.nc26.digitronics.model.dao;

import it.unisa.nc26.digitronics.model.bean.ItemOrdine;
import it.unisa.nc26.digitronics.utils.ConPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe DAO (Data Access Object) per la gestione degli oggetti associati agli ordini.
 */
public class ItemOrdineDAO {

    /**
     * Salva un nuovo item ordine nel database.
     *
     * @param itemOrdine l'oggetto {@link ItemOrdine} da salvare
     */
    public synchronized void doSave(ItemOrdine itemOrdine) {
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO itemordine (nome, immagine, prezzo, quantità, IdProdotto, IdOrdine) " +
                            "VALUES (?, ?, ?, ?, ?, ?)");

            ps.setString(1, itemOrdine.getNome());
            ps.setString(2, itemOrdine.getImmagine());
            ps.setDouble(3, itemOrdine.getPrezzo());
            ps.setInt(4, itemOrdine.getQuantità());
            ps.setInt(5, itemOrdine.getIdProdotto());
            ps.setInt(6, itemOrdine.getIdOrdine());

            if (ps.executeUpdate() != 1) {
                throw new RuntimeException("INSERT error.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Errore durante l'accesso agli oggetti dell'ordine, riprova più tardi");
        }
    }

    /**
     * Recupera una lista di oggetti ordine associati a un determinato ID ordine.
     *
     * @param idOrdine l'ID dell'ordine
     * @return una lista di {@link ItemOrdine} associati all'ID ordine specificato
     */
    public List<ItemOrdine> doRetrieveByOrdine(int idOrdine) {
        try (Connection con = ConPool.getConnection()) {

            PreparedStatement ps = con.prepareStatement(
                    "SELECT IdItemOrdine, nome, immagine, prezzo, quantità, IdProdotto, IdOrdine " +
                            "FROM itemordine " +
                            "WHERE IdOrdine = ?");

            ps.setInt(1, idOrdine);
            ResultSet rs = ps.executeQuery();
            List<ItemOrdine> itemOrdini = new ArrayList<>();

            while (rs.next()) {
                ItemOrdine itemOrdine = new ItemOrdine();
                itemOrdine.setIdItemOrdine(rs.getInt(1));
                itemOrdine.setNome(rs.getString(2));
                itemOrdine.setImmagine(rs.getString(3));
                itemOrdine.setPrezzo(rs.getDouble(4));
                itemOrdine.setQuantità(rs.getInt(5));
                itemOrdine.setIdProdotto(rs.getInt(6));
                itemOrdine.setIdOrdine(rs.getInt(7));

                itemOrdini.add(itemOrdine);
            }

            return itemOrdini;

        } catch (SQLException s) {
            throw new RuntimeException("Errore durante l'accesso agli oggetti dell'ordine, riprova più tardi");
        }
    }
}