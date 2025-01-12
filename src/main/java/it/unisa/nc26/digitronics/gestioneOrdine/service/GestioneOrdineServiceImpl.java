package it.unisa.nc26.digitronics.gestioneOrdine.service;

import it.unisa.nc26.digitronics.gestioneOrdine.service.IndirizzoAdapter.OpenStreetMapVerificaIndirizzoApiAdapterImpl;
import it.unisa.nc26.digitronics.gestioneOrdine.service.IndirizzoAdapter.VerificaIndirizzoApiAdapter;
import it.unisa.nc26.digitronics.model.bean.ItemOrdine;
import it.unisa.nc26.digitronics.model.bean.MetodoSpedizione;
import it.unisa.nc26.digitronics.model.bean.Ordine;
import it.unisa.nc26.digitronics.model.bean.Prodotto;
import it.unisa.nc26.digitronics.model.dao.*;
import it.unisa.nc26.digitronics.utils.MyServletException;

import java.util.List;

public class GestioneOrdineServiceImpl implements GestioneOrdineService{
    private ProdottoDAO prodottoDAO;
    private MetodoSpedizioneDAO metodoSpedizioneDAO;
    private OrdineDAO ordineDAO;
    private ItemCarrelloDAO itemCarrelloDAO;
    private ItemOrdineDAO itemOrdineDAO;
    private VerificaIndirizzoApiAdapter verificaIndirizzoApiAdapter;

    public GestioneOrdineServiceImpl() {
        this.prodottoDAO = new ProdottoDAO();
        this.metodoSpedizioneDAO = new MetodoSpedizioneDAO();
        this.ordineDAO = new OrdineDAO();
        this.itemCarrelloDAO = new ItemCarrelloDAO();
        this.itemOrdineDAO = new ItemOrdineDAO();
        this.verificaIndirizzoApiAdapter = new OpenStreetMapVerificaIndirizzoApiAdapterImpl();
    }

    public void setProdottoDAO(ProdottoDAO prodottoDAO) {
        this.prodottoDAO = prodottoDAO;
    }

    public void setMetodoSpedizioneDAO(MetodoSpedizioneDAO metodoSpedizioneDAO) {
        this.metodoSpedizioneDAO = metodoSpedizioneDAO;
    }

    public void setOrdineDAO(OrdineDAO ordineDAO) {
        this.ordineDAO = ordineDAO;
    }

    public void setItemCarrelloDAO(ItemCarrelloDAO itemCarrelloDAO) {
        this.itemCarrelloDAO = itemCarrelloDAO;
    }

    public void setItemOrdineDAO(ItemOrdineDAO itemOrdineDAO) {
        this.itemOrdineDAO = itemOrdineDAO;
    }

    public void setVerificaIndirizzoApiAdapter(VerificaIndirizzoApiAdapter verificaIndirizzoApiAdapter) {
        this.verificaIndirizzoApiAdapter = verificaIndirizzoApiAdapter;
    }

    @Override
    public Prodotto fetchByIdProdotto(int idProdotto) throws MyServletException {
        Prodotto prodotto = prodottoDAO.doRetrieveById(idProdotto);
        if (prodotto == null) {
            throw new MyServletException("Prodotto con ID " + idProdotto + " non esistente.");
        }
        return prodotto;
    }

    @Override
    public List<MetodoSpedizione> fetchAllMetodiSpedizione() {
        return metodoSpedizioneDAO.doRetrieveAll();
    }

    @Override
    public MetodoSpedizione fetchMetodoSpedizioneByNome(String nome) {
        return metodoSpedizioneDAO.doRetrieveByName(nome);
    }

    @Override
    public int saveOrdine(Ordine ordine) {
        return ordineDAO.doSave(ordine);
    }

    @Override
    public void updateQuantitàProdotto(Prodotto prodotto) {
        prodottoDAO.doUpdate(prodotto);
    }

    @Override
    public void rimuoviCarrelloServletByIdUtente(int idUtente) {
        itemCarrelloDAO.doDelete(idUtente);
    }

    @Override
    public void saveItemOrdine(ItemOrdine itemOrdine) {
        itemOrdineDAO.doSave(itemOrdine);
    }

    @Override
    public boolean verificaIndirizzo(String via, String cap, String città) {
        return verificaIndirizzoApiAdapter.verifica(via, cap, città);
    }

    @Override
    public List<Ordine> fetchByIdUtente(int idUtente) {
        return ordineDAO.doRetrieveByCustomer(idUtente);
    }

    @Override
    public List<Ordine> fetchAllOrders() {
        return ordineDAO.doRetrieveAll();
    }

    @Override
    public List<ItemOrdine> fetchItemOrder(int idOrdine) throws MyServletException {
        List<ItemOrdine> itemsOrdine = itemOrdineDAO.doRetrieveByOrdine(idOrdine);
        if (itemsOrdine.isEmpty()) {
            throw new MyServletException("Nessun elemento trovato per l'ordine con ID " + idOrdine + ".");
        }
        return itemsOrdine;
    }

    //Prende l'ordine
    @Override
    public Ordine fetchByIdOrder(int idOrdine) throws MyServletException {
        Ordine ordine = ordineDAO.doRetrieveByIdOrder(idOrdine);
        if (ordine == null) {
            throw new MyServletException("Ordine con ID " + idOrdine + " non esistente.");
        }
        return ordine;
    }

}
