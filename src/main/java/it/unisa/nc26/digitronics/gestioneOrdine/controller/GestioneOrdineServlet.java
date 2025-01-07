package it.unisa.nc26.digitronics.gestioneOrdine.controller;

import it.unisa.nc26.digitronics.gestioneOrdine.service.GestioneOrdineService;
import it.unisa.nc26.digitronics.gestioneOrdine.service.GestioneOrdineServiceImpl;
import it.unisa.nc26.digitronics.model.bean.*;
import it.unisa.nc26.digitronics.utils.MyServletException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = {"/inizio-checkout", "/procedi-al-pagamento"})
public class GestioneOrdineServlet extends HttpServlet {
    private GestioneOrdineService gestioneOrdineService;

    public GestioneOrdineServlet() {
        this.gestioneOrdineService = new GestioneOrdineServiceImpl();
    }

    public void setGestioneOrdineService(GestioneOrdineService gestioneOrdineService) {
        this.gestioneOrdineService = gestioneOrdineService;
    }

    protected void doGet (HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {
        HttpSession session = request.getSession();

        //Se l'utente è un admin o non è registrato non possono accedere alle funzionalità presenti nella servlet
        Utente utenteSession = (Utente) request.getSession().getAttribute("utente");
        List<ItemCarrello> carrelloSession = (List<ItemCarrello>) session.getAttribute("carrello");
        if(utenteSession==null){
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/results/login.jsp");
            requestDispatcher.forward(request, response);
        } else if(utenteSession.isRuolo()){
            throw new MyServletException("Non puoi accedere a questa funzionalità");
        }else if (carrelloSession.isEmpty() || carrelloSession==null ){
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/results/cart.jsp");
            requestDispatcher.forward(request, response);
        } else{
            //Se l'utente è registrato può fare varie operazioni che saranno riconosciute dal path
            String path = request.getServletPath();

            double prezzoTotale;
            switch (path) {
                //Caso in cui dal carrello si vuole fare il checkout, bisogna controllare che effettivamente i prodotti sono presenti
                case "/inizio-checkout":
                    //Calcolo il prezzo totale (spedizione esclusa)
                    prezzoTotale = 0;
                    for(ItemCarrello c : carrelloSession){
                        int idProdotto = c.getIdProdotto();
                        //devo controllare se la quantità inserita nel carrello dall'utente è effettivamente presente
                        int quantitàNelCarrello= c.getQuantità();
                        Prodotto prodotto = gestioneOrdineService.fetchByIdProdotto(idProdotto);

                        if(quantitàNelCarrello > prodotto.getQuantità()){
                            c.setQuantità(prodotto.getQuantità());
                            throw new MyServletException("Quantità selezionata del prodotto non presente in magazzino");
                        }
                        prezzoTotale = prezzoTotale + prodotto.getPrezzo() * quantitàNelCarrello;
                    }
                    request.setAttribute("prezzoTotale", prezzoTotale);

                    //Mi prendo dal database i vari metodi di pagamento e metodi di spedizione in modo da passarli alla jsp
                    List<MetodoSpedizione> metodiSpedizione = gestioneOrdineService.fetchAllMetodiSpedizione();
                    request.setAttribute("metodiSpedizione", metodiSpedizione);

                    RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/results/checkout.jsp");
                    requestDispatcher.forward(request, response);
                    break;
                case "/procedi-al-pagamento":
                    // Recupera i dettagli della spedizione dell'ordine dal form e controlla se sono corretti
                    String nome, cognome, via, città, telefono, numeroCarta, nomeIntestatario, scadenzaCarta, cvv, cap;
                    int numeroCivico;
                    MetodoSpedizione metodoSpedizione;

                    try {
                        nome = request.getParameter("nome");
                        cognome = request.getParameter("cognome");
                        via = request.getParameter("via");
                        String numeroCivicoStr = request.getParameter("numerocivico");
                        cap = request.getParameter("cap");
                        città = request.getParameter("città");
                        telefono = request.getParameter("telefono");
                        numeroCarta = request.getParameter("numeroCarta");
                        nomeIntestatario = request.getParameter("nomeIntestatario");
                        scadenzaCarta = request.getParameter("scadenzaCarta");
                        cvv = request.getParameter("cvv");
                        metodoSpedizione = gestioneOrdineService.fetchMetodoSpedizioneByNome(request.getParameter("metodoSpedizione"));

                        if (nome == null || cognome == null || via == null || telefono == null || metodoSpedizione == null || città == null ||
                                cap == null || numeroCivicoStr == null || numeroCarta == null || nomeIntestatario == null ||
                                scadenzaCarta == null || cvv == null) {
                            throw new MyServletException("Tutti i campi sono obbligatori");
                        }

                        // Controllo dei formati per i campi della spedizione
                        if (!nome.matches("[a-zA-Z ]+") || !cognome.matches("[a-zA-Z ]+")) {
                            throw new MyServletException("Nome o cognome non validi.");
                        }
                        if (!numeroCivicoStr.matches("[0-9]{1,4}") || !cap.matches("[0-9]{5}")) {
                            throw new MyServletException("Numero civico o CAP non validi.");
                        }
                        numeroCivico = Integer.parseInt(numeroCivicoStr);

                        if (telefono != null && !telefono.matches("[0-9]{9,15}")) {
                            throw new MyServletException("Numero di telefono non valido.");
                        }

                        // Controllo dei formati per i campi della carta di credito
                        if (!nomeIntestatario.matches("[a-zA-Z ]+")) {
                            throw new MyServletException("Nome dell'intestatario della carta non valido.");
                        }
                        if (!cvv.matches("[0-9]{3}")) {
                            throw new MyServletException("CVV non valido. Deve essere di 3 cifre.");
                        }
                        //Controlli necessari per il numero della carta
                        if (!numeroCarta.replaceAll(" ", "").matches("[0-9]{16}")) {
                            throw new MyServletException("Numero della carta di credito non valido. Deve essere di 16 cifre.");
                        }
                        //Controlli necessari per la data di scadenza
                    } catch (NumberFormatException ex) {
                        throw new MyServletException("Parametri in input non validi");
                    }


                    //Si verifica a questo punto che l'indirizzo di spedizione esista per davvero
                    boolean indirizzoVerificato = gestioneOrdineService.verificaIndirizzo(via, cap, città);
                    if(!indirizzoVerificato){
                        //Se l'indirizzo non è verificato si vede se i prodotti sono ancora nel magazzino
                        prezzoTotale =0;
                        for(ItemCarrello c : carrelloSession){
                            int idProdotto = c.getIdProdotto();
                            //devo controllare se la quantità inserita nel carrello dall'utente è effettivamente presente
                            int quantitàNelCarrello= c.getQuantità();
                            Prodotto prodotto = gestioneOrdineService.fetchByIdProdotto(idProdotto);

                            if(quantitàNelCarrello > prodotto.getQuantità()){
                                c.setQuantità(prodotto.getQuantità());
                                throw new MyServletException("Quantità selezionata del prodotto non presente in magazzino");
                            }
                            prezzoTotale = prezzoTotale + prodotto.getPrezzo() * quantitàNelCarrello;
                        }
                        request.setAttribute("prezzoTotale", prezzoTotale);
                        //Se i prodotti sono ancora nel magazzino si rimanda alla pagina di inserimento spedizione e pagamento checkout.jsp con i dati inseriti precedentemente
                        request.setAttribute("erroreIndirizzo", "L'indirizzo di spedizione inserito non è valido. Si prega di verificarlo.");
                        request.setAttribute("nome", nome);
                        request.setAttribute("cognome", cognome);
                        request.setAttribute("telefono", telefono);
                        metodiSpedizione = gestioneOrdineService.fetchAllMetodiSpedizione();
                        request.setAttribute("metodiSpedizione", metodiSpedizione);
                        requestDispatcher = request.getRequestDispatcher("/WEB-INF/results/checkout.jsp");
                        requestDispatcher.forward(request, response);
                    }else {//Caso in cui l'indirizzo esiste ed è possibile confermare l'ordine
                        // Crea un oggetto Ordine
                        Ordine ordine = new Ordine();
                        ordine.setIdUtente(utenteSession.getIdUtente());
                        ordine.setNome(nome);
                        ordine.setCognome(cognome);
                        ordine.setVia(via);
                        ordine.setNumeroCivico(numeroCivico);
                        ordine.setCap(cap);
                        ordine.setCittà(città);
                        ordine.setTelefono(telefono);
                        ordine.setDataOrdine(Date.valueOf(LocalDate.now()));
                        ordine.setNomeMetodoSpedizione(metodoSpedizione.getNome());
                        // Per calcolare il prezzo totale bisogna scorrere tutto il carrello e aggiungere anche il costo della spedizione
                        prezzoTotale = metodoSpedizione.getCosto();
                        synchronized (this) {
                            for (ItemCarrello itemCarrello : carrelloSession) {
                                int idProdotto = itemCarrello.getIdProdotto();
                                Prodotto prodotto = gestioneOrdineService.fetchByIdProdotto(idProdotto);

                                // Controllo se c'è qualche prodotto da ordinare la cui quantità è maggiore di quella nel magazzino
                                if (itemCarrello.getQuantità() > prodotto.getQuantità()) {
                                    throw new MyServletException("Prodotto non più disponibile nelle quantità inserite dall'utente");
                                }

                                // Diminuisco la quantità di prodotto presente in magazzino e aggiorno il prodotto
                                prodotto.setQuantità(prodotto.getQuantità() - itemCarrello.getQuantità());
                                gestioneOrdineService.updateQuantitàProdotto(prodotto);

                                prezzoTotale = prezzoTotale + prodotto.getPrezzo() * itemCarrello.getQuantità();
                            }
                        }
                        ordine.setTotale(prezzoTotale);

                        // Salva l'ordine nel database e recupera l'id dell'ordine appena salvato

                        int ordineId = gestioneOrdineService.saveOrdine(ordine);

                        // Converti il carrello in una lista di itemOrdine e salva ogni itemOrdine nel database
                        List<ItemOrdine> itemsOrdine = new ArrayList<>();
                        for (ItemCarrello itemCarrello : carrelloSession) {
                            int idProdotto = itemCarrello.getIdProdotto();
                            Prodotto prodotto = gestioneOrdineService.fetchByIdProdotto(idProdotto);
                            ItemOrdine itemOrdine = new ItemOrdine();
                            itemOrdine.setIdOrdine(ordineId);
                            itemOrdine.setNome(prodotto.getNome());
                            itemOrdine.setImmagine(prodotto.getImmagine());
                            itemOrdine.setPrezzo(prodotto.getPrezzo());
                            itemOrdine.setQuantità(itemCarrello.getQuantità());
                            itemOrdine.setIdProdotto(itemCarrello.getIdProdotto());
                            gestioneOrdineService.saveItemOrdine(itemOrdine);
                            itemsOrdine.add(itemOrdine);
                        }

                        // Svuota il carrello dopo l'invio dell'ordine
                        session.removeAttribute("carrello");

                        //Rimuove il carrello dal database
                        gestioneOrdineService.rimuoviCarrelloServletByIdUtente(utenteSession.getIdUtente());


                        request.setAttribute("ordine", ordine);
                        request.setAttribute("itemsOrdine", itemsOrdine);

                        // Reindirizza ad una pagina di conferma dell'ordine
                        requestDispatcher = request.getRequestDispatcher("/WEB-INF/results/confirm-order.jsp");
                        requestDispatcher.forward(request, response);
                    }
                    break;
                default:
                    throw new MyServletException("Funzionalità non esistente");
            }
        }
    }

    protected void doPost (HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {
        doGet(request, response);
    }
}
