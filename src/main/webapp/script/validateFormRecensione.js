 function validateForm(event) {
    // Riferimenti agli elementi
    const title = document.getElementById("title");
    const description = document.getElementById("description");
    const score = document.getElementById("score");

    // Riferimenti agli elementi di avviso
    const titleWarning = document.getElementById("title-warning");
    const descriptionWarning = document.getElementById("description-warning");
    const scoreWarning = document.getElementById("score-warning");

    // Limiti
    const maxTitleLength = 255;
    const minScore = 1;
    const maxScore = 5;

    let isValid = true;

    // Validazione del titolo (non vuoto e lunghezza massima)
    if (title.value.trim() === "") {
    titleWarning.innerText = "Il titolo non può essere vuoto.";
    titleWarning.style.display = "block";
    isValid = false;
} else if (title.value.length > maxTitleLength) {
    titleWarning.innerText = "Il titolo non può superare i 255 caratteri.";
    titleWarning.style.display = "block";
    isValid = false;
} else {
    titleWarning.style.display = "none";
}

    // Validazione della descrizione (non vuota)
    if (description.value.trim() === "") {
    descriptionWarning.innerText = "La descrizione non può essere vuota.";
    descriptionWarning.style.display = "block";
    isValid = false;
} else {
    descriptionWarning.style.display = "none";
}

    // Validazione del punteggio (compreso tra 1 e 5)
    const scoreValue = parseFloat(score.value);
    if (isNaN(scoreValue) || scoreValue < minScore || scoreValue > maxScore) {
    scoreWarning.innerText = "Il punteggio deve essere compreso tra 1 e 5.";
    scoreWarning.style.display = "block";
    isValid = false;
} else {
    scoreWarning.style.display = "none";
}

    // Se non valido, impedisci l'invio del modulo
    if (!isValid) {
    event.preventDefault();
}
}
