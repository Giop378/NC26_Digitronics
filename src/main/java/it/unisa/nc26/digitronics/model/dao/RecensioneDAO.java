package it.unisa.nc26.digitronics.model.dao;

import it.unisa.nc26.digitronics.model.bean.Prodotto;
import it.unisa.nc26.digitronics.model.bean.Recensione;
import it.unisa.nc26.digitronics.utils.ConPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * DAO per gestire le operazioni sulle recensioni.
 * Contiene metodi per salvare, recuperare e calcolare statistiche sulle recensioni nel database.
 */
public class RecensioneDAO {


    /**
     * Salva una nuova recensione nel database.
     *
     * @param recensione Oggetto {@link Recensione} contenente i dettagli della recensione da salvare.
     *                   L'ID generato automaticamente viene impostato nell'oggetto fornito.
     * @throws SQLException Se si verifica un errore durante l'operazione sul database.
     */
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

    /**
     * Recupera tutte le recensioni associate a un prodotto specifico.
     *
     * @param product ID del prodotto di cui recuperare le recensioni.
     * @return Una collezione di oggetti {@link Recensione} associati al prodotto specificato.
     * @throws SQLException Se si verifica un errore durante l'operazione sul database.
     */
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

    /**
     * Recupera tutte le recensioni effettuate da un utente specifico.
     *
     * @param idUtente ID dell'utente di cui recuperare le recensioni.
     * @return Una collezione di oggetti {@link Recensione} effettuati dall'utente specificato.
     * @throws SQLException Se si verifica un errore durante l'operazione sul database.
     */
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

    /**
     * Calcola il punteggio medio delle recensioni per un prodotto specifico.
     *
     * @param idProdotto ID del prodotto di cui calcolare il punteggio medio.
     * @return Il punteggio medio come valore {@code Double}.
     *         Restituisce 0.0 se non ci sono recensioni per il prodotto.
     * @throws SQLException Se si verifica un errore durante l'operazione sul database.
     */
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


