package it.unisa.nc26.digitronics.utils;

import it.unisa.nc26.digitronics.infoProdotto.service.InfoProdottoService;
import it.unisa.nc26.digitronics.infoProdotto.service.InfoProdottoServiceImpl;
import it.unisa.nc26.digitronics.model.bean.Categoria;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

import java.util.List;

/**
 * Servlet di inizializzazione per il caricamento delle categorie nel contesto dell'applicazione.
 *
 * Questa servlet viene caricata all'avvio dell'applicazione per inizializzare
 * alcune risorse globali, come la lista delle categorie disponibili.
 */
@WebServlet(name = "MyInit", urlPatterns = "/MyInit", loadOnStartup = 0)
public class InitServlet extends HttpServlet {

    private InfoProdottoService infoProdottoService;

    /**
     * Costruttore di default che inizializza il servizio per la gestione delle informazioni sui prodotti.
     */
    public InitServlet() {
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
     * Metodo di inizializzazione della servlet.
     *
     * Recupera la lista delle categorie e la memorizza nel contesto dell'applicazione
     * come attributo globale accessibile a tutte le servlet.
     *
     * @throws ServletException se si verifica un errore durante l'inizializzazione
     */
    @Override
    public void init() throws ServletException {
        super.init();
        List<Categoria> categorie = infoProdottoService.getAllCategorie();
        getServletContext().setAttribute("categorie", categorie);
    }
}
