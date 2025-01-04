package it.unisa.nc26.digitronics.gestioneRecensione.controller;

import it.unisa.nc26.digitronics.gestioneRecensione.service.RecensioneService;
import it.unisa.nc26.digitronics.gestioneRecensione.service.RecensioneServiceImpl;
import it.unisa.nc26.digitronics.model.bean.Recensione;
import it.unisa.nc26.digitronics.model.bean.Utente;
import it.unisa.nc26.digitronics.utils.HomeServlet;
import it.unisa.nc26.digitronics.utils.MyServletException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/aggiungiRecensione")
public class AggiungiRecensioneServlet extends HomeServlet {

    private RecensioneService recensioneService;

    public void setRecensioneService(RecensioneService recensioneService) {
        this.recensioneService = new RecensioneServiceImpl();
    }

    public AggiungiRecensioneServlet() {
        this.recensioneService = new RecensioneServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Utente utente = (Utente) request.getSession().getAttribute("utente");

        if (utente == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String titolo = request.getParameter("title");
        String descrizione = request.getParameter("description");
        int punteggio = Integer.parseInt(request.getParameter("score"));
        int idUtente = utente.getIdUtente();
        int idProduct = Integer.parseInt(request.getParameter("productId"));

        if(titolo == null || titolo.length() > 255 || descrizione == null || punteggio < 1 || punteggio > 5) {
            throw new MyServletException("Recensione non valida");
        }
        
        Recensione recensione = new Recensione();
        recensione.setTitolo(titolo);
        recensione.setDescrizione(descrizione);
        recensione.setPunteggio(punteggio);
        recensione.setIdUtente(idUtente);
        recensione.setIdProdotto(idProduct);

        try {
            recensioneService.saveRecensione(recensione);
            response.sendRedirect("dettagliProdotto?id=" + idProduct);
        } catch (SQLException e) {
            throw new MyServletException("Errore database:"+e.getMessage());
        }
    }
}
