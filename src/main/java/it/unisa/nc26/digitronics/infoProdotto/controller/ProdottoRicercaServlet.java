package it.unisa.nc26.digitronics.infoProdotto.controller;

import it.unisa.nc26.digitronics.infoProdotto.service.infoProdottoService;
import it.unisa.nc26.digitronics.infoProdotto.service.infoProdottoServiceImpl;
import it.unisa.nc26.digitronics.model.bean.Prodotto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


import java.io.IOException;
import java.util.List;

/**
 * Servlet per la ricerca di prodotti.
 * Gestisce richieste HTTP GET e POST per effettuare ricerche di prodotti tramite query.
 */
@WebServlet("/search")
public class ProdottoRicercaServlet extends HttpServlet {

    /**
     * Servizio per la gestione delle informazioni sui prodotti.
     */
    private infoProdottoService infoProdottoService;

    /**
     * Costruttore di default che inizializza il servizio infoProdottoService.
     */
    public ProdottoRicercaServlet() {
        this.infoProdottoService = new infoProdottoServiceImpl();
    }

    /**
     * Imposta un'istanza personalizzata del servizio infoProdottoService.
     *
     * @param infoProdottoService il servizio da utilizzare.
     */
    public void setProductService(infoProdottoService infoProdottoService) {
        this.infoProdottoService = infoProdottoService;
    }

    /**
     * Gestisce le richieste HTTP GET per cercare prodotti in base a una query.
     *
     * @param request  l'oggetto HttpServletRequest contenente i dettagli della richiesta.
     * @param response l'oggetto HttpServletResponse per inviare la risposta.
     * @throws ServletException in caso di errore nella gestione della richiesta.
     * @throws IOException      in caso di errore di input/output.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("query");

        // Delegare la ricerca al service
        List<Prodotto> risultati;
        try {
            risultati = infoProdottoService.cercaProdottoPerNome(query);
        } catch (Exception e) {
            throw new ServletException("Errore nella ricerca dei prodotti", e);
        }

        // Convertire la lista di prodotti in formato JSON
        JSONArray jsonRisultati = new JSONArray();
        for (Prodotto prodotto : risultati) {
            JSONObject prodottoJson = new JSONObject();
            prodottoJson.put("idProdotto", prodotto.getIdProdotto());
            prodottoJson.put("nome", prodotto.getNome());
            prodottoJson.put("descrizione", prodotto.getDescrizione());
            prodottoJson.put("prezzo", prodotto.getPrezzo());
            prodottoJson.put("vetrina", prodotto.isVetrina());
            prodottoJson.put("immagine", prodotto.getImmagine());
            prodottoJson.put("quantita", prodotto.getQuantità());
            prodottoJson.put("nomecategoria", prodotto.getNomeCategoria());

            jsonRisultati.add(prodottoJson);
        }

        // Rispondere con i risultati in formato JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonRisultati.toJSONString());
    }

    /**
     * Gestisce le richieste HTTP POST inoltrandole al metodo doGet.
     *
     * @param request  l'oggetto HttpServletRequest contenente i dettagli della richiesta.
     * @param response l'oggetto HttpServletResponse per inviare la risposta.
     * @throws ServletException in caso di errore nella gestione della richiesta.
     * @throws IOException      in caso di errore di input/output.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}