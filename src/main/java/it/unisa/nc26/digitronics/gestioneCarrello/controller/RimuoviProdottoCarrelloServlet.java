package it.unisa.nc26.digitronics.gestioneCarrello.controller;

import it.unisa.nc26.digitronics.gestioneCarrello.service.GestioneCarrelloService;
import it.unisa.nc26.digitronics.gestioneCarrello.service.GestioneCarrelloServiceImpl;
import it.unisa.nc26.digitronics.model.bean.ItemCarrello;
import it.unisa.nc26.digitronics.utils.MyServletException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Servlet che gestisce la rimozione di un prodotto dal carrello.
 * La servlet è mappata all'URL "/rimuovi-prodotto-carrello".
 */
@WebServlet("/rimuovi-prodotto-carrello")
public class RimuoviProdottoCarrelloServlet extends HttpServlet {
    private GestioneCarrelloService gestioneCarrelloService;

    /**
     * Costruttore della servlet.
     * Inizializza il servizio di gestione del carrello con un'implementazione predefinita.
     */
    public RimuoviProdottoCarrelloServlet() {
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
     * Gestisce le richieste GET per rimuovere un prodotto dal carrello.
     * Controlla l'esistenza del carrello in sessione, cerca il prodotto tramite l'ID fornito
     * e lo rimuove, se presente.
     *
     * @param request  l'oggetto HttpServletRequest contenente i dati della richiesta
     * @param response l'oggetto HttpServletResponse per inviare la risposta
     * @throws ServletException se si verifica un errore nella servlet
     * @throws IOException      se si verifica un errore di I/O
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        List<ItemCarrello> carrelloSession = (List<ItemCarrello>) session.getAttribute("carrello");

        if (carrelloSession != null) {
            int idProdotto;
            try {
                idProdotto = Integer.parseInt(request.getParameter("idProdotto"));
            } catch (NumberFormatException ex) {
                throw new MyServletException("Errore in idProdotto: non è un numero!!");
            }

            for (int i = 0; i < carrelloSession.size(); i++) {
                if (carrelloSession.get(i).getIdProdotto() == idProdotto) {
                    carrelloSession.remove(i);
                    break; // Assumendo che ogni prodotto sia unico, interrompi il ciclo dopo aver rimosso l'elemento.
                }
            }
            session.setAttribute("carrello", carrelloSession);
        }

        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/results/cart.jsp");
        requestDispatcher.forward(request, response);
    }

    /**
     * Gestisce le richieste POST per rimuovere un prodotto dal carrello.
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
