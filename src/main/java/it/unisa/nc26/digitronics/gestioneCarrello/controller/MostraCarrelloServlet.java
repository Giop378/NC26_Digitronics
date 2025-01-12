package it.unisa.nc26.digitronics.gestioneCarrello.controller;

import it.unisa.nc26.digitronics.gestioneCarrello.service.GestioneCarrelloService;
import it.unisa.nc26.digitronics.gestioneCarrello.service.GestioneCarrelloServiceImpl;
import it.unisa.nc26.digitronics.model.bean.ItemCarrello;
import it.unisa.nc26.digitronics.model.bean.Prodotto;
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
import java.util.ArrayList;
import java.util.List;
@WebServlet("/mostra-carrello")
public class MostraCarrelloServlet extends HttpServlet {
    private GestioneCarrelloService gestioneCarrelloService;

    public MostraCarrelloServlet() {
        this.gestioneCarrelloService = new GestioneCarrelloServiceImpl();
    }

    public void setGestioneCarrelloService(GestioneCarrelloService gestioneCarrelloService) {
        this.gestioneCarrelloService = gestioneCarrelloService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Utente utente = (Utente) session.getAttribute("utente");
        if(utente!=null) {
            if ( utente.isRuolo() ) {
                throw new MyServletException("L'admin non può interagire con il carrello");
            }
        }
        List<ItemCarrello> carrello = (List<ItemCarrello>) session.getAttribute("carrello");
        if (carrello==null) {
            carrello = new ArrayList<ItemCarrello>();
            session.setAttribute("carrello", carrello);
        }else{
            for(ItemCarrello c : carrello){
                int idProdotto = c.getIdProdotto();
                //devo controllare se la quantità inserita nel carrello dall'utente è effettivamente presente
                int quantitàNelCarrello= c.getQuantità();
                Prodotto prodotto = gestioneCarrelloService.fetchByIdProdotto(idProdotto);


                if(quantitàNelCarrello > prodotto.getQuantità()){
                    c.setQuantità(prodotto.getQuantità());
                }
            }
            session.setAttribute("carrello", carrello);
        }


        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/results/cart.jsp");
        requestDispatcher.forward(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
