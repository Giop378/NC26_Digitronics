package it.unisa.nc26.digitronics.gestioneRecensione.controller;

import it.unisa.nc26.digitronics.gestioneRecensione.service.RecensioneService;
import it.unisa.nc26.digitronics.gestioneRecensione.service.RecensioneServiceImpl;
import it.unisa.nc26.digitronics.model.bean.Prodotto;
import it.unisa.nc26.digitronics.model.bean.Recensione;
import it.unisa.nc26.digitronics.model.bean.Utente;
import it.unisa.nc26.digitronics.utils.HomeServlet;
import it.unisa.nc26.digitronics.utils.MyServletException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;


/**
 * Servlet per aggiungere una recensione a un prodotto.
 * Gestisce le richieste GET e POST per l'inserimento di una recensione nel database.
 */
@WebServlet("/aggiungiRecensione")
public class AggiungiRecensioneServlet extends HomeServlet {

    /** Servizio per la gestione delle recensioni. */
    private RecensioneService recensioneService;

    /**
     * Imposta un'istanza personalizzata del servizio delle recensioni.
     *
     * @param recensioneService L'implementazione del servizio {@link RecensioneService}.
     */
    public void setRecensioneService(RecensioneService recensioneService) {
        this.recensioneService = recensioneService;
    }

    /**
     * Costruttore predefinito che inizializza il servizio per la gestione delle recensioni.
     */
    public AggiungiRecensioneServlet() {
        this.recensioneService = new RecensioneServiceImpl();
    }

    /**
     * Gestisce le richieste GET per aggiungere una recensione.
     *
     * @param request  La richiesta HTTP.
     * @param response La risposta HTTP.
     * @throws ServletException Se si verifica un errore nella gestione della richiesta.
     * @throws IOException      Se si verifica un errore di input/output.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Verifica la presenza di un utente loggato nella sessione.
        Utente utente = (Utente) request.getSession().getAttribute("utente");

        // Se l'utente non è loggato, lancia un'eccezione personalizzata.
        if (utente == null) {
            throw new MyServletException("Devi essere registrato per effettuare una recensione");
        }

        // Recupero dei parametri inviati con la richiesta HTTP.
        String titolo = request.getParameter("title");
        String descrizione = request.getParameter("description");
        String scoreParam = request.getParameter("score");
        String productIdParam = request.getParameter("productId");

        // Validazione dei parametri ricevuti.
        if(titolo == null || titolo.isEmpty() || titolo.length() > 255){
            throw new MyServletException("Titolo non valido.");
        }

        if(descrizione == null || descrizione.isEmpty()){
            throw new MyServletException("Descrizione non valida.");
        }

        if(scoreParam == null || scoreParam.isEmpty()){
            throw new MyServletException("Punteggio non valido.");
        }

        if(productIdParam == null || productIdParam.isEmpty()) {
            throw new MyServletException("Id prodotto non valido.");
        }


        int punteggio;
        int idProduct;
        try {
            punteggio = Integer.parseInt(scoreParam);
            idProduct = Integer.parseInt(productIdParam);

            if (punteggio < 1 || punteggio > 5) {
                throw new MyServletException("Punteggio non valido.");
            }
        } catch (NumberFormatException e) {
            throw new MyServletException("Punteggio non valido.");
        }

        // Creazione di un oggetto `Recensione` con i dati forniti dall'utente.
        Recensione recensione = new Recensione();
        recensione.setTitolo(titolo);
        recensione.setDescrizione(descrizione);
        recensione.setPunteggio(punteggio);
        recensione.setIdUtente(utente.getIdUtente());
        recensione.setIdProdotto(idProduct);

        // Salvataggio della recensione nel database
        try {
            recensioneService.saveRecensione(recensione);
            response.sendRedirect("dettagliProdotto?id=" + idProduct);
        } catch (SQLException e) {
            throw new MyServletException("Errore database: " + e.getMessage());
        }
    }

    /**
     * Gestisce le richieste POST per aggiungere una recensione.
     * Reindirizza le richieste POST al metodo {@link #doGet(HttpServletRequest, HttpServletResponse)}.
     *
     * @param request  La richiesta HTTP.
     * @param response La risposta HTTP.
     * @throws ServletException Se si verifica un errore nella gestione della richiesta.
     * @throws IOException      Se si verifica un errore di input/output.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
