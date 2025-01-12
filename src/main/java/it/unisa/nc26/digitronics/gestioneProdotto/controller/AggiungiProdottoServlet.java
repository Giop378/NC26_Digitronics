package it.unisa.nc26.digitronics.gestioneProdotto.controller;

import it.unisa.nc26.digitronics.gestioneProdotto.service.GestioneProdottoService;
import it.unisa.nc26.digitronics.gestioneProdotto.service.GestioneProdottoServiceImpl;
import it.unisa.nc26.digitronics.model.bean.Prodotto;
import it.unisa.nc26.digitronics.model.bean.Utente;
import it.unisa.nc26.digitronics.utils.MyServletException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * Servlet per aggiungere un nuovo prodotto al sistema.
 * Gestisce l'invio dei dati del prodotto e il caricamento di un'immagine associata al prodotto.
 * Si occupa della validazione dei dati, del controllo delle estensioni e dimensioni del file,
 * e dell'inserimento del prodotto nel database tramite il servizio {@link GestioneProdottoService}.
 *
 * <p>La servlet supporta il caricamento di file multimediali, come immagini, con estensioni specifiche
 * (JPG, JPEG, PNG, GIF) e dimensioni fino a 10 MB. Se il file ha lo stesso nome di uno già presente,
 * il nome viene modificato per evitare conflitti.</p>
 *
 * <p>Solo gli utenti con il ruolo appropriato (amministratori) possono accedere a questa funzionalità.</p>
 *
 * <p>In caso di errore nel caricamento del file o nella validazione dei dati, l'utente viene reindirizzato
 * a una pagina di errore. Se l'inserimento del prodotto è riuscito, viene mostrato un messaggio di conferma.</p>
 *
 * @see GestioneProdottoService
 * @see Prodotto
 * @see MyServletException
 */
@WebServlet("/add-product-servlet")
@MultipartConfig
public class AggiungiProdottoServlet extends HttpServlet {

    /**
     * Cartella in cui vengono salvati i file caricati (immagini dei prodotti).
     */
    private static final String CARTELLA_UPLOAD = "./images/prodotti";

    /**
     * Elenco delle estensioni di file supportate per l'upload delle immagini.
     */
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif");

    /**
     * Limite massimo della dimensione del file caricato (in byte).
     * Il valore è impostato a 10MB.
     */
    private static final long MAX_FILE_SIZE = 1024 * 1024 * 10; // 10 MB

    /**
     * Servizio di gestione dei prodotti che si occupa della logica di validazione e salvataggio nel database.
     */
    private GestioneProdottoService modificaProdottoService;

    /**
     * Costruttore di default che inizializza il servizio di gestione dei prodotti.
     */
    public AggiungiProdottoServlet() {
        this.modificaProdottoService = new GestioneProdottoServiceImpl();
    }

    /**
     * Imposta il servizio di gestione dei prodotti. Utilizzato per iniettare un servizio esterno, se necessario.
     *
     * @param modifyProductService il servizio di gestione dei prodotti
     */
    public void setModifyProductService(GestioneProdottoService modifyProductService) {
        this.modificaProdottoService = modifyProductService;
    }

    /**
     * Gestisce una richiesta HTTP di tipo POST per l'aggiunta di un nuovo prodotto.
     * Verifica la validità del file immagine caricato, la validità dei parametri del prodotto
     * e tenta di aggiungere il prodotto nel database.
     *
     * <p>In caso di successo, il prodotto viene inserito nel database e l'utente viene reindirizzato
     * alla pagina di conferma dell'operazione. In caso di errore, l'utente viene reindirizzato alla pagina
     * di errore con un messaggio appropriato.</p>
     *
     * @param request la richiesta HTTP
     * @param response la risposta HTTP
     * @throws ServletException se si verifica un errore durante l'esecuzione della servlet
     * @throws IOException se si verifica un errore nell'elaborazione dei dati della richiesta
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Recupera la sessione utente e verifica il ruolo
        HttpSession session = request.getSession();
        Utente utente = (Utente) session.getAttribute("utente");
        if (utente == null || !utente.isRuolo()) {
            throw new MyServletException("Non puoi accedere a questa pagina");
        }

        // Recupera il file caricato dalla richiesta
        Part filePart = request.getPart("file");
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

        // Verifica l'estensione del file caricato
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (!ALLOWED_EXTENSIONS.contains(fileExtension.toLowerCase())) {
            request.setAttribute("errorMessage", "Tipo di file non supportato. Carica un file con estensione JPG, JPEG, PNG o GIF.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/results/general-error.jsp");
            dispatcher.forward(request, response);
            return;
        }

        // Verifica la dimensione del file caricato
        if (filePart.getSize() > MAX_FILE_SIZE) {
            request.setAttribute("errorMessage", "Dimensione del file superiore al limite consentito (10MB).");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/results/general-error.jsp");
            dispatcher.forward(request, response);
            return;
        }

        // Gestisce il path di destinazione del file
        String destinazione = CARTELLA_UPLOAD + File.separator + fileName;
        Path pathDestinazione = Paths.get(getServletContext().getRealPath(destinazione));

        // Rinomina il file se esiste già un file con lo stesso nome
        for (int i = 2; Files.exists(pathDestinazione); i++) {
            destinazione = CARTELLA_UPLOAD + File.separator + i + "_" + fileName;
            pathDestinazione = Paths.get(getServletContext().getRealPath(destinazione));
        }

        // Scrive il file nella destinazione specificata
        try (InputStream fileInputStream = filePart.getInputStream()) {
            Files.createDirectories(pathDestinazione.getParent());
            Files.copy(fileInputStream, pathDestinazione);
        } catch (IOException e) {
            request.setAttribute("errorMessage", "Errore durante il caricamento del file. Riprova più tardi.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/results/general-error.jsp");
            dispatcher.forward(request, response);
            return;
        }

        // Crea e valida i dati del prodotto
        try {
            String nome = request.getParameter("nome");
            String descrizione = request.getParameter("descrizione");
            double prezzo = Double.parseDouble(request.getParameter("prezzo"));
            boolean inVetrina = request.getParameter("vetrina") != null;
            int quantita = Integer.parseInt(request.getParameter("quantita"));
            String nomeCategoria = request.getParameter("nomecategoria");

            Prodotto nuovoProdotto = new Prodotto();
            nuovoProdotto.setImmagine(destinazione);
            nuovoProdotto.setNome(nome);
            nuovoProdotto.setDescrizione(descrizione);
            nuovoProdotto.setPrezzo(prezzo);
            nuovoProdotto.setQuantità(quantita);
            nuovoProdotto.setNomeCategoria(nomeCategoria);
            nuovoProdotto.setVetrina(inVetrina);

            // Aggiunge il prodotto nel database
            modificaProdottoService.aggiungiProdotto(nuovoProdotto);

            // Reindirizza alla pagina di conferma
            String successMessage = "Prodotto aggiunto con successo";
            request.setAttribute("successMessage", successMessage);
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("WEB-INF/results/confirm-operation.jsp");
            requestDispatcher.forward(request, response);
        } catch (Exception e) {
            // Gestisce eventuali errori e reindirizza alla pagina di errore
            request.setAttribute("errorMessage", e.getMessage());
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/results/general-error.jsp");
            dispatcher.forward(request, response);
        }
    }
}

