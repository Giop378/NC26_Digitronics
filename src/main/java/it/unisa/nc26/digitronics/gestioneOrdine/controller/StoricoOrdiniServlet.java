package it.unisa.nc26.digitronics.gestioneOrdine.controller;

import it.unisa.nc26.digitronics.gestioneOrdine.service.GestioneOrdineService;
import it.unisa.nc26.digitronics.gestioneOrdine.service.GestioneOrdineServiceImpl;
import it.unisa.nc26.digitronics.model.bean.Ordine;
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
import java.util.List;

/**
 * StoricoOrdiniServlet è una servlet che gestisce la visualizzazione dello storico degli ordini
 * per un utente specifico o per tutti gli ordini nel caso di un amministratore.
 * Gli utenti normali possono visualizzare solo i propri ordini, mentre gli amministratori
 * possono accedere a tutti gli ordini nel sistema.
 */
@WebServlet("/storicoOrdini")
public class StoricoOrdiniServlet extends HttpServlet {

    private GestioneOrdineService gestioneOrdineService;

    /**
     * Costruttore della servlet che inizializza il servizio di gestione degli ordini.
     */
    public StoricoOrdiniServlet() {
        this.gestioneOrdineService = new GestioneOrdineServiceImpl();
    }

    /**
     * Imposta un'implementazione personalizzata del servizio di gestione degli ordini.
     *
     * @param gestioneOrdineService il servizio di gestione degli ordini
     */
    public void setOrderService(GestioneOrdineService gestioneOrdineService) {
        this.gestioneOrdineService = gestioneOrdineService;
    }

    /**
     * Gestisce le richieste HTTP GET.
     * Recupera lo storico degli ordini in base al ruolo dell'utente:
     * gli utenti normali vedono solo i propri ordini, mentre gli amministratori vedono tutti gli ordini.
     *
     * @param request  l'oggetto HttpServletRequest che contiene la richiesta del client
     * @param response l'oggetto HttpServletResponse che contiene la risposta al client
     * @throws ServletException in caso di errore durante la gestione della richiesta
     * @throws IOException      in caso di errore di input/output
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Utente utente = (Utente) session.getAttribute("utente");

        if (utente == null) {
            throw new MyServletException("Non puoi accedere a questa pagina. Accedi per continuare");
        }
        if (!utente.isRuolo()) {
            // Recupera gli ordini dell'utente corrente
            List<Ordine> ordini = gestioneOrdineService.fetchByIdUtente(utente.getIdUtente());
            request.setAttribute("ordini", ordini);
        } else {
            // Recupera tutti gli ordini per l'amministratore
            List<Ordine> ordini = gestioneOrdineService.fetchAllOrders();
            request.setAttribute("ordini", ordini);
        }
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/results/storicoOrdini.jsp");
        requestDispatcher.forward(request, response);
    }

    /**
     * Gestisce le richieste HTTP POST.
     * Reindirizza le richieste POST al metodo doGet.
     *
     * @param request  l'oggetto HttpServletRequest che contiene la richiesta del client
     * @param response l'oggetto HttpServletResponse che contiene la risposta al client
     * @throws ServletException in caso di errore durante la gestione della richiesta
     * @throws IOException      in caso di errore di input/output
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
