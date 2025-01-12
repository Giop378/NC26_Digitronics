package it.unisa.nc26.digitronics.gestioneProdotto.controller;

import it.unisa.nc26.digitronics.gestioneProdotto.service.GestioneProdottoService;
import it.unisa.nc26.digitronics.gestioneProdotto.service.GestioneProdottoServiceImpl;
import it.unisa.nc26.digitronics.infoProdotto.service.infoProdottoService;
import it.unisa.nc26.digitronics.infoProdotto.service.infoProdottoServiceImpl;
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

/**
 * Servlet per la rimozione di un prodotto dal sistema.
 * Gestisce la cancellazione di un prodotto esistente nel database
 * tramite il servizio {@link GestioneProdottoService}.
 *
 * <p>La servlet permette agli utenti con ruolo di amministratore di eliminare un prodotto
 * esistente, previa verifica dell'esistenza del prodotto nel sistema.</p>
 *
 * <p>In caso di successo, il prodotto viene rimosso dal database e l'utente viene reindirizzato a una
 * pagina di conferma. Se si verifica un errore durante l'eliminazione o se il prodotto non esiste,
 * viene generato un messaggio di errore.</p>
 *
 * @see GestioneProdottoService
 * @see infoProdottoService
 * @see MyServletException
 */
@WebServlet(name = "DeleteProductServlet", value = "/delete-product-servlet")
public class RimuoviProdottoServlet extends HttpServlet {

    /**
     * Servizio di gestione dei prodotti che si occupa della logica di eliminazione nel database.
     */
    private GestioneProdottoService modificaProdottoService;

    /**
     * Servizio per la gestione delle informazioni di un prodotto.
     */
    private infoProdottoService infoProdottoService;

    /**
     * Costruttore di default che inizializza i servizi di gestione dei prodotti e informazioni del prodotto.
     */
    public RimuoviProdottoServlet() {
        this.modificaProdottoService = new GestioneProdottoServiceImpl();
        this.infoProdottoService = new infoProdottoServiceImpl();
    }

    /**
     * Imposta il servizio di gestione dei prodotti. Utilizzato per iniettare un servizio esterno, se necessario.
     *
     * @param modificaProdottoService il servizio di gestione dei prodotti
     */
    public void setProdottoService(GestioneProdottoService modificaProdottoService) {
        this.modificaProdottoService = modificaProdottoService;
    }

    /**
     * Imposta il servizio per la gestione delle informazioni del prodotto. Utilizzato per iniettare un servizio esterno, se necessario.
     *
     * @param infoProdottoService il servizio di gestione delle informazioni del prodotto
     */
    public void setInfoProdottoService(infoProdottoService infoProdottoService) {
        this.infoProdottoService = infoProdottoService;
    }

    /**
     * Gestisce una richiesta HTTP di tipo GET per la rimozione di un prodotto.
     *
     * <p>Verifica se l'utente ha i permessi per eliminare il prodotto, quindi recupera l'id del prodotto dalla richiesta,
     * controlla se il prodotto esiste nel sistema e, se tutto è corretto, lo elimina tramite il servizio di gestione prodotti.</p>
     *
     * @param request la richiesta HTTP
     * @param response la risposta HTTP
     * @throws ServletException se si verifica un errore durante l'esecuzione della servlet
     * @throws IOException se si verifica un errore nell'elaborazione dei dati della richiesta
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Utente utente = (Utente) session.getAttribute("utente");

        // Verifica se l'utente è un admin
        if (utente == null || !utente.isRuolo()) {
            throw new MyServletException("Non è possibile accedere a questa pagina");
        }

        try {
            int idProdotto = Integer.parseInt(request.getParameter("id"));

            // Verifica se il prodotto esiste
            if (infoProdottoService.fetchByIdProdotto(idProdotto) == null) {
                throw new MyServletException("Prodotto non esistente");
            }

            // Elimina il prodotto tramite il servizio
            modificaProdottoService.eliminaProdotto(idProdotto);

        } catch (NumberFormatException e) {
            throw new MyServletException("Errore di formato nei dati inseriti nel form");
        }

        // Successo: reindirizza alla pagina di conferma
        String successMessage = "Prodotto eliminato con successo";
        request.setAttribute("successMessage", successMessage);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("WEB-INF/results/confirm-operation.jsp");
        requestDispatcher.forward(request, response);
    }

    /**
     * Gestisce una richiesta HTTP di tipo POST per la rimozione di un prodotto.
     * Questa funzione invoca semplicemente {@link #doGet(HttpServletRequest, HttpServletResponse)}
     * per gestire la logica di eliminazione del prodotto.
     *
     * @param request la richiesta HTTP
     * @param response la risposta HTTP
     * @throws ServletException se si verifica un errore durante l'esecuzione della servlet
     * @throws IOException se si verifica un errore nell'elaborazione dei dati della richiesta
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
