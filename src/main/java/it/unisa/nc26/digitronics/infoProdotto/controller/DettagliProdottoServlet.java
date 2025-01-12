package it.unisa.nc26.digitronics.infoProdotto.controller;

import it.unisa.nc26.digitronics.infoProdotto.service.InfoProdottoService;
import it.unisa.nc26.digitronics.infoProdotto.service.InfoProdottoServiceImpl;
import it.unisa.nc26.digitronics.model.bean.Prodotto;
import it.unisa.nc26.digitronics.model.bean.Recensione;
import it.unisa.nc26.digitronics.utils.MyServletException;
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

    private InfoProdottoService infoProdottoService;

    public void setProdottoService(InfoProdottoService infoProdottoService) {
        this.infoProdottoService = infoProdottoService;
    }

    public DettagliProdottoServlet() {
        this.infoProdottoService = new InfoProdottoServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Recupero id del prodotto dalla request
        String idProduct = req.getParameter("id");

        if (idProduct != null) {
            try {
                //Recupero il prodotto dal database
                Prodotto product = infoProdottoService.fetchByIdProdotto(Integer.parseInt(idProduct));
                if (product == null) {
                    throw new MyServletException("Id prodotto non valido");
                }
                List<Recensione> recensioni = infoProdottoService.fetchRecensioniByIdProdotto(Integer.parseInt(idProduct));
                req.setAttribute("product", product);
                req.setAttribute("recensioni", recensioni);
                getServletContext().getRequestDispatcher("/WEB-INF/results/dettagliProdotto.jsp").forward(req, resp);
            } catch (NumberFormatException e) {
                throw new MyServletException("Id prodotto deve essere un numero!");
            } catch (SQLException e) {
                throw new MyServletException("Errore database:"+e.getMessage());
            }
        } else {
            throw new MyServletException("Id prodotto non può essere vuoto!");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}

