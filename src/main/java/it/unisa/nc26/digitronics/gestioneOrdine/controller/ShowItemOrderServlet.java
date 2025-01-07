package it.unisa.nc26.digitronics.gestioneOrdine.controller;

import it.unisa.nc26.digitronics.gestioneOrdine.service.GestioneOrdineService;
import it.unisa.nc26.digitronics.gestioneOrdine.service.GestioneOrdineServiceImpl;
import it.unisa.nc26.digitronics.model.bean.ItemOrdine;
import it.unisa.nc26.digitronics.model.bean.Ordine;
import it.unisa.nc26.digitronics.model.bean.Utente;
import it.unisa.nc26.digitronics.utils.MyServletException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/showItemsOrder")
public class ShowItemOrderServlet extends HttpServlet {

    private GestioneOrdineService gestioneOrdineService;

    public ShowItemOrderServlet() {
        this.gestioneOrdineService = new GestioneOrdineServiceImpl();
    }

    public void setOrderService(GestioneOrdineService gestioneOrdineService) {
        this.gestioneOrdineService = gestioneOrdineService;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int idOrdine;
        try {
            idOrdine = Integer.parseInt(request.getParameter("idOrdine"));
        } catch (NumberFormatException e) {
            throw new MyServletException("idOrdine non valido");
        }

        Utente utente = (Utente) request.getSession().getAttribute("utente");

        if( utente.isRuolo() ){
            List<ItemOrdine> itemOrdineList= gestioneOrdineService.fetchItemOrder(idOrdine);
            request.setAttribute("itemOrdineList", itemOrdineList);
            if(itemOrdineList.isEmpty()){
                throw new MyServletException("Ordine non esistente");
            }
        }
        //Nel caso che è un utente normale si mostra il suo storico ordini (si deve però controllare se l'utente può vedere quella lista di ordini tramite un ciclo for)
        else{
            List<Ordine> ordini = gestioneOrdineService.fetchByIdUtente(utente.getIdUtente());
            Ordine ordine = gestioneOrdineService.fetchByIdOrder(idOrdine);
            if(ordini.contains(ordine)){
                List<ItemOrdine> itemOrdineList = gestioneOrdineService.fetchItemOrder(idOrdine);
                request.setAttribute("itemOrdineList", itemOrdineList);
            }else{
                throw new MyServletException("Non puoi visualizzare questo ordine perchè non fa parte dei tuoi ordini");
            }
        }

        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/results/showItemsOrder.jsp");
        requestDispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
