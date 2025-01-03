package it.unisa.nc26.digitronics.infoProdotto.controller;

import it.unisa.nc26.digitronics.infoProdotto.service.CategoriaService;
import it.unisa.nc26.digitronics.infoProdotto.service.CategoriaServiceImpl;
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

@WebServlet(name = "CategoriaServlet", value = "/categoria")
public class CategoriaServlet extends HttpServlet {
    private CategoriaService categoriaService;

    public CategoriaServlet() {
        this.categoriaService = new CategoriaServiceImpl();
    }

    public void setCategoryService(CategoriaService categoryService) {
        this.categoriaService= categoryService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nomeCategoria = request.getParameter("categoria");

        // Controllo sul parametro
        if (nomeCategoria == null || nomeCategoria.isEmpty()) {
            throw new MyServletException("Parametro categoria non valido");
        }

        // Chiamate al Service
        Categoria categoriaScelta = categoriaService.getCategoriaPerNome(nomeCategoria);
        if (categoriaScelta == null) {
            throw new MyServletException("La categoria non è stata trovata");
        }

        List<Prodotto> prodottiPerCategoria = categoriaService.getProdottiPerCategoria(nomeCategoria);
        List<Categoria> categorie = categoriaService.getAllCategorie();

        // Imposta gli attributi e inoltra la richiesta
        request.setAttribute("prodottiPerCategoria", prodottiPerCategoria);
        request.setAttribute("categoriaScelta", categoriaScelta);
        request.setAttribute("categorie", categorie);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/results/category.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}

