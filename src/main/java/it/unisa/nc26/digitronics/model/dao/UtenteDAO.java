package it.unisa.nc26.digitronics.model.dao;

import it.unisa.nc26.digitronics.model.bean.Utente;
import it.unisa.nc26.digitronics.utils.ConPool;

import java.sql.*;

public class UtenteDAO {

    public Utente doRetrieveByEmailPassword(String email, String passwordhash) {

        PreparedStatement statement = null;
        ResultSet rs = null;
        Utente utente = null;

        try (Connection connection = ConPool.getConnection()) {
            statement = connection.prepareStatement("SELECT idUtente, nome, cognome, email, passwordhash, datadinascita, ruolo FROM utente WHERE email=? AND passwordhash=SHA1(?)");
            statement.setString(1, email);
            statement.setString(2, passwordhash);
            rs = statement.executeQuery();

            if (rs.next()) {
                utente = new Utente();
                utente.setIdUtente(rs.getInt("idutente"));
                utente.setNome(rs.getString("nome"));
                utente.setCognome(rs.getString("cognome"));
                utente.setEmail(rs.getString("email"));
                utente.setPasswordHash(passwordhash);
                utente.setDataDiNascita(rs.getDate("datadinascita").toLocalDate());
                utente.setRuolo(rs.getBoolean("ruolo"));
            }

        }  catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (rs!= null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException("Errore del database durante l'accesso, riprova più tardi");
            }
        }

        return utente;
    }

    public synchronized int doSave(Utente utente) {
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO utente (nome, cognome, email, passwordhash, datadinascita, ruolo) VALUES(?,?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, utente.getNome());
            ps.setString(2, utente.getCognome());
            ps.setString(3, utente.getEmail());
            ps.setString(4, utente.getPasswordHash());
            ps.setDate(5, java.sql.Date.valueOf(utente.getDataDiNascita()));
            ps.setBoolean(6, utente.isRuolo());

            if (ps.executeUpdate() != 1) {
                throw new RuntimeException("INSERT error.");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new RuntimeException("Non è stato possibile recuperare l'id utente generato dal database, riprova più tardi");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Salvataggio dell'utente nel database non riuscito, riprova più tardi");
        }
    }

    public Utente doRetrieveById(int idUtente) {

        PreparedStatement statement = null;
        ResultSet rs = null;
        Utente utente = null;

        try (Connection connection = ConPool.getConnection()) {
            statement = connection.prepareStatement("SELECT idUtente, nome, cognome, email, passwordhash, datadinascita, ruolo FROM utente WHERE idUtente=?");
            statement.setString(1, String.valueOf(idUtente));
            rs = statement.executeQuery();

            if (rs.next()) {
                utente = new Utente();
                utente.setIdUtente(rs.getInt("idutente"));
                utente.setNome(rs.getString("nome"));
                utente.setCognome(rs.getString("cognome"));
                utente.setEmail(rs.getString("email"));
                utente.setPasswordHash("passwordhash");
                utente.setDataDiNascita(rs.getDate("datadinascita").toLocalDate());
                utente.setRuolo(rs.getBoolean("ruolo"));
            }

        }  catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (rs!= null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException("Recupero dell'utente dal database non riuscito, riprova più tardi");
            }
        }

        return utente;
    }

}
