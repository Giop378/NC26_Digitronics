
document.addEventListener('DOMContentLoaded', function() {
    var searchInput = document.querySelector('.search-bar');
    var searchResults = document.querySelector('.search-results');

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

    function displayResults(results) {
        clearResults();

        var resultsContainer = document.createElement('div');
        resultsContainer.classList.add('search-results');

        var numResults = Math.min(results.length, 4); // Limita a massimo 4 risultati

        if (numResults > 0) {
            for (var i = 0; i < numResults; i++) {
                var prodotto = results[i];

                var resultItem = document.createElement('div');
                resultItem.classList.add('result-item');

                var link = document.createElement('a');
                link.href = 'dettagliProdotto?id=' + prodotto.idProdotto;
                link.classList.add('product-link');

                var img = document.createElement('img');
                img.src = prodotto.immagine;
                img.alt = prodotto.nome;
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

                resultsContainer.appendChild(resultItem);
            }
        } else {
            displayNoResults();
        }

        searchResults.appendChild(resultsContainer);
    }

    function displayNoResults() {
        clearResults();

        var noResultsMessage = document.createElement('p');
        noResultsMessage.textContent = 'Nessun prodotto trovato.';
        noResultsMessage.classList.add('no-results-message'); // Aggiungi la classe per il colore del testo
        searchResults.appendChild(noResultsMessage);
    }

    function clearResults() {
        // Rimuovi tutti gli elementi figli da searchResults
        while (searchResults.firstChild) {
            searchResults.removeChild(searchResults.firstChild);
        }
    }

    function formatCurrency(amount) {
        return (amount).toFixed(2).replace('.', ',') + ' €';
    }
});
