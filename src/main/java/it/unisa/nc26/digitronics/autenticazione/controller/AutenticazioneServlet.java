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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servlet per la gestione dell'autenticazione degli utenti.
 */
@WebServlet("/autenticazione-servlet")
public class AutenticazioneServlet extends HttpServlet {
    private AutenticazioneService autenticazioneService;

    /**
     * Costruttore che inizializza il servizio di autenticazione con l'implementazione predefinita.
     */
    public AutenticazioneServlet() {
        this.autenticazioneService = new AutenticazioneServiceImpl();
    }

    /**
     * Imposta un'istanza personalizzata del servizio di autenticazione.
     *
     * @param autenticazioneService l'istanza personalizzata del servizio
     */
    public void setAutenticazioneService(AutenticazioneService autenticazioneService) {
        this.autenticazioneService = autenticazioneService;
    }

    /**
     * Gestisce le richieste HTTP GET.
     *
     * @param request  la richiesta HTTP
     * @param response la risposta HTTP
     * @throws ServletException in caso di errori nella gestione della richiesta
     * @throws IOException      in caso di errori di input/output
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Utente utenteSession = (Utente) request.getSession().getAttribute("utente");
        HttpSession session = request.getSession();
        String action = request.getParameter("action");

        // Se l'utente è già autenticato, redirige alla pagina appropriata
        if (utenteSession != null) {
            if (utenteSession.isRuolo()) {
                RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/results/admin.jsp");
                requestDispatcher.forward(request, response);
            } else {//Nel caso che l'utente ha già fatto l'accesso lo si manda al profilo
                session.setAttribute("utente", utenteSession);
                RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/results/profile.jsp");
                requestDispatcher.forward(request, response);
            }
        }else if("login".equals(action)){//Caso in cui l'utente non ha fatto l'accesso e vuole fare la login
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/results/login.jsp");
            requestDispatcher.forward(request, response);
        }else if("register".equals(action)){//Caso in cui l'utente non ha fatto la registrazione e vuole fare la registrazione
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/results/register.jsp");
            requestDispatcher.forward(request, response);
        }else {
            //Ora ci si occupa dell'accesso vero e proprio
            String email = request.getParameter("email");
            String password = request.getParameter("password");

            if (email == null || password == null) {
                throw new MyServletException("Parametri in input non validi");
            }
            Utente utente = autenticazioneService.retrieveUtenteEmailPassword(email, password);
            request.getSession().setAttribute("utente", utente);

            if (utente == null) {//nel caso provo a fare login ma l'utente non esiste lo si rimanda alla pagina di login perchè l'utente non esiste
                RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/results/login.jsp");
                requestDispatcher.forward(request, response);

            } else if (utente.isRuolo()) {//se l'utente esiste nel database ed è admin deve andare alla pagina admin
                RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/results/admin.jsp");
                requestDispatcher.forward(request, response);

            } else {//se l'utente esiste e non è admin deve andare alla pagina profilo utente
                //questa parte si occupa di prendere i prodotti del carrello da DB e unirli con quelli in sessione
                List<ItemCarrello> carrelloSession = (List<ItemCarrello>) request.getSession().getAttribute("carrello");
                if (carrelloSession == null) {
                    carrelloSession = new ArrayList<ItemCarrello>();
                }
                List<ItemCarrello> carrelloDB = autenticazioneService.retrieveCarrelloUtente(utente.getIdUtente());
                //prima di unire i due carrelli devo impostare l'id utente ai vari prodotti del carrello che al momento sono ancora null perchè sono stati aggiunti prima del login
                for (ItemCarrello carrello : carrelloSession) {
                    carrello.setIdUtente(utente.getIdUtente());
                }
                mergeCarrello(carrelloDB, carrelloSession);
                request.getSession().setAttribute("carrello", carrelloDB);
                RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/results/profile.jsp");
                requestDispatcher.forward(request, response);
            }
        }
    }

    /**
     * Gestisce le richieste HTTP POST delegandole al metodo doGet.
     *
     * @param request  la richiesta HTTP
     * @param response la risposta HTTP
     * @throws ServletException in caso di errori nella gestione della richiesta
     * @throws IOException      in caso di errori di input/output
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    /**
     * Unisce gli elementi del carrello della sessione con quelli del database.
     *
     * @param carrelloDB     il carrello recuperato dal database
     * @param carrelloSession il carrello della sessione
     */
    private void mergeCarrello(List<ItemCarrello> carrelloDB, List<ItemCarrello> carrelloSession) {
        Map<Integer, ItemCarrello> map = new HashMap<>();
        //per evitare di avere errori se una delle due liste è null la inizializzo come lista vuota
        if (carrelloDB == null) {
            carrelloDB = new ArrayList<>();
        }
        if (carrelloSession == null) {
            carrelloSession = new ArrayList<>();
        }

        for (ItemCarrello carrello : carrelloDB) {
            map.put(carrello.getIdProdotto(), carrello);
        }

        for (ItemCarrello carrello : carrelloSession) {
            ItemCarrello existingCarrello = map.get(carrello.getIdProdotto());
            if (existingCarrello == null) {
                carrelloDB.add(carrello);
            } else {
                int totalQuantita = existingCarrello.getQuantità() + carrello.getQuantità();
                existingCarrello.setQuantità(totalQuantita);
            }
        }
    }
}