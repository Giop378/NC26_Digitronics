function validateFormModificaProdotto(event) {
    // Riferimenti agli elementi
    const nome = document.getElementById("nome");
    const descrizione = document.getElementById("descrizione");
    const prezzoInput = document.getElementById("prezzo");
    const quantita = document.getElementById("quantita");
    const categoria = document.getElementById("categoria");

    // Riferimenti agli elementi di avviso
    const nomeWarning = document.getElementById("nome-warning");
    const descrizioneWarning = document.getElementById("descrizione-warning");
    const prezzoWarning = document.getElementById("prezzo-warning");
    const quantitaWarning = document.getElementById("quantita-warning");
    const categoriaWarning = document.getElementById("categoria-warning");

    // Limiti
    const maxNomeLength = 255;
    let isValid = true;

    // Validazione Nome
    if (nome.value.trim() === "") {
        nomeWarning.innerText = "Il Nome non può essere vuoto.";
        nomeWarning.style.display = "block";
        isValid = false;
    } else if (nome.value.length > maxNomeLength) {
        nomeWarning.innerText = `Il Nome non può superare i ${maxNomeLength} caratteri.`;
        nomeWarning.style.display = "block";
        isValid = false;
    } else {
        nomeWarning.style.display = "none";
    }

    // Validazione Descrizione
    if (descrizione.value.trim() === "") {
        descrizioneWarning.innerText = "La Descrizione non può essere vuota.";
        descrizioneWarning.style.display = "block";
        isValid = false;
    } else {
        descrizioneWarning.style.display = "none";
    }

    // Validazione Prezzo
    if (prezzoInput.value.trim() === "") {
        prezzoWarning.innerText = "Il Prezzo non deve essere vuoto.";
        prezzoWarning.style.display = "block";
        isValid = false;
    } else {
        const prezzoValue = parseFloat(prezzoInput.value);
        if (isNaN(prezzoValue) || prezzoValue <= 0) {
            prezzoWarning.innerText = "Il Prezzo deve essere un numero positivo.";
            prezzoWarning.style.display = "block";
            isValid = false;
        } else if (!/^\d+(\.\d{2})$/.test(prezzoInput.value)) {
            prezzoWarning.innerText = "Il Prezzo deve avere esattamente due cifre decimali (es. 10.00).";
            prezzoWarning.style.display = "block";
            isValid = false;
        } else {
            prezzoWarning.style.display = "none";
        }
    }

    // Validazione Quantità
    if (quantita.value.trim() === "") {
        quantitaWarning.innerText = "La Quantità non deve essere vuota.";
        quantitaWarning.style.display = "block";
        isValid = false;
    } else {
        const quantitaValue = parseInt(quantita.value, 10);
        if (isNaN(quantitaValue) || quantitaValue <= 0) {
            quantitaWarning.innerText = "La Quantità deve essere un numero intero maggiore di zero.";
            quantitaWarning.style.display = "block";
            isValid = false;
        } else {
            quantitaWarning.style.display = "none";
        }
    }

    // Se il form non è valido, impedisci l'invio
    if (!isValid) {
        event.preventDefault();
        return false;
    }
    return true;
}
