package it.unisa.nc26.digitronics.infoProdotto.controller;

import it.unisa.nc26.digitronics.gestioneRecensione.service.RecensioneService;
import it.unisa.nc26.digitronics.gestioneRecensione.service.RecensioneServiceImpl;
import it.unisa.nc26.digitronics.infoProdotto.service.ProdottoService;
import it.unisa.nc26.digitronics.infoProdotto.service.ProdottoServiceImpl;
import it.unisa.nc26.digitronics.model.bean.Prodotto;
import it.unisa.nc26.digitronics.model.bean.Recensione;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/dettagliProdotto")
public class DettagliProdottoServlet extends HttpServlet {

    private ProdottoService prodottoService;
    private RecensioneService recensioneService;

    public void setProdottoService(ProdottoService prodottoService) {
        this.prodottoService = prodottoService;
    }

    public void setRecensioneService(RecensioneService recensioneService) {
        this.recensioneService = recensioneService;
    }

    public DettagliProdottoServlet() {
        this.prodottoService = new ProdottoServiceImpl();
        this.recensioneService = new RecensioneServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Recupero id del prodotto dalla request
        String idProduct = req.getParameter("id");

        if (idProduct != null) {
            try {
                //Recupero il prodotto dal database
                Prodotto product = prodottoService.fetchByIdProdotto(Integer.parseInt(idProduct));
                List<Recensione> recensioni = (List<Recensione>) recensioneService.fetchByProduct(Integer.parseInt(idProduct));
                if (product == null) {
                    req.setAttribute("errorMsg", "Id prodotto non valido");
                    getServletContext().getRequestDispatcher("/WEB-INF/results/error.jsp").forward(req, resp);
                }
                req.setAttribute("product", product);
                req.setAttribute("recensioni", recensioni);
                getServletContext().getRequestDispatcher("/WEB-INF/results/dettagliProdotto.jsp").forward(req, resp);
            } catch (NumberFormatException e) {
                resp.sendError(400);
            } catch (SQLException e) {
                e.printStackTrace();
                resp.sendError(500);
            }
        } else {
            resp.sendRedirect(getServletContext().getContextPath() + "/WEB-INF/results/home.jsp");
        }
    }
}

