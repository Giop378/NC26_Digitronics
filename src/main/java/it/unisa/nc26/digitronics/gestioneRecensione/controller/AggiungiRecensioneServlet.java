package it.unisa.nc26.digitronics.gestioneRecensione.controller;

import it.unisa.nc26.digitronics.gestioneRecensione.service.RecensioneService;
import it.unisa.nc26.digitronics.gestioneRecensione.service.RecensioneServiceImpl;
import it.unisa.nc26.digitronics.infoProdotto.service.infoProdottoService;
import it.unisa.nc26.digitronics.infoProdotto.service.infoProdottoServiceImpl;
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
    private infoProdottoService infoProdottoService;

    public void setRecensioneService(RecensioneService recensioneService) {
        this.recensioneService = new RecensioneServiceImpl();
    }

    public void setInfoProdottoService(infoProdottoService infoProdottoService) {
        this.infoProdottoService = infoProdottoService;
    }

    public AggiungiRecensioneServlet() {
        this.recensioneService = new RecensioneServiceImpl();
        this.infoProdottoService = new infoProdottoServiceImpl();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Verifica sessione utente
        Utente utente = (Utente) request.getSession().getAttribute("utente");

        if (utente == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Recupero parametri e validazione input
        String titolo = request.getParameter("title");
        String descrizione = request.getParameter("description");
        String scoreParam = request.getParameter("score");
        String productIdParam = request.getParameter("productId");

        if (titolo == null || titolo.isEmpty() || titolo.length() > 255 ||
                descrizione == null || descrizione.isEmpty() ||
                scoreParam == null || productIdParam == null) {
            throw new MyServletException("Parametri in input non validi");
        }
        
        int punteggio;
        int idProduct;
        try {
            punteggio = Integer.parseInt(scoreParam);
            idProduct = Integer.parseInt(productIdParam);

            if (punteggio < 1 || punteggio > 5) {
                throw new MyServletException("Il punteggio deve essere compreso tra 1 e 5");
            }
        } catch (IllegalArgumentException e) {
            throw new MyServletException("Parametri in input non validi");
        }

        try {
            if(infoProdottoService.fetchByIdProdotto(idProduct) == null) {
                throw new MyServletException("Prodotto non trovato");
            }
        } catch (SQLException e) {
            throw new MyServletException("Errore nel recupero del prodotto");
        }

        // Creazione oggetto Recensione
        Recensione recensione = new Recensione();
        recensione.setTitolo(titolo);
        recensione.setDescrizione(descrizione);
        recensione.setPunteggio(punteggio);
        recensione.setIdUtente(utente.getIdUtente());
        recensione.setIdProdotto(idProduct);

        // Salvataggio della recensione
        try {
            recensioneService.saveRecensione(recensione);
            response.sendRedirect("dettagliProdotto?id=" + idProduct);
        } catch (SQLException e) {
            throw new MyServletException("Errore nel salvataggio della recensione");
        }
    }

}
