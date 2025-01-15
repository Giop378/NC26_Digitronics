package it.unisa.nc26.digitronics.gestioneProdotto.controller;

import it.unisa.nc26.digitronics.gestioneProdotto.service.GestioneProdottoService;
import it.unisa.nc26.digitronics.gestioneProdotto.service.GestioneProdottoServiceImpl;
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

/**
 * Servlet per la modifica di un prodotto esistente nel sistema.
 * Gestisce la visualizzazione e l'elaborazione dei dati relativi al prodotto,
 * convalidando i parametri inviati tramite la richiesta HTTP e aggiornando il prodotto nel database
 * tramite il servizio {@link GestioneProdottoService}.
 *
 * <p>La servlet consente agli utenti con ruolo di amministratore di modificare i dettagli di un prodotto
 * esistente. Viene effettuata una serie di validazioni sui parametri del prodotto, tra cui nome, descrizione,
 * prezzo, quantità e categoria.</p>
 *
 * <p>In caso di successo, il prodotto viene aggiornato nel database e l'utente viene reindirizzato a una
 * pagina di conferma. Se si verifica un errore durante la validazione o l'elaborazione dei dati,
 * viene generato un messaggio di errore.</p>
 *
 * @see GestioneProdottoService
 * @see Prodotto
 * @see MyServletException
 */
@WebServlet(name = "ModificaProdottoServlet", value = "/modify-product-servlet")
public class ModificaProdottoServlet extends HttpServlet {

    /**
     * Servizio di gestione dei prodotti che si occupa della logica di modifica nel database.
     */
    private GestioneProdottoService modificaProdottoService;

    /**
     * Costruttore di default che inizializza il servizio di gestione dei prodotti.
     */
    public ModificaProdottoServlet() {
        this.modificaProdottoService = new GestioneProdottoServiceImpl();
    }

    /**
     * Imposta il servizio di gestione dei prodotti. Utilizzato per iniettare un servizio esterno, se necessario.
     *
     * @param modifyProductService il servizio di gestione dei prodotti
     */
    public void setGestioneProdottoService(GestioneProdottoService modifyProductService) {
        this.modificaProdottoService = modifyProductService;
    }

    /**
     * Gestisce una richiesta HTTP di tipo GET per la modifica di un prodotto.
     * Recupera i parametri del prodotto, li valida, e aggiorna il prodotto nel database.
     *
     * <p>In caso di successo, l'utente viene reindirizzato alla pagina di conferma. In caso di errore,
     * viene generato un messaggio di errore.</p>
     *
     * @param request la richiesta HTTP
     * @param response la risposta HTTP
     * @throws ServletException se si verifica un errore durante l'esecuzione della servlet
     * @throws IOException se si verifica un errore nell'elaborazione dei dati della richiesta
     */
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
            if (prezzoStr == null || Double.parseDouble(prezzoStr) <= 0) {
                throw new MyServletException("Il prezzo deve essere maggiore di zero e non deve essere vuoto");
            }

            // Validazione del formato del prezzo (almeno due decimali)
            if (prezzoStr != null && !prezzoStr.matches("^(?!0\\.00$)\\d+(\\.\\d{2,})$")) {
                throw new MyServletException("Il prezzo deve essere un valore numerico valido con almeno due cifre decimali (es. 10.00)");
            }

            prezzo = Double.parseDouble(prezzoStr);

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

            // Delegazione al service per aggiornare il prodotto nel database
            modificaProdottoService.modificaProdotto(prodottoModificato);

            // Successo: reindirizza alla pagina di conferma
            String successMessage = "Prodotto modificato con successo";
            request.setAttribute("successMessage", successMessage);
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("WEB-INF/results/confirm-operation.jsp");
            requestDispatcher.forward(request, response);

        } catch (NumberFormatException e) {
            throw new MyServletException("Uno o più parametri errati nel form");
        } catch (Exception e) {
            throw new MyServletException(e.getMessage());
        }
    }

    /**
     * Gestisce una richiesta HTTP di tipo POST per la modifica di un prodotto.
     * Questa funzione invoca il metodo {@link #doGet(HttpServletRequest, HttpServletResponse)}
     * per gestire la logica di modifica del prodotto.
     *
     * @param request la richiesta HTTP
     * @param response la risposta HTTP
     * @throws ServletException se si verifica un errore durante l'esecuzione della servlet
     * @throws IOException se si verifica un errore nell'elaborazione dei dati della richiesta
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}



