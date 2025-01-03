package it.unisa.nc26.digitronics.gestioneProdotto.service;

import it.unisa.nc26.digitronics.model.bean.Prodotto;
import it.unisa.nc26.digitronics.model.dao.CategoriaDAO;
import it.unisa.nc26.digitronics.model.dao.ProdottoDAO;
import it.unisa.nc26.digitronics.utils.MyServletException;

public class ModificaProdottoServiceImpl implements ModificaProdottoService{
    private CategoriaDAO categoriaDAO;
    private ProdottoDAO prodottoDAO;

    public ModificaProdottoServiceImpl() {
        this.categoriaDAO = new CategoriaDAO();
        this.prodottoDAO = new ProdottoDAO();
    }

    public void setCategoriaDAO(CategoriaDAO categoriaDAO) {
        this.categoriaDAO = categoriaDAO;
    }

    public void setProdottoDAO(ProdottoDAO prodottoDAO) {
        this.prodottoDAO = prodottoDAO;
    }

    @Override
    public void modificaProdotto(Prodotto prodottoModificato) throws Exception {
        // Verifica che il prodotto esista
        Prodotto prodottoEsistente = prodottoDAO.doRetrieveById(prodottoModificato.getIdProdotto());
        if (prodottoEsistente == null) {
            throw new MyServletException("Prodotto non esistente");
        }

        // Verifica che la categoria esista
        if (categoriaDAO.doRetrieveByNomeCategoria(prodottoModificato.getNomeCategoria()) == null) {
            throw new MyServletException("Categoria non esistente");
        }

        // Controlli sui valori
        if (prodottoModificato.getNome() == null || prodottoModificato.getDescrizione() == null) {
            throw new MyServletException("Nome o descrizione non possono essere null");
        }
        if (prodottoModificato.getPrezzo() <= 0  || prodottoModificato.getQuantità() <= 0) {
            throw new MyServletException("Valori di prezzo, sconto o quantità non validi");
        }

        // Mantieni l'immagine se non fornita
        if (prodottoModificato.getImmagine() == null || prodottoModificato.getImmagine().isEmpty()) {
            prodottoModificato.setImmagine(prodottoEsistente.getImmagine());
        }

        // Aggiorna il prodotto nel database
        prodottoDAO.doUpdate(prodottoModificato);
    }
}

