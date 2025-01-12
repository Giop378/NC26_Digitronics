package it.unisa.nc26.digitronics.infoProdotto.controller;

import it.unisa.nc26.digitronics.infoProdotto.service.infoProdottoService;
import it.unisa.nc26.digitronics.infoProdotto.service.infoProdottoServiceImpl;
import it.unisa.nc26.digitronics.model.bean.Categoria;
import it.unisa.nc26.digitronics.model.bean.Prodotto;
import it.unisa.nc26.digitronics.utils.MyServletException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Servlet per gestire le operazioni relative alle categorie.
 * Questa servlet elabora richieste GET e POST per ottenere informazioni su una categoria,
 * i prodotti associati e tutte le categorie disponibili.
 */
@WebServlet(name = "CategoriaServlet", value = "/categoria")
public class CategoriaServlet extends HttpServlet {

    /**
     * Servizio per la gestione delle informazioni sui prodotti e categorie.
     */
    private infoProdottoService infoProdottoService;

    /**
     * Costruttore di default che inizializza il servizio infoProdottoService.
     */
    public CategoriaServlet() {
        this.infoProdottoService = new infoProdottoServiceImpl();
    }

    /**
     * Imposta un'istanza personalizzata del servizio infoProdottoService.
     *
     * @param infoProdottoService il servizio da utilizzare.
     */
    public void setCategoryService(infoProdottoService infoProdottoService) {
        this.infoProdottoService = infoProdottoService;
    }

    /**
     * Gestisce le richieste HTTP GET per ottenere i dettagli di una categoria e i relativi prodotti.
     *
     * @param request  l'oggetto HttpServletRequest contenente i dettagli della richiesta.
     * @param response l'oggetto HttpServletResponse per inviare la risposta.
     * @throws ServletException in caso di errore nella gestione della richiesta.
     * @throws IOException      in caso di errore di input/output.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nomeCategoria = request.getParameter("categoria");

        // Controllo sul parametro
        if (nomeCategoria == null || nomeCategoria.isEmpty()) {
            throw new MyServletException("Parametro categoria non valido");
        }

        // Chiamate al Service
        Categoria categoriaScelta = infoProdottoService.getCategoriaPerNome(nomeCategoria);
        if (categoriaScelta == null) {
            throw new MyServletException("La categoria non è stata trovata");
        }

        List<Prodotto> prodottiPerCategoria = infoProdottoService.getProdottiPerCategoria(nomeCategoria);
        List<Categoria> categorie = infoProdottoService.getAllCategorie();

        // Imposta gli attributi e inoltra la richiesta
        request.setAttribute("prodottiPerCategoria", prodottiPerCategoria);
        request.setAttribute("categoriaScelta", categoriaScelta);
        request.setAttribute("categorie", categorie);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/results/category.jsp");
        dispatcher.forward(request, response);
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

