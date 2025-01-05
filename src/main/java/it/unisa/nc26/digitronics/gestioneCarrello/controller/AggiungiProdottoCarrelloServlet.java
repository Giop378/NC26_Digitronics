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

@WebServlet("/aggiungi-prodotto-carrello")
public class AggiungiProdottoCarrelloServlet extends HttpServlet {
    private GestioneCarrelloService gestioneCarrelloService;

    public AggiungiProdottoCarrelloServlet() {
        this.gestioneCarrelloService = new GestioneCarrelloServiceImpl();
    }

    public void setGestioneCarrelloService(GestioneCarrelloService gestioneCarrelloService) {
        this.gestioneCarrelloService = gestioneCarrelloService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        List<ItemCarrello> carrelloSession = (List<ItemCarrello>) session.getAttribute("carrello");
        if (carrelloSession == null) {
            carrelloSession = new ArrayList<>();
            session.setAttribute("carrello", carrelloSession);
        }

        //Controlli lato server il parametro idProdotto
        int idProdotto = 0;
        String idProdottoParam = request.getParameter("idProdotto");

        if (idProdottoParam == null || idProdottoParam.trim().isEmpty()) {
            throw new MyServletException("Il parametro idProdotto non può essere vuoto");
        }

        try {
            idProdotto = Integer.parseInt(idProdottoParam);
            if (idProdotto <= 0) {
                throw new MyServletException("Id prodotto non valido: deve essere un numero intero positivo");
            }
        } catch (NumberFormatException e) {
            throw new MyServletException("Id prodotto non valido: deve essere un numero intero");
        }


        Prodotto p = gestioneCarrelloService.fetchByIdProdotto(idProdotto);
        if(p==null){
            throw new MyServletException("Il prodotto non esiste");
        }

        //Controllo lato server parametro quantità
        int quantità;
        String quantitàParam = request.getParameter("quantità");
        if (quantitàParam == null || quantitàParam.trim().isEmpty()) {
            throw new MyServletException("Inserire la quantità è obbligatorio");
        }

        try {
            quantità= Integer.parseInt(quantitàParam);
            if (quantità<= 0) {
                throw new MyServletException("La quantità deve essere un numero intero positivo");
            }
        } catch (NumberFormatException e) {
            throw new MyServletException("Quantità non valida: deve essere un numero intero");
        }

        ItemCarrello itemCarrello = new ItemCarrello();
        if (session.getAttribute("utente") == null) {
            itemCarrello.setIdUtente(null);
        } else {
            Utente utente = (Utente) session.getAttribute("utente");
            if(utente.isRuolo()){
                throw new MyServletException("L'admin non può interagire con il carrello");
            }
            int idUtente = utente.getIdUtente();
            itemCarrello.setIdUtente(idUtente);
        }
        itemCarrello.setProdotto(p);
        itemCarrello.setIdProdotto(p.getIdProdotto());

        //Prima di inserire la quantità controllo che effettivamente ci sono in magazzino abbastanza prodotti
        if(quantità > p.getQuantità()){
            throw new MyServletException("Quantità selezionata del prodotto non presente in magazzino");
        }
        itemCarrello.setQuantità(quantità);

        // Controlla se il prodotto è già presente nel carrello
        boolean prodottoTrovato = false;
        for (ItemCarrello item : carrelloSession) {
            if (item.getIdProdotto() == itemCarrello.getIdProdotto()) {
                // Aggiorna la quantità del prodotto esistente
                item.setQuantità(item.getQuantità() + quantità);
                prodottoTrovato = true;
                break;
            }
        }

        // Se il prodotto non è trovato, aggiungilo al carrello
        if (!prodottoTrovato) {
            carrelloSession.add(itemCarrello);
        }

        session.setAttribute("carrello", carrelloSession);

        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/results/cart.jsp");
        requestDispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
