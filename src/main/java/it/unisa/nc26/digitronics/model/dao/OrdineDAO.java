package it.unisa.nc26.digitronics.model.dao;

import it.unisa.nc26.digitronics.model.bean.Ordine;
import it.unisa.nc26.digitronics.utils.ConPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrdineDAO {
    // Recupera una lista di tutti gli ordini dal database
    public List<Ordine> doRetrieveAll() {


        try (Connection con = ConPool.getConnection()) {



            PreparedStatement ps = con.prepareStatement("select IdOrdine,totale,Idutente,cap,numerocivico,nome,cognome,via,telefono,nomemetodospedizione,dataordine, città from ordine");
            ResultSet rs = ps.executeQuery();

            List<Ordine>  ordini = new ArrayList<>();

            while (rs.next()) {

                Ordine c = new Ordine();

                c.setIdOrdine(rs.getInt(1));
                c.setTotale(rs.getDouble(2));
                c.setIdUtente(rs.getInt(3));
                c.setCap(rs.getString(4));
                c.setNumeroCivico(rs.getString(5));
                c.setNome(rs.getString(6));
                c.setCognome(rs.getString(7));
                c.setVia(rs.getString((8)));
                c.setTelefono(rs.getString(9));
                c.setNomeMetodoSpedizione(rs.getString(10));
                c.setDataOrdine(rs.getDate(11));
                c.setCittà(rs.getString(12));

                ordini.add(c);
            }

            return ordini;

        } catch (SQLException e) {

            throw new RuntimeException("Errore durante l'accesso agli ordini, riprova più tardi");
        }
    }

    // Dato un id recupera un ordine
    public Ordine doRetrieveByIdOrder(int id) {
        try (Connection con = ConPool.getConnection()) {

            PreparedStatement ps = con.prepareStatement(
                    "SELECT IdOrdine, totale, IdUtente, cap, numerocivico, nome, cognome, via, telefono, nomemetodospedizione, dataordine, città " +
                            "FROM ordine " +
                            "WHERE IdOrdine = ?");

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();


            if(rs.next()) {
                Ordine o = new Ordine();
                o.setIdOrdine(rs.getInt(1));
                o.setTotale(rs.getDouble(2));
                o.setIdUtente(rs.getInt(3));
                o.setCap(rs.getString(4));
                o.setNumeroCivico(rs.getString(5));
                o.setNome(rs.getString(6));
                o.setCognome(rs.getString(7));
                o.setVia(rs.getString(8));
                o.setTelefono(rs.getString(9));
                o.setNomeMetodoSpedizione(rs.getString(10));
                o.setDataOrdine(rs.getDate(11));
                o.setCittà(rs.getString(12));

                return o;
            }

            return null;

        } catch (SQLException s) {
            throw new RuntimeException("Errore durante l'accesso ad un ordine con ID specifico, riprova più tardi");
        }
    }

    // Recupera una lista di ordini dal database filtrati per ID utente
    public List<Ordine> doRetrieveByCustomer(int idUtente) {
        try (Connection con = ConPool.getConnection()) {

            PreparedStatement ps = con.prepareStatement(
                    "SELECT IdOrdine, totale, IdUtente, cap, numerocivico, nome, cognome, via, telefono, nomemetodospedizione, dataordine, città " +
                            "FROM ordine " +
                            "WHERE IdUtente = ?");

            ps.setInt(1, idUtente);
            ResultSet rs = ps.executeQuery();
            List<Ordine> ordini = new ArrayList<>();

            while (rs.next()) {
                Ordine o = new Ordine();
                o.setIdOrdine(rs.getInt(1));
                o.setTotale(rs.getDouble(2));
                o.setIdUtente(rs.getInt(3));
                o.setCap(rs.getString(4));
                o.setNumeroCivico(rs.getString(5));
                o.setNome(rs.getString(6));
                o.setCognome(rs.getString(7));
                o.setVia(rs.getString(8));
                o.setTelefono(rs.getString(9));
                o.setNomeMetodoSpedizione(rs.getString(10));
                o.setDataOrdine(rs.getDate(11));
                o.setCittà(rs.getString(12));

                ordini.add(o);
            }

            return ordini;

        } catch (SQLException s) {
            throw new RuntimeException("Errore durante l'accesso agli ordini di un utente, riprova più tardi");
        }
    }

    // Salva un nuovo ordine nel database e restituisce l'ID generato
    public synchronized int doSave(Ordine ordine) {
        try (Connection con = ConPool.getConnection()) {
            String sql = "INSERT INTO ordine (totale, IdUtente, cap, numerocivico, nome, cognome, via, telefono,nomemetodospedizione, dataordine, città ) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setDouble(1, ordine.getTotale());
            ps.setInt(2, ordine.getIdUtente());
            ps.setString(3, ordine.getCap());
            ps.setString(4, ordine.getNumeroCivico());
            ps.setString(5, ordine.getNome());
            ps.setString(6, ordine.getCognome());
            ps.setString(7, ordine.getVia());
            ps.setString(8, ordine.getTelefono());
            ps.setString(9, ordine.getNomeMetodoSpedizione());
            ps.setDate(10, ordine.getDataOrdine());
            ps.setString(11, ordine.getCittà());

            if (ps.executeUpdate() != 1) {
                throw new RuntimeException("INSERT error.");
            }

            // Recupera l'ID generato
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new RuntimeException("Non è stato possibile recuperare dal database l'id generato");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Errore durante il salvataggio dell'ordine, riprova più tardi");
        }
    }
}
