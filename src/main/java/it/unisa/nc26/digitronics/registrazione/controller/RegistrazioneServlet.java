package it.unisa.nc26.digitronics.registrazione.controller;

import it.unisa.nc26.digitronics.model.bean.ItemCarrello;
import it.unisa.nc26.digitronics.model.bean.Utente;
import it.unisa.nc26.digitronics.registrazione.service.RegistrazioneServiceImpl;
import it.unisa.nc26.digitronics.utils.MyServletException;
import it.unisa.nc26.digitronics.registrazione.service.RegistrazioneService;
import it.unisa.nc26.digitronics.registrazione.service.RegistrazioneServiceImpl;
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

@WebServlet("/registrazione-servlet")
public class RegistrazioneServlet extends HttpServlet {

    private RegistrazioneService registrazioneService;

    public RegistrazioneServlet() {
        this.registrazioneService = new RegistrazioneServiceImpl();
    }

    public void setRegistrazioneService(RegistrazioneService registrazioneService) {
        this.registrazioneService = registrazioneService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Utente utenteSession = (Utente) request.getSession().getAttribute("utente");
        //se c'è già un utente nella sessione non c'è bisogno della registrazione quindi si va o all'area admin o all'area profilo utente
        if (utenteSession != null) {
            if (utenteSession.isRuolo()) {
                RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/results/admin.jsp");
                requestDispatcher.forward(request, response);
            } else {
                RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/results/profile.jsp");
                requestDispatcher.forward(request, response);
            }
        }
        //Ora si occupa della vera e propria registrazione
        String nome = request.getParameter("nome");
        String cognome = request.getParameter("cognome");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        LocalDate dataDiNascita;
        try {
            dataDiNascita = LocalDate.parse(request.getParameter("datadinascita"));
        }catch (Exception ex){
            throw new MyServletException("Data di nascita non valida");
        }
        Boolean admin = false;

        //controllo i dati in input
        if (nome == null || !nome.matches("[a-zA-Z ]+")||
                cognome == null || !cognome.matches("[a-zA-Z ]+")||
                email == null || !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")||
                password == null || password.length() < 8 ) {
            throw new MyServletException("Dati in input non validi");
        }

        Utente utente = new Utente();
        utente.setNome(nome);
        utente.setCognome(cognome);
        utente.setEmail(email);
        utente.setPasswordHash(password);
        utente.setDataDiNascita(dataDiNascita);
        utente.setRuolo(admin);//In questo modo l'utente viene settato in modo automatico in utente registrato


        int idUtente = registrazioneService.registraUtente(utente);

        //Setto l'id dell'utente per salvarlo nella sessione
        utente.setIdUtente(idUtente);

        request.getSession().setAttribute("utente", utente);

        //Imposto l'id di tutti gli utenti nel carrello
        List<ItemCarrello> carrelloSession = (List<ItemCarrello>) request.getSession().getAttribute("carrello");
        if(carrelloSession == null){
            carrelloSession = new ArrayList<ItemCarrello>();
        }
        for(ItemCarrello carrello : carrelloSession){
            carrello.setIdUtente(utente.getIdUtente());
        }
        request.getSession().setAttribute("carrello", carrelloSession);

        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/results/profile.jsp");
        requestDispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
