package it.unisa.nc26.digitronics.gestioneCarrello.controller;

import it.unisa.nc26.digitronics.gestioneCarrello.service.GestioneCarrelloService;
import it.unisa.nc26.digitronics.gestioneCarrello.service.GestioneCarrelloServiceImpl;
import it.unisa.nc26.digitronics.model.bean.ItemCarrello;
import it.unisa.nc26.digitronics.model.bean.Prodotto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Servlet per la gestione dell'aggiornamento delle quantità dei prodotti nel carrello.
 */
@WebServlet("/aggiorna-quantità-carrello")
public class AggiornaQuantitàCarrelloServlet extends HttpServlet {
    private GestioneCarrelloService gestioneCarrelloService;

    /**
     * Costruttore che inizializza il servizio di gestione del carrello con l'implementazione predefinita.
     */
    public AggiornaQuantitàCarrelloServlet() {
        this.gestioneCarrelloService = new GestioneCarrelloServiceImpl();
    }

    /**
     * Imposta un'istanza personalizzata del servizio di gestione del carrello.
     *
     * @param gestioneCarrelloService il servizio personalizzato da utilizzare
     */
    public void setGestioneCarrelloService(GestioneCarrelloService gestioneCarrelloService) {
        this.gestioneCarrelloService = gestioneCarrelloService;
    }

    /**
     * Gestisce le richieste HTTP POST delegandole al metodo doGet.
     *
     * @param request  la richiesta HTTP
     * @param response la risposta HTTP
     * @throws ServletException in caso di errori nella gestione della richiesta
     * @throws IOException      in caso di errori di input/output
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    /**
     * Gestisce le richieste HTTP GET per aggiornare la quantità di un prodotto nel carrello.
     *
     * @param request  la richiesta HTTP
     * @param response la risposta HTTP
     * @throws ServletException in caso di errori nella gestione della richiesta
     * @throws IOException      in caso di errori di input/output
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        response.setContentType("application/json");

        synchronized (session) {
            List<ItemCarrello> carrelloSession = (List<ItemCarrello>) session.getAttribute("carrello");

            JSONParser parser = new JSONParser();
            try(PrintWriter out = response.getWriter();) {
                int idProdotto= Integer.parseInt(request.getParameter("idProdotto"));
                Prodotto prodotto = gestioneCarrelloService.fetchByIdProdotto(idProdotto);

                if (prodotto == null) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    JSONObject error = new JSONObject();
                    error.put("message", "Prodotto non esistente");
                    out.print(error.toJSONString());
                    out.flush();
                    return;
                } else {
                    int nuovaQuantita = Integer.parseInt(request.getParameter("nuovaQuantita"));
                    if (nuovaQuantita <= 0) {
                        nuovaQuantita = 1;
                    }

                    boolean prodottoTrovato = false;
                    for (ItemCarrello prodottoCarrello : carrelloSession) {
                        if (prodottoCarrello.getIdProdotto() == idProdotto) {
                            if (nuovaQuantita > prodotto.getQuantità()) {
                                nuovaQuantita = prodotto.getQuantità();
                            }

                            prodottoCarrello.setQuantità(nuovaQuantita);
                            JSONObject result = new JSONObject();
                            result.put("nuovaQuantita", nuovaQuantita);
                            out.print(result.toJSONString());
                            out.flush();

                            prodottoTrovato = true;
                            break;
                        }
                    }

                    if (!prodottoTrovato) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        JSONObject error = new JSONObject();
                        error.put("message", "Prodotto non trovato nel carrello");
                        out.print(error.toJSONString());
                        out.flush();
                        return;
                    }

                    session.setAttribute("carrello", carrelloSession);
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                JSONObject error = new JSONObject();
                error.put("message", "Errore durante l'elaborazione della richiesta");
                try (PrintWriter out = response.getWriter()) {
                    out.print(error.toJSONString());
                }
            }
        }
    }
}
