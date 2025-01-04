package it.unisa.nc26.digitronics.model.dao;

import it.unisa.nc26.digitronics.model.bean.Prodotto;
import it.unisa.nc26.digitronics.model.bean.Recensione;
import it.unisa.nc26.digitronics.utils.ConPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RecensioneDAO {

    public void doSave(Recensione recensione) throws SQLException {
        String sql = "INSERT INTO Recensione (Titolo, Descrizione, Punteggio, idUtente, idProdotto) VALUES(?, ?, ?, ?, ?)";

        try(Connection con = ConPool.getConnection();
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){

            ps.setString(1, recensione.getTitolo());
            ps.setString(2, recensione.getDescrizione());
            ps.setInt(3, recensione.getPunteggio());
            ps.setInt(4, recensione.getIdUtente());
            ps.setInt(5, recensione.getIdProdotto());

            ps.executeUpdate();

            try(ResultSet rs = ps.getGeneratedKeys()){
                if(rs.next()){
                    int id = rs.getInt(1);
                    recensione.setIdRecensione(id);
                }
            }
        }
    }

    public Collection<Recensione> doRetrieveByProduct(int product) throws SQLException{
        String sql = "SELECT * FROM Recensione WHERE idProdotto = ?";
        List<Recensione> recensioni = new ArrayList<>();

        try(Connection con = ConPool.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setInt(1, product);

            try(ResultSet rs = ps.executeQuery()){

                while (rs.next()){
                    Recensione recensione = new Recensione();
                    recensione.setIdRecensione(rs.getInt("idRecensione"));
                    recensione.setTitolo(rs.getString("Titolo"));
                    recensione.setDescrizione(rs.getString("Descrizione"));
                    recensione.setPunteggio(rs.getInt("Punteggio"));
                    recensione.setIdUtente(rs.getInt("idUtente"));
                    recensione.setIdProdotto(rs.getInt("idProdotto"));
                    recensione.setUtente(new UtenteDAO().doRetrieveById(recensione.getIdUtente()));
                    recensioni.add(recensione);
                }
            }
        }
        return recensioni;
    }

    public Collection<Recensione> doRetrieveByUtente(int idUtente) throws SQLException{
        String sql = "SELECT * FROM Recensione WHERE idUtente = ?";
        List<Recensione> recensioni = new ArrayList<>();

        try(Connection con = ConPool.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setInt(1, idUtente);

            try(ResultSet rs = ps.executeQuery()){

                while (rs.next()){
                    Recensione recensione = new Recensione();
                    recensione.setIdRecensione(rs.getInt("idRecensione"));
                    recensione.setTitolo(rs.getString("Titolo"));
                    recensione.setDescrizione(rs.getString("Descrizione"));
                    recensione.setPunteggio(rs.getInt("Punteggio"));
                    recensione.setIdUtente(rs.getInt("idUtente"));
                    recensione.setIdProdotto(rs.getInt("idProdotto"));
                    recensioni.add(recensione);
                }
            }
        }
        return recensioni;
    }

    public Double doRetrieveAveragePunteggio(int idProdotto) throws SQLException{

        String sql = "SELECT AVG(punteggio) AS Media FROM Recensione WHERE idProdotto=?";
        double result = 0;

        try(Connection con = ConPool.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setInt(1, idProdotto);

            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    result = rs.getDouble("Media");
                }
            }
        }
        return result;
    }



}


