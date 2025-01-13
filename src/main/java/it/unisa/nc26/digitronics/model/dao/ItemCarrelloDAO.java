package it.unisa.nc26.digitronics.model.dao;

import it.unisa.nc26.digitronics.model.bean.ItemCarrello;
import it.unisa.nc26.digitronics.utils.ConPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe DAO (Data Access Object) per la gestione degli elementi del carrello.
 */
public class ItemCarrelloDAO {

    /**
     * Recupera gli elementi del carrello associati a un determinato utente.
     *
     * @param idUtente l'ID dell'utente
     * @return una lista di {@link ItemCarrello} contenente gli elementi del carrello dell'utente
     */
    public List<ItemCarrello> doRetrieveByIdUtente(int idUtente) {
        List<ItemCarrello> carrelli = new ArrayList<>();
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM itemcarrello WHERE IdUtente = ?"
            );
            ps.setInt(1, idUtente);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ItemCarrello carrello = new ItemCarrello();
                carrello.setIdUtente(rs.getInt("IdUtente"));
                carrello.setIdProdotto(rs.getInt("IdProdotto"));
                carrello.setQuantità(rs.getInt("quantità"));

                // Recupera il prodotto dal database e lo imposta come attributo
                ProdottoDAO prodottoDAO = new ProdottoDAO();
                carrello.setProdotto(prodottoDAO.doRetrieveById(carrello.getIdProdotto()));

                carrelli.add(carrello);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante l'accesso al carrello, riprova più tardi");
        }
        return carrelli;
    }

    /**
     * Salva gli elementi del carrello nel database.
     *
     * @param carrelli la lista di {@link ItemCarrello} da salvare
     */
    public synchronized void doSave(List<ItemCarrello> carrelli) {
        if (carrelli == null || carrelli.isEmpty()) {
            return;
        }
        try (Connection con = ConPool.getConnection()) {
            for (ItemCarrello carrello : carrelli) {
                PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO itemcarrello (IdUtente, IdProdotto, quantità) VALUES(?,?,?)"
                );
                ps.setInt(1, carrello.getIdUtente());
                ps.setInt(2, carrello.getIdProdotto());
                ps.setInt(3, carrello.getQuantità());
                if (ps.executeUpdate() != 1) {
                    throw new RuntimeException("Errore durante l'accesso al carrello, riprova più tardi");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Cancella tutti gli elementi del carrello associati a un determinato utente.
     *
     * @param idUtente l'ID dell'utente
     */
    public synchronized void doDelete(int idUtente) {
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                    "DELETE FROM itemCarrello WHERE IdUtente = ?"
            );
            ps.setInt(1, idUtente);
            int rowsDeleted = ps.executeUpdate();
            System.out.println("Deleted " + rowsDeleted + " rows."); // Opzionale: log per controllo
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante l'accesso al carrello, riprova più tardi");
        }
    }
}