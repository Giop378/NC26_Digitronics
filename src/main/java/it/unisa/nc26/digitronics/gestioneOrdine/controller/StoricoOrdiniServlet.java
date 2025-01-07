package it.unisa.nc26.digitronics.gestioneOrdine.controller;

import it.unisa.nc26.digitronics.gestioneOrdine.service.GestioneOrdineService;
import it.unisa.nc26.digitronics.gestioneOrdine.service.GestioneOrdineServiceImpl;
import it.unisa.nc26.digitronics.model.bean.Ordine;
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
import java.util.List;

@WebServlet("/storicoOrdini")
public class StoricoOrdiniServlet extends HttpServlet {

    private GestioneOrdineService gestioneOrdineService;

    public StoricoOrdiniServlet() {
        this.gestioneOrdineService = new GestioneOrdineServiceImpl();
    }

    public void setOrderService(GestioneOrdineService gestioneOrdineService) {
        this.gestioneOrdineService = gestioneOrdineService;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Utente utente = (Utente) session.getAttribute("utente");

        if(utente == null){
            throw new MyServletException("Non puoi accedere a questa pagina. Accedi per continuare");
        }
        if(!utente.isRuolo() ){
            List<Ordine> ordini = gestioneOrdineService.fetchByIdUtente(utente.getIdUtente());
            request.setAttribute("ordini", ordini);
        }else{
            List<Ordine> ordini = gestioneOrdineService.fetchAllOrders();
            request.setAttribute("ordini", ordini);
        }
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/results/storicoOrdini.jsp");
        requestDispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }


}
