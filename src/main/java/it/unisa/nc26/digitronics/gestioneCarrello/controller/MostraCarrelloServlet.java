package it.unisa.nc26.digitronics.gestioneCarrello.controller;

import it.unisa.nc26.digitronics.gestioneCarrello.service.GestioneCarrelloService;
import it.unisa.nc26.digitronics.gestioneCarrello.service.GestioneCarrelloServiceImpl;
import it.unisa.nc26.digitronics.model.bean.ItemCarrello;
import it.unisa.nc26.digitronics.model.bean.Prodotto;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet che gestisce la visualizzazione del carrello.
 * La servlet è mappata all'URL "/mostra-carrello".
 */
@WebServlet("/mostra-carrello")
public class MostraCarrelloServlet extends HttpServlet {
    private GestioneCarrelloService gestioneCarrelloService;

    /**
     * Costruttore della servlet.
     * Inizializza il servizio di gestione del carrello con un'implementazione predefinita.
     */
    public MostraCarrelloServlet() {
        this.gestioneCarrelloService = new GestioneCarrelloServiceImpl();
    }

    /**
     * Metodo setter per iniettare una specifica implementazione del servizio di gestione del carrello.
     *
     * @param gestioneCarrelloService l'implementazione del servizio di gestione del carrello
     */
    public void setGestioneCarrelloService(GestioneCarrelloService gestioneCarrelloService) {
        this.gestioneCarrelloService = gestioneCarrelloService;
    }

    /**
     * Gestisce le richieste GET per visualizzare il carrello.
     * Controlla che l'utente non sia un admin, inizializza il carrello se necessario
     * e verifica che le quantità dei prodotti nel carrello siano disponibili in magazzino.
     *
     * @param request  l'oggetto HttpServletRequest contenente i dati della richiesta
     * @param response l'oggetto HttpServletResponse per inviare la risposta
     * @throws ServletException se si verifica un errore nella servlet
     * @throws IOException      se si verifica un errore di I/O
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Utente utente = (Utente) session.getAttribute("utente");

        if (utente != null) {
            if (utente.isRuolo()) {
                throw new MyServletException("L'admin non può interagire con il carrello");
            }
        }

        List<ItemCarrello> carrello = (List<ItemCarrello>) session.getAttribute("carrello");
        if (carrello==null) {
            carrello = new ArrayList<ItemCarrello>();
            session.setAttribute("carrello", carrello);
        } else {
            for (ItemCarrello c : carrello) {
                int idProdotto = c.getIdProdotto();
                //devo controllare se la quantità inserita nel carrello dall'utente è effettivamente presente
                int quantitàNelCarrello= c.getQuantità();
                Prodotto prodotto = gestioneCarrelloService.fetchByIdProdotto(idProdotto);

                // Verifica che la quantità nel carrello non superi la disponibilità in magazzino
                if (quantitàNelCarrello > prodotto.getQuantità()) {
                    c.setQuantità(prodotto.getQuantità());
                }
            }
            session.setAttribute("carrello", carrello);
        }

        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/results/cart.jsp");
        requestDispatcher.forward(request, response);
    }

    /**
     * Gestisce le richieste POST per visualizzare il carrello.
     * Reindirizza la richiesta al metodo doGet per elaborazione.
     *
     * @param request  l'oggetto HttpServletRequest contenente i dati della richiesta
     * @param response l'oggetto HttpServletResponse per inviare la risposta
     * @throws ServletException se si verifica un errore nella servlet
     * @throws IOException      se si verifica un errore di I/O
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}

