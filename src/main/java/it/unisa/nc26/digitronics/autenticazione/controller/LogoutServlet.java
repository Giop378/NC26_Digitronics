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

/**
 * Servlet per gestire la funzionalità di logout degli utenti.
 *
 * <p>Questa servlet è responsabile di:</p>
 * <ul>
 *   <li>Gestire la disconnessione degli utenti registrati.</li>
 *   <li>Salvare lo stato del carrello per utenti non amministratori.</li>
 *   <li>Invalidare la sessione corrente.</li>
 *   <li>Reindirizzare l'utente alla pagina principale.</li>
 * </ul>
 *
 * <p><b>Annotazioni:</b></p>
 * <ul>
 *   <li>{@code @WebServlet}: Definisce il nome e il percorso della servlet.</li>
 * </ul>
 */
@WebServlet(name = "LogoutServlet", value = "/logout")
public class LogoutServlet extends HttpServlet {
    /**
     * Servizio di autenticazione utilizzato per la gestione del carrello e altre funzionalità.
     */
    private AutenticazioneService autenticazioneService;

    /**
     * Costruttore della servlet. Inizializza il servizio di autenticazione con l'implementazione predefinita.
     */
    public LogoutServlet() {
        this.autenticazioneService = new AutenticazioneServiceImpl();
    }

    /**
     * Metodo per impostare un'istanza personalizzata del servizio di autenticazione.
     *
     * <p>Questo metodo è utile per l'iniezione di dipendenze nei test.</p>
     *
     * @param autenticazioneService il servizio di autenticazione da utilizzare.
     */
    public void setAutenticazioneService(AutenticazioneService autenticazioneService) {
        this.autenticazioneService = autenticazioneService;
    }
    /**
     * Gestisce le richieste HTTP GET per la disconnessione dell'utente.
     *
     * <p><b>Logica di funzionamento:</b></p>
     * <ul>
     *   <li>Verifica se l'utente è registrato; in caso contrario, lancia un'eccezione.</li>
     *   <li>Per utenti non amministratori:
     *     <ul>
     *       <li>Elimina il carrello dell'utente dal database.</li>
     *       <li>Salva lo stato corrente del carrello della sessione nel database.</li>
     *     </ul>
     *   </li>
     *   <li>Invalida la sessione corrente.</li>
     *   <li>Reindirizza l'utente alla pagina principale.</li>
     * </ul>
     *
     * @param request  l'oggetto {@link HttpServletRequest} contenente i dati della richiesta.
     * @param response l'oggetto {@link HttpServletResponse} per inviare la risposta al client.
     * @throws ServletException se si verifica un errore nella servlet.
     * @throws IOException se si verifica un errore di I/O.
     * @throws MyServletException se l'utente non è registrato.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        List<ItemCarrello> carrelloSession = (List<ItemCarrello>) session.getAttribute("carrello");
        Utente utente = (Utente) session.getAttribute("utente");

        if (utente == null) {
            throw new MyServletException("Utente non registrato: non puoi fare il logout");
        }
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
    }


    /**
     * Gestisce le richieste HTTP POST chiamando il metodo {@link #doGet(HttpServletRequest, HttpServletResponse)}.
     *
     * @param request  l'oggetto {@link HttpServletRequest} contenente i dati della richiesta.
     * @param response l'oggetto {@link HttpServletResponse} per inviare la risposta al client.
     * @throws ServletException se si verifica un errore nella servlet.
     * @throws IOException se si verifica un errore di I/O.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
