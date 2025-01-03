package it.unisa.nc26.digitronics.infoProdotto.controller;

import it.unisa.nc26.digitronics.infoProdotto.service.ProdottoRicercaService;
import it.unisa.nc26.digitronics.infoProdotto.service.ProdottoRicercaServiceImpl;
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

@WebServlet("/search")
public class ProdottoRicercaServlet extends HttpServlet {
    private ProdottoRicercaService prodottoRicercaService;

    public ProdottoRicercaServlet() {
        this.prodottoRicercaService = new ProdottoRicercaServiceImpl();
    }

    public void setProductSearchService(ProdottoRicercaService productSearchService) {
        this.prodottoRicercaService = productSearchService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("query");

        // Delegare la ricerca al service
        List<Prodotto> risultati;
        try {
            risultati = prodottoRicercaService.cercaProdottoPerNome(query);
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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
