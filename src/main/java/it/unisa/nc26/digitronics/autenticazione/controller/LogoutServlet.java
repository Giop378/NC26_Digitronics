package it.unisa.nc26.digitronics.autenticazione.controller;

import it.unisa.nc26.digitronics.autenticazione.service.AutenticazioneService;
import it.unisa.nc26.digitronics.autenticazione.service.AutenticazioneServiceImpl;
import it.unisa.nc26.digitronics.model.bean.ItemCarrello;
import it.unisa.nc26.digitronics.model.bean.Utente;
import it.unisa.nc26.digitronics.utils.MyServletException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "LogoutServlet", value = "/logout")
public class LogoutServlet extends HttpServlet {

    private AutenticazioneService autenticazioneService;

    public LogoutServlet() {
        this.autenticazioneService = new AutenticazioneServiceImpl();
    }

    public void setAutenticazioneService(AutenticazioneService autenticazioneService) {
        this.autenticazioneService = autenticazioneService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        List<ItemCarrello> carrelloSession = (List<ItemCarrello>) session.getAttribute("carrello");
        Utente utente = (Utente) session.getAttribute("utente");

        if (utente == null) {
            throw new MyServletException("Utente non registrato: non puoi fare il logout");
        }

        try {
            if (!utente.isRuolo()) {
                // Elimina tutti gli elementi del carrello dal database per l'utente corrente
                autenticazioneService.clearCarrelloUtente(utente.getIdUtente());

                // Salva il carrello della sessione nel database
                if (carrelloSession != null && !carrelloSession.isEmpty()) {
                    List<ItemCarrello> itemCarrelloList = new ArrayList<>();
                    for (ItemCarrello carrello : carrelloSession) {
                        ItemCarrello item = new ItemCarrello();
                        item.setIdUtente(utente.getIdUtente());
                        item.setIdProdotto(carrello.getIdProdotto());
                        item.setQuantità(carrello.getQuantità());
                        itemCarrelloList.add(item);
                    }
                    autenticazioneService.saveCarrelloUtente(itemCarrelloList);
                }
            }

            // Invalida la sessione
            session.invalidate();

            // Reindirizza alla pagina principale
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("index.html");
            requestDispatcher.forward(request, response);

        } catch (SQLException e) {
            throw new MyServletException("Errore durante il logout: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
