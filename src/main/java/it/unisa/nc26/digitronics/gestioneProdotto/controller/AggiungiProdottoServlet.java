package it.unisa.nc26.digitronics.gestioneProdotto.controller;

import it.unisa.nc26.digitronics.gestioneProdotto.service.ModificaProdottoService;
import it.unisa.nc26.digitronics.gestioneProdotto.service.ModificaProdottoServiceImpl;
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

@WebServlet("/add-product-servlet")
@MultipartConfig
public class AggiungiProdottoServlet extends HttpServlet {
    private static final String CARTELLA_UPLOAD = "./images/prodotti";
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif");
    private static final long MAX_FILE_SIZE = 1024 * 1024 * 10; // 10 MB
    private ModificaProdottoService modificaProdottoService;

    public AggiungiProdottoServlet() {
        this.modificaProdottoService = new ModificaProdottoServiceImpl();
    }

    public void setModifyProductService(ModificaProdottoService modifyProductService) {
        this.modificaProdottoService = modifyProductService;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Utente utente = (Utente) session.getAttribute("utente");
        if (utente == null || !utente.isRuolo()) {
            throw new MyServletException("Non puoi accedere a questa pagina");
        }

        // Controllo per il file caricato
        Part filePart = request.getPart("file");
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

        // Verifica dell'estensione del file
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (!ALLOWED_EXTENSIONS.contains(fileExtension.toLowerCase())) {
            request.setAttribute("errorMessage", "Tipo di file non supportato. Carica un file con estensione JPG, JPEG, PNG o GIF.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/results/general-error.jsp");
            dispatcher.forward(request, response);
            return;
        }

        // Verifica della dimensione del file
        if (filePart.getSize() > MAX_FILE_SIZE) {
            request.setAttribute("errorMessage", "Dimensione del file superiore al limite consentito (10MB).");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/results/general-error.jsp");
            dispatcher.forward(request, response);
            return;
        }

        // Path di destinazione del file
        String destinazione = CARTELLA_UPLOAD + File.separator + fileName;
        Path pathDestinazione = Paths.get(getServletContext().getRealPath(destinazione));

        // Rinomina il file se esiste già un file con lo stesso nome
        for (int i = 2; Files.exists(pathDestinazione); i++) {
            destinazione = CARTELLA_UPLOAD + File.separator + i + "_" + fileName;
            pathDestinazione = Paths.get(getServletContext().getRealPath(destinazione));
        }

        // Scrive il file nella destinazione
        try (InputStream fileInputStream = filePart.getInputStream()) {
            Files.createDirectories(pathDestinazione.getParent());
            Files.copy(fileInputStream, pathDestinazione);
        } catch (IOException e) {
            request.setAttribute("errorMessage", "Errore durante il caricamento del file. Riprova più tardi.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/results/general-error.jsp");
            dispatcher.forward(request, response);
            return;
        }

        // Creazione prodotto e validazione dei dati
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

            // Delegazione al service per la logica di validazione e salvataggio
            modificaProdottoService.aggiungiProdotto(nuovoProdotto); // Aggiungi il prodotto nel DB

            // Inoltra la richiesta al risultato della pagina JSP
            String successMessage = "Prodotto aggiunto con successo";
            request.setAttribute("successMessage", successMessage);
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("WEB-INF/results/confirm-operation.jsp");
            requestDispatcher.forward(request, response);
        } catch (Exception e) {
            // Gestione errori
            request.setAttribute("errorMessage", e.getMessage());
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/results/general-error.jsp");
            dispatcher.forward(request, response);
        }
    }
}

