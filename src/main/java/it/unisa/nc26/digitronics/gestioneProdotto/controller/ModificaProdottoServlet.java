package it.unisa.nc26.digitronics.gestioneProdotto.controller;

import it.unisa.nc26.digitronics.gestioneProdotto.service.ModificaProdottoService;
import it.unisa.nc26.digitronics.gestioneProdotto.service.ModificaProdottoServiceImpl;
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

@WebServlet(name = "ModificaProdottoServlet", value = "/modify-product-servlet")
public class ModificaProdottoServlet extends HttpServlet {
    private ModificaProdottoService modificaProdottoService;

    public ModificaProdottoServlet() {
        this.modificaProdottoService = new ModificaProdottoServiceImpl();
    }

    public void setModifyProductService(ModificaProdottoService modifyProductService) {
        this.modificaProdottoService= modifyProductService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Utente utente = (Utente) session.getAttribute("utente");

        // Verifica se l'utente è un admin
        if (utente == null || !utente.isRuolo()) {
            throw new MyServletException("Non è possibile accedere a questa pagina");
        }

        try {
            // Recupero parametri dalla richiesta
            int idProdotto = Integer.parseInt(request.getParameter("id"));
            String nome = request.getParameter("nome");
            String descrizione = request.getParameter("descrizione");
            double prezzo;
            String prezzoStr = request.getParameter("prezzo");
            boolean inVetrina = request.getParameter("vetrina") != null; // checkbox
            int quantita = Integer.parseInt(request.getParameter("quantita"));
            String nomeCategoria = request.getParameter("nomecategoria");

            // Validazione parametro Nome Prodotto
            if (nome == null || nome.trim().isEmpty() || nome.length() > 255) {
                throw new MyServletException("Il nome del prodotto deve avere una lunghezza tra 1 e 255 caratteri");
            }

            // Validazione parametro Descrizione
            if (descrizione == null || descrizione.trim().isEmpty()) {
                throw new MyServletException("La descrizione del prodotto non può essere vuota");
            }

            // Validazioni Prezzo
            if (prezzoStr == null || !prezzoStr.matches("^(?!0\\.00$)\\d+(\\.\\d{2,})$")) {
                throw new MyServletException("Il prezzo deve essere un valore numerico valido con almeno due cifre decimali (es. 10.00)");
            }
            prezzo = Double.parseDouble(prezzoStr);
            if (prezzo <= 0) {
                throw new MyServletException("Il prezzo deve essere maggiore di zero");
            }

            // Validazioni Quantità
            if (quantita <= 0) {
                throw new MyServletException("La quantità deve essere maggiore di zero");
            }

            // Validazioni Categoria
            if (nomeCategoria == null || nomeCategoria.trim().isEmpty() || nomeCategoria.length() > 255) {
                throw new MyServletException("La categoria deve avere una lunghezza compresa tra 1 e 255 caratteri");
            }

            // Creazione prodotto da modificare
            Prodotto prodottoModificato = new Prodotto();
            prodottoModificato.setIdProdotto(idProdotto);
            prodottoModificato.setNome(nome);
            prodottoModificato.setDescrizione(descrizione);
            prodottoModificato.setPrezzo(prezzo);
            prodottoModificato.setQuantità(quantita);
            prodottoModificato.setNomeCategoria(nomeCategoria);
            prodottoModificato.setVetrina(inVetrina);

            // Delegazione al service
            modificaProdottoService.modificaProdotto(prodottoModificato);

            // Successo
            String successMessage = "Prodotto modificato con successo";
            request.setAttribute("successMessage", successMessage);
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("WEB-INF/results/confirm-operation.jsp");
            requestDispatcher.forward(request, response);

        } catch (NumberFormatException e) {
            throw new MyServletException("Uno o più parametri errati nel form");
        } catch (MyServletException e) {
            throw new MyServletException(e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
