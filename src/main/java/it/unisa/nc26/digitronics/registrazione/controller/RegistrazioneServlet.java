package it.unisa.nc26.digitronics.registrazione.controller;

import it.unisa.nc26.digitronics.model.bean.ItemCarrello;
import it.unisa.nc26.digitronics.model.bean.Utente;
import it.unisa.nc26.digitronics.registrazione.service.RegistrazioneServiceImpl;
import it.unisa.nc26.digitronics.utils.MyServletException;
import it.unisa.nc26.digitronics.registrazione.service.RegistrazioneService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet per gestire il processo di registrazione degli utenti.
 */
@WebServlet("/registrazione-servlet")
public class RegistrazioneServlet extends HttpServlet {

    private RegistrazioneService registrazioneService;

    /**
     * Costruttore di default. Inizializza un'istanza di RegistrazioneServiceImpl.
     */
    public RegistrazioneServlet() {
        this.registrazioneService = new RegistrazioneServiceImpl();
    }

    /**
     * Imposta un'istanza personalizzata di RegistrazioneService.
     *
     * @param registrazioneService L'istanza di RegistrazioneService da impostare
     */
    public void setRegistrazioneService(RegistrazioneService registrazioneService) {
        this.registrazioneService = registrazioneService;
    }

    /**
     * Metodo per gestire richieste GET.
     * Verifica se un utente è già autenticato nella sessione. Se non lo è, gestisce il processo di registrazione.
     *
     * @param request  La richiesta HTTP
     * @param response La risposta HTTP
     * @throws ServletException Se si verifica un errore nella gestione della servlet
     * @throws IOException      Se si verifica un errore di I/O
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Utente utenteSession = (Utente) request.getSession().getAttribute("utente");
        // Controlla se l'utente è già autenticato
        if (utenteSession != null) {
            if (utenteSession.isRuolo()) {
                RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/results/admin.jsp");
                requestDispatcher.forward(request, response);
            } else {
                RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/results/profile.jsp");
                requestDispatcher.forward(request, response);
            }
        }

        // Gestione della registrazione
        String nome = request.getParameter("nome");
        String cognome = request.getParameter("cognome");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        LocalDate dataDiNascita;
        try {
            dataDiNascita = LocalDate.parse(request.getParameter("datadinascita"));
        } catch (Exception ex) {
            throw new MyServletException("Data di nascita non valida");
        }
        Boolean admin = false;

        // Validazione dei dati di input
        if (nome == null || !nome.matches("[a-zA-Z ]+") ||
                cognome == null || !cognome.matches("[a-zA-Z ]+") ||
                email == null || !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$") ||
                password == null || password.length() < 8) {
            throw new MyServletException("Dati in input non validi");
        }

        Utente utente = new Utente();
        utente.setNome(nome);
        utente.setCognome(cognome);
        utente.setEmail(email);
        utente.setPasswordHash(password);
        utente.setDataDiNascita(dataDiNascita);
        utente.setRuolo(admin); // L'utente viene impostato come utente registrato

        int idUtente = registrazioneService.registraUtente(utente);

        // Imposta l'id dell'utente nella sessione
        utente.setIdUtente(idUtente);
        request.getSession().setAttribute("utente", utente);

        // Aggiorna l'id degli elementi nel carrello
        List<ItemCarrello> carrelloSession = (List<ItemCarrello>) request.getSession().getAttribute("carrello");
        if(carrelloSession == null){
            carrelloSession = new ArrayList<ItemCarrello>();
        }
        for (ItemCarrello carrello : carrelloSession) {
            carrello.setIdUtente(utente.getIdUtente());
        }
        request.getSession().setAttribute("carrello", carrelloSession);

        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/results/profile.jsp");
        requestDispatcher.forward(request, response);
    }

    /**
     * Metodo per gestire richieste POST.
     * Redirige al metodo doGet per la gestione.
     *
     * @param request  La richiesta HTTP
     * @param response La risposta HTTP
     * @throws ServletException Se si verifica un errore nella gestione della servlet
     * @throws IOException      Se si verifica un errore di I/O
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}

