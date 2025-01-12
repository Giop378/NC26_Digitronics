package it.unisa.nc26.digitronics.model.dao;

import it.unisa.nc26.digitronics.model.bean.MetodoSpedizione;
import it.unisa.nc26.digitronics.utils.ConPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe Data Access Object (DAO) per l'entità {@link MetodoSpedizione}.
 *
 * <p>La classe {@code MetodoSpedizioneDAO} fornisce metodi per interagire con il database
 * e gestire le operazioni CRUD (Create, Read, Update, Delete) per l'entità {@link MetodoSpedizione}.
 * Le operazioni implementate includono il recupero di tutti i metodi di spedizione e il recupero
 * di un metodo di spedizione specifico dato il nome.</p>
 */
public class MetodoSpedizioneDAO {

    /**
     * Recupera una lista di tutti i metodi di spedizione dal database.
     *
     * @return una lista di {@link MetodoSpedizione} contenente tutti i metodi di spedizione nel database
     * @throws RuntimeException se si verifica un errore durante l'accesso al database
     */
    public List<MetodoSpedizione> doRetrieveAll() {

        try (Connection con = ConPool.getConnection()) {

            PreparedStatement ps = con.prepareStatement("select nome,descrizione,costo from metodospedizione");
            ResultSet rs = ps.executeQuery();

            List<MetodoSpedizione> metodiSpedizioni = new ArrayList<>();

            while (rs.next()) {

                MetodoSpedizione c = new MetodoSpedizione();

                c.setNome(rs.getString(1));
                c.setDescrizione(rs.getString(2));
                c.setCosto(rs.getDouble(3));

                metodiSpedizioni.add(c);
            }

            return metodiSpedizioni;

        } catch (SQLException e) {

            throw new RuntimeException("Errore durante l'accesso ai metodi di spedizione, riprova più tardi");
        }
    }

    /**
     * Recupera un metodo di spedizione dal database dato il nome del metodo di spedizione.
     *
     * @param nomeMetodo il nome del metodo di spedizione da cercare
     * @return un oggetto {@link MetodoSpedizione} corrispondente al nome, o {@code null} se il metodo di spedizione non esiste
     * @throws RuntimeException se si verifica un errore durante l'accesso al database
     */
    public MetodoSpedizione doRetrieveByName(String nomeMetodo) {
        MetodoSpedizione metodoSpedizione = null;

        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT nome, descrizione, costo FROM metodospedizione WHERE nome = ?");
            ps.setString(1, nomeMetodo);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                metodoSpedizione = new MetodoSpedizione();
                metodoSpedizione.setNome(rs.getString("nome"));
                metodoSpedizione.setDescrizione(rs.getString("descrizione"));
                metodoSpedizione.setCosto(rs.getDouble("costo"));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Errore durante il recupero del metodo di spedizione per nome: " + nomeMetodo, e);
        }

        return metodoSpedizione;
    }
}
