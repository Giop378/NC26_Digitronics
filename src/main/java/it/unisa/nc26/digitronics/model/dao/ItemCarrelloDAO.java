package it.unisa.nc26.digitronics.model.dao;

import it.unisa.nc26.digitronics.model.bean.ItemCarrello;
import it.unisa.nc26.digitronics.utils.ConPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemCarrelloDAO {
    //ritorna i prodotti inseriti nel carrello da quell'utente
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

                //Si prende il prodotto dal database e lo si imposta come attributo
                ProdottoDAO prodottoDAO= new ProdottoDAO();
                carrello.setProdotto(prodottoDAO.doRetrieveById(carrello.getIdProdotto()));

                carrelli.add(carrello);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return carrelli;
    }
    //salva gli elementi del carrello nel database
    public synchronized void doSave(List<ItemCarrello> carrelli) {
        if(carrelli == null || carrelli.isEmpty()){
            return;
        }
        try (Connection con = ConPool.getConnection()) {
            for (ItemCarrello carrello : carrelli) {
                PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO carrello (IdUtente, IdProdotto, quantità) VALUES(?,?,?)"
                );
                ps.setInt(1, carrello.getIdUtente());
                ps.setInt(2, carrello.getIdProdotto());
                ps.setInt(3, carrello.getQuantità());
                if (ps.executeUpdate() != 1) {
                    throw new RuntimeException("INSERT error.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    //cancella tutti i prodotti nel carrello di un utente
    public synchronized void doDelete(int idUtente) {
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                    "DELETE FROM carrello WHERE IdUtente = ?"
            );
            ps.setInt(1, idUtente);
            int rowsDeleted = ps.executeUpdate();
            System.out.println("Deleted " + rowsDeleted + " rows."); // Opzionale: log per controllo
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
