package it.unisa.nc26.digitronics.utils;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Servlet per la gestione delle operazioni di amministrazione relative ai prodotti.
 *
 * Questa servlet instrada le richieste dell'amministratore verso pagine specifiche
 * per aggiungere, modificare o eliminare prodotti, in base al parametro "choice".
 */
@WebServlet("/admin-choice-servlet")
public class AdminChoiceServlet extends HttpServlet {

    /**
     * Gestisce le richieste HTTP di tipo GET.
     *
     * In base al valore del parametro "choice", questa servlet instrada la richiesta
     * verso la pagina appropriata:
     * - "addproduct" per aggiungere un prodotto
     * - "modifyproduct" per modificare un prodotto
     * - "deleteproduct" per eliminare un prodotto
     *
     * @param request  l'oggetto {@link HttpServletRequest} contenente i dettagli della richiesta
     * @param response l'oggetto {@link HttpServletResponse} per inviare la risposta
     * @throws ServletException se si verifica un errore durante l'elaborazione della richiesta
     * @throws IOException      se si verifica un errore di input/output durante l'elaborazione
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String choice = request.getParameter("choice");
        if(choice==null){
            throw new MyServletException("Errore nella richiesta");
        }
        if("addproduct".equals(choice)){
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/results/add-product.jsp");
            dispatcher.forward(request, response);
        }else if("modifyproduct".equals(choice)){
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/results/modify-product.jsp");
            dispatcher.forward(request, response);
        }else if("deleteproduct".equals(choice)){
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/results/delete-product.jsp");
            dispatcher.forward(request, response);
        }else{
            throw new MyServletException("Errore nella richiesta");
        }
    }

    /**
     * Gestisce le richieste HTTP di tipo POST.
     *
     * Redirige le richieste POST al metodo {@link #doGet(HttpServletRequest, HttpServletResponse)}.
     *
     * @param request  l'oggetto {@link HttpServletRequest} contenente i dettagli della richiesta
     * @param response l'oggetto {@link HttpServletResponse} per inviare la risposta
     * @throws ServletException se si verifica un errore durante l'elaborazione della richiesta
     * @throws IOException      se si verifica un errore di input/output durante l'elaborazione
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
