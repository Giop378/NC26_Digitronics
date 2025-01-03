document.addEventListener('DOMContentLoaded', function() {
    var searchInput = document.querySelector('.search-bar');
    var searchResultsContainer = document.querySelector('.search-results');

    // Aggiungi un event listener per gestire l'input sulla barra di ricerca
    searchInput.addEventListener('input', function() {
        var query = searchInput.value.trim();

        // Se la query non è vuota, avvia la ricerca
        if (query !== '') {
            performSearch(query);
        } else {
            // Se la barra di ricerca è vuota, nascondi i risultati
            clearResults();
        }
    });

    // Funzione per eseguire la ricerca tramite AJAX
    function performSearch(query) {
        var xhr = new XMLHttpRequest();
        xhr.open('POST', 'search?query=' + encodeURIComponent(query), true);

        xhr.onreadystatechange = function() {
            if (xhr.readyState === 4) {
                if (xhr.status === 200) {
                    var results = JSON.parse(xhr.responseText);
                    displayResults(results);
                } else {
                    // Gestisci il caso in cui la ricerca non è riuscita (es. server error)
                    displayNoResults();
                }
            }
        };

        xhr.send();
    }

    // Funzione per visualizzare i risultati della ricerca
    function displayResults(results) {
        clearResults();

        var resultsContainer = document.createElement('div');
        resultsContainer.classList.add('search-results');

        if (results.length > 0) {
            // Limita i risultati a un massimo di 4
            results.slice(0, 4).forEach(function(prodotto) {
                var resultItem = document.createElement('div');
                resultItem.classList.add('result-item');

                var link = document.createElement('a');
                link.href = '#'; // Non si naviga realmente a un link
                link.classList.add('product-link');

                var img = document.createElement('img');
                img.alt = prodotto.nome;
                img.src = prodotto.immagine;
                resultItem.appendChild(img);

                var productInfo = document.createElement('div');
                productInfo.classList.add('product-info');

                var nome = document.createElement('p');
                nome.textContent = prodotto.nome;
                productInfo.appendChild(nome);

                var prezzo = document.createElement('p');
                prezzo.textContent = formatCurrency(prodotto.prezzo);
                productInfo.appendChild(prezzo);

                link.appendChild(productInfo);
                resultItem.appendChild(link);

                // Aggiungi un event listener per gestire il click sul risultato
                link.addEventListener('click', function(event) {
                    event.preventDefault(); // Previeni l'azione predefinita del link

                    // Popola il form con i dati del prodotto selezionato
                    populateForm(prodotto);
                });

                resultsContainer.appendChild(resultItem);
            });
        } else {
            displayNoResults();
        }

        searchResultsContainer.appendChild(resultsContainer);
    }

    // Funzione per popolare il form con i dati del prodotto selezionato
    function populateForm(prodotto) {
        document.getElementById('id').value = prodotto.idProdotto;
        document.getElementById('nome').value = prodotto.nome;
        document.getElementById('descrizione').value = prodotto.descrizione; // Aggiungi descrizione se presente nel prodotto
        document.getElementById('prezzo').value = prodotto.prezzo;
        document.getElementById('vetrina').checked = prodotto.vetrina;
        document.getElementById('quantita').value = prodotto.quantita;
        document.getElementById('categoria').value = prodotto.nomecategoria;

        // Seleziona l'opzione corrispondente alla categoria del prodotto
        var categoriaSelect = document.getElementById('categoria');
        for (var i = 0; i < categoriaSelect.options.length; i++) {
            if (categoriaSelect.options[i].value === prodotto.categoria) {
                categoriaSelect.selectedIndex = i;
                break;
            }
        }
    }

    // Funzione per gestire il caso in cui non ci sono risultati
    function displayNoResults() {
        clearResults();

        var noResultsMessage = document.createElement('p');
        noResultsMessage.textContent = 'Nessun prodotto trovato.';
        noResultsMessage.classList.add('no-results-message'); // Aggiungi la classe per il colore del testo
        searchResultsContainer.appendChild(noResultsMessage);
    }

    // Funzione per cancellare i risultati della ricerca precedente
    function clearResults() {
        // Rimuovi tutti gli elementi figli da searchResultsContainer
        while (searchResultsContainer.firstChild) {
            searchResultsContainer.removeChild(searchResultsContainer.firstChild);
        }
    }

    // Funzione per formattare il prezzo in valuta
    function formatCurrency(amount) {
        return (amount).toFixed(2).replace('.', ',') + ' €';
    }
});