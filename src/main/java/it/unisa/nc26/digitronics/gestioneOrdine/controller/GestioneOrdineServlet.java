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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {
        HttpSession session = request.getSession();

        //Se l'utente è un admin o non è registrato non possono accedere alle funzionalità presenti nella servlet
        Utente utenteSession = (Utente) request.getSession().getAttribute("utente");
        List<ItemCarrello> carrelloSession = (List<ItemCarrello>) session.getAttribute("carrello");
        if (utenteSession == null) {
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/results/login.jsp");
            requestDispatcher.forward(request, response);
        } else if (utenteSession.isRuolo()) {
            throw new MyServletException("Non puoi accedere a questa funzionalità");
        } else if (carrelloSession.isEmpty() || carrelloSession == null) {
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/results/cart.jsp");
            requestDispatcher.forward(request, response);
        } else {
            //Se l'utente è registrato può fare varie operazioni che saranno riconosciute dal path
            String path = request.getServletPath();

            double prezzoTotale;
            switch (path) {
                //Caso in cui dal carrello si vuole fare il checkout, bisogna controllare che effettivamente i prodotti sono presenti
                case "/inizio-checkout":
                    //Calcolo il prezzo totale (spedizione esclusa)
                    prezzoTotale = 0;
                    for (ItemCarrello c : carrelloSession) {
                        int idProdotto = c.getIdProdotto();
                        //devo controllare se la quantità inserita nel carrello dall'utente è effettivamente presente
                        int quantitàNelCarrello = c.getQuantità();
                        Prodotto prodotto = gestioneOrdineService.fetchByIdProdotto(idProdotto);

                        if (quantitàNelCarrello > prodotto.getQuantità()) {
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
                    String nome, cognome, via, città, telefono, numeroCarta, nomeIntestatario, scadenzaCarta, cvv, cap, numeroCivico;
                    MetodoSpedizione metodoSpedizione;

                    nome = request.getParameter("nome");
                    cognome = request.getParameter("cognome");
                    via = request.getParameter("via");
                    numeroCivico = request.getParameter("numerocivico");
                    cap = request.getParameter("cap");
                    città = request.getParameter("città");
                    telefono = request.getParameter("telefono");
                    numeroCarta = request.getParameter("numeroCarta");
                    nomeIntestatario = request.getParameter("nomeIntestatario");
                    scadenzaCarta = request.getParameter("scadenzaCarta");
                    cvv = request.getParameter("cvv");
                    metodoSpedizione = gestioneOrdineService.fetchMetodoSpedizioneByNome(request.getParameter("metodoSpedizione"));

                    if (nome == null || cognome == null || via == null || telefono == null || metodoSpedizione == null || città == null ||
                            cap == null || numeroCivico == null || numeroCarta == null || nomeIntestatario == null ||
                            scadenzaCarta == null || cvv == null) {
                        throw new MyServletException("Tutti i campi sono obbligatori");
                    }

                    // Controllo dei formati per i campi della spedizione
                    if (!nome.matches("^(?!\\s*$)[a-zA-Zà-ÿÀ-Ÿ\\s']{1,255}$")) {
                        throw new MyServletException("Nome non valido. Deve contenere solo lettere, spazi e apostrofi, con una lunghezza massima di 255 caratteri.");
                    }

                    if (!cognome.matches("^(?!\\s*$)[a-zA-Zà-ÿÀ-Ÿ\\s']{1,255}$")) {
                        throw new MyServletException("Cognome non valido. Deve contenere solo lettere, spazi e apostrofi, con una lunghezza massima di 255 caratteri.");
                    }

                    if (!via.matches("^.{1,255}$")) {
                        throw new MyServletException("La via deve contenere tra 1 e 255 caratteri.");
                    }

                    if (!cap.matches("^\\d{5}$")) {
                        throw new MyServletException("CAP non valido. Deve contenere esattamente 5 caratteri numerici");
                    }

                    if (!città.matches("^(?!\\s*$)[a-zA-Zà-ÿÀ-Ÿ\\s']{1,255}$")) {
                        throw new MyServletException("Il nome della città non è valido. Deve contenere solo lettere, spazi e apostrofi, con una lunghezza massima di 255 caratteri.");
                    }

                    if (!numeroCivico.matches("^\\d{1,5}(\\s?(bis|tris|[a-zA-Z]))?$")) {
                        throw new MyServletException("Numero civico non valido.");
                    }

                    if (!telefono.matches("^\\+?[1-9]\\d{1,14}$")) {
                        throw new MyServletException("Numero di telefono non valido.");
                    }

                    // Controllo dei formati per i campi della carta di credito
                    if (!nomeIntestatario.matches("^(?!\\s*$)[a-zA-Zà-ÿÀ-Ÿ\\s']{1,255}$")) {
                        throw new MyServletException("Nome dell'intestatario della carta non valido.Deve contenere solo lettere, spazi e apostrofi, con una lunghezza massima di 255 caratteri.");
                    }
                    if (!cvv.matches("^[0-9]{3}$")) {
                        throw new MyServletException("CVV non valido. Deve essere di 3 cifre numeriche.");
                    }
                    //Controlli necessari per il numero della carta
                    if (!numeroCarta.replaceAll(" ", "").matches("^[0-9]{16}$")) {
                        throw new MyServletException("Numero della carta di credito non valido. Deve essere di 16 cifre.");
                    }
                    // Controlli necessari per la data di scadenza
                    if (!scadenzaCarta.matches("^(0[1-9]|1[0-2])/[0-9]{2}$")) {
                        throw new MyServletException("Data di scadenza della carta non valida. Deve essere nel formato MM/YY.");
                    }

                    // Parsing della data di scadenza
                    String[] scadenzaParts = scadenzaCarta.split("/");
                    int meseScadenza = Integer.parseInt(scadenzaParts[0]);
                    int annoScadenza = Integer.parseInt(scadenzaParts[1]) + 2000; // Aggiungo 2000 per ottenere l'anno completo

                    LocalDate oggi = LocalDate.now();
                    LocalDate dataScadenza = LocalDate.of(annoScadenza, meseScadenza, 1).plusMonths(1).minusDays(1);

                    if (dataScadenza.isBefore(oggi)) {
                        throw new MyServletException("La carta di credito è scaduta.");
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
                    }else {//Caso in cui l'indirizzo esiste, verificare e confermare l'ordine
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
                            for (ItemCarrello itemCarrello : carrelloSession) {//Ciclo for per verificare se i prodotti da ordinare sono disponibili
                                int idProdotto = itemCarrello.getIdProdotto();
                                Prodotto prodotto = gestioneOrdineService.fetchByIdProdotto(idProdotto);

                                // Controllo se c'è qualche prodotto da ordinare la cui quantità è maggiore di quella nel magazzino
                                if (itemCarrello.getQuantità() > prodotto.getQuantità()) {
                                    throw new MyServletException("Prodotto non più disponibile nelle quantità inserite dall'utente, ordine annullato");
                                }
                            }
                            for(ItemCarrello itemCarrello : carrelloSession){//Ciclo for per togliere la quantità relativa all'ordine
                                int idProdotto = itemCarrello.getIdProdotto();
                                Prodotto prodotto = gestioneOrdineService.fetchByIdProdotto(idProdotto);

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
