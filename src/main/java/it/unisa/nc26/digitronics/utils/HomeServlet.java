package it.unisa.nc26.digitronics.utils;

import it.unisa.nc26.digitronics.infoProdotto.service.InfoProdottoService;
import it.unisa.nc26.digitronics.infoProdotto.service.InfoProdottoServiceImpl;
import it.unisa.nc26.digitronics.model.bean.Prodotto;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Servlet per la gestione della home page dell'applicazione.
 *
 * Questa servlet recupera i prodotti da mostrare in vetrina
 * e li passa alla pagina JSP per la visualizzazione.
 */
@WebServlet(name = "HomeServlet", value = "/index.html")
public class HomeServlet extends HttpServlet {

    private InfoProdottoService infoProdottoService;

    /**
     * Costruttore di default che inizializza il servizio per la gestione delle informazioni sui prodotti.
     */
    public HomeServlet() {
        this.infoProdottoService = new InfoProdottoServiceImpl();
    }

    /**
     * Imposta un'istanza personalizzata del servizio per la gestione delle informazioni sui prodotti.
     *
     * @param infoProdottoService il servizio da utilizzare
     */
    public void setInfoProdottoService(InfoProdottoService infoProdottoService) {
        this.infoProdottoService = infoProdottoService;
    }

    /**
     * Gestisce le richieste HTTP di tipo GET.
     *
     * Recupera la lista dei prodotti in vetrina, la inserisce come attributo nella richiesta
     * e inoltra la richiesta alla pagina JSP per la visualizzazione.
     *
     * @param request  l'oggetto HttpServletRequest contenente i dettagli della richiesta
     * @param response l'oggetto HttpServletResponse per l'invio della risposta al client
     * @throws ServletException se si verifica un errore durante l'elaborazione della richiesta
     * @throws IOException      se si verifica un errore di input/output durante l'elaborazione della richiesta
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Prodotto> prodottiVetrina = infoProdottoService.fetchProdottoVetrina();
        request.setAttribute("prodottiVetrina", prodottiVetrina);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/results/home.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Gestisce le richieste HTTP di tipo POST.
     *
     * Redirige le richieste POST al metodo {@link #doGet(HttpServletRequest, HttpServletResponse)}.
     *
     * @param request  l'oggetto HttpServletRequest contenente i dettagli della richiesta
     * @param response l'oggetto HttpServletResponse per l'invio della risposta al client
     * @throws ServletException se si verifica un errore durante l'elaborazione della richiesta
     * @throws IOException      se si verifica un errore di input/output durante l'elaborazione della richiesta
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}