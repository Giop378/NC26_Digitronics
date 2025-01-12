package it.unisa.nc26.digitronics.gestioneProdotto.controller;

import it.unisa.nc26.digitronics.gestioneProdotto.service.ModificaProdottoService;
import it.unisa.nc26.digitronics.gestioneProdotto.service.ModificaProdottoServiceImpl;
import it.unisa.nc26.digitronics.infoProdotto.service.InfoProdottoService;
import it.unisa.nc26.digitronics.infoProdotto.service.InfoProdottoServiceImpl;
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

@WebServlet(name = "DeleteProductServlet", value = "/delete-product-servlet")
public class RimuoviProdottoServlet extends HttpServlet {

    private ModificaProdottoService modificaProdottoService;
    private InfoProdottoService infoProdottoService;

    public RimuoviProdottoServlet() {
        this.modificaProdottoService = new ModificaProdottoServiceImpl();
        this.infoProdottoService = new InfoProdottoServiceImpl();
    }

    public void setProdottoService(ModificaProdottoService modificaProdottoService) {
        this.modificaProdottoService = modificaProdottoService;
    }

    public void setInfoProdottoService(InfoProdottoService infoProdottoService) {
        this.infoProdottoService = infoProdottoService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Utente utente = (Utente) session.getAttribute("utente");
        if(utente == null || !utente.isRuolo()){
            throw new MyServletException("Non è possibile accedere a questa pagina");
        }
        try {
            int idProdotto = Integer.parseInt(request.getParameter("id"));
            if(infoProdottoService.fetchByIdProdotto(idProdotto) == null){
                throw new MyServletException("Prodotto non esistente");
            }
            modificaProdottoService.eliminaProdotto(idProdotto);
        } catch (NumberFormatException e) {
            throw new MyServletException("Errore di formato nei dati inseriti nel form");
        }
        String successMessage = "Prodotto eliminato con successo";
        request.setAttribute("successMessage", successMessage);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("WEB-INF/results/confirm-operation.jsp");
        requestDispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
