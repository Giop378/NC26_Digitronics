package it.unisa.nc26.digitronics.gestioneOrdine.service;

import it.unisa.nc26.digitronics.model.bean.ItemOrdine;
import it.unisa.nc26.digitronics.model.bean.MetodoSpedizione;
import it.unisa.nc26.digitronics.model.bean.Ordine;
import it.unisa.nc26.digitronics.model.bean.Prodotto;

import java.util.List;

public interface GestioneOrdineService {
    Prodotto fetchByIdProdotto(int idProdotto);
    List<MetodoSpedizione> fetchAllMetodiSpedizione();
    MetodoSpedizione fetchMetodoSpedizioneByNome(String nome);
    int saveOrdine(Ordine ordine);
    void updateQuantitàProdotto(Prodotto prodotto);
    void rimuoviCarrelloServletByIdUtente(int idUtente);
    void saveItemOrdine(ItemOrdine itemOrdine);
    boolean verificaIndirizzo(String via, String cap, String città);
    List<Ordine> fetchByIdUtente(int idUtente);
    List<Ordine> fetchAllOrders();
    List<ItemOrdine> fetchItemOrder(int idOrdine);
    Ordine fetchByIdOrder(int idOrdine);
}