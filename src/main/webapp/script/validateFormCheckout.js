function validateCheckoutForm(event) {
    // Inizialmente consideriamo il form valido
    let isValid = true;

    // Reset di tutti i messaggi d'errore
    const errorFields = document.querySelectorAll('.error-message');
    errorFields.forEach(function(el) {
        el.innerText = '';
        el.style.display = 'none';
    });

    // REGEX e limiti per la validazione
    const nameRegex = /^[a-zA-Zà-ÿÀ-Ÿ\s']{1,255}$/;
    const viaRegex = /^.{1,255}$/;
    const numerocivicoRegex = /^\d{1,5}(\s?(bis|tris|[a-zA-Z]))?$/;
    const capRegex = /^\d{5}$/;
    const cittàRegex = /^[a-zA-Zà-ÿÀ-Ÿ\s']{1,255}$/;
    const telefonoRegex = /^\+?[1-9]\d{1,14}$/;
    const cardNumberRegex = /^(?:\d{4} ){3}\d{4}$/;
    const expirationRegex = /^(0[1-9]|1[0-2])\/[0-9]{2}$/;
    const cvvRegex = /^\d{3}$/;

    // Validazione dei campi della spedizione
    let nome = document.getElementById('nome');
    if (!nome.value.trim() || !nameRegex.test(nome.value.trim())) {
        document.getElementById('nome-error').innerText = "Inserisci un nome valido (solo lettere, spazi o apostrofi, massimo 255 caratteri).";
        document.getElementById('nome-error').style.display = "block";
        isValid = false;
    }

    let cognome = document.getElementById('cognome');
    if (!cognome.value.trim() || !nameRegex.test(cognome.value.trim())) {
        document.getElementById('cognome-error').innerText = "Inserisci un cognome valido (solo lettere, spazi o apostrofi, massimo 255 caratteri).";
        document.getElementById('cognome-error').style.display = "block";
        isValid = false;
    }

    let via = document.getElementById('via');
    if (!via.value.trim() || !viaRegex.test(via.value.trim())) {
        document.getElementById('via-error').innerText = "Inserisci una via valida (da 1 a 255 caratteri).";
        document.getElementById('via-error').style.display = "block";
        isValid = false;
    }

    let numerocivico = document.getElementById('numerocivico');
    if (!numerocivico.value.trim() || !numerocivicoRegex.test(numerocivico.value.trim())) {
        document.getElementById('numerocivico-error').innerText = "Inserisci un numero civico valido (1-5 cifre, con eventuale suffisso 'bis', 'tris' o una lettera).";
        document.getElementById('numerocivico-error').style.display = "block";
        isValid = false;
    }

    let cap = document.getElementById('cap');
    if (!cap.value.trim() || !capRegex.test(cap.value.trim())) {
        document.getElementById('cap-error').innerText = "Inserisci un CAP valido (esattamente 5 numeri).";
        document.getElementById('cap-error').style.display = "block";
        isValid = false;
    }

    let città = document.getElementById('città');
    if (!città.value.trim() || !cittàRegex.test(città.value.trim())) {
        document.getElementById('città-error').innerText = "Inserisci una città valida (solo lettere, spazi o apostrofi, massimo 255 caratteri).";
        document.getElementById('città-error').style.display = "block";
        isValid = false;
    }

    let telefono = document.getElementById('telefono');
    if (telefono.value.trim() && !telefonoRegex.test(telefono.value.trim())) {
        document.getElementById('telefono-error').innerText = "Inserisci un numero di telefono valido (massimo 15 cifre, eventuale prefisso '+').";
        document.getElementById('telefono-error').style.display = "block";
        isValid = false;
    }

    // Validazione dei dati del pagamento
    let numeroCarta = document.getElementById('numeroCarta');
    if (!numeroCarta.value.trim() || !cardNumberRegex.test(numeroCarta.value.trim())) {
        document.getElementById('numeroCarta-error').innerText = "Inserisci un numero carta valido (16 cifre separate da spazi).";
        document.getElementById('numeroCarta-error').style.display = "block";
        isValid = false;
    }

    let nomeIntestatario = document.getElementById('nomeIntestatario');
    if (!nomeIntestatario.value.trim() || !nameRegex.test(nomeIntestatario.value.trim())) {
        document.getElementById('nomeIntestatario-error').innerText = "Inserisci un nome intestatario valido (solo lettere, spazi o apostrofi, massimo 255 caratteri).";
        document.getElementById('nomeIntestatario-error').style.display = "block";
        isValid = false;
    }

    let scadenzaCarta = document.getElementById('scadenzaCarta');
    if (!scadenzaCarta.value.trim() || !expirationRegex.test(scadenzaCarta.value.trim())) {
        document.getElementById('scadenzaCarta-error').innerText = "Inserisci una data di scadenza valida nel formato MM/YY.";
        document.getElementById('scadenzaCarta-error').style.display = "block";
        isValid = false;
    } else {
        // Controllo se la carta è scaduta
        const parts = scadenzaCarta.value.trim().split('/');
        const mese = parseInt(parts[0], 10);
        const anno = parseInt(parts[1], 10) + 2000; // Assume che l'anno sia nel formato YY e lo converte in YYYY

        // Ottieni la data corrente
        const oggi = new Date();
        const annoCorrente = oggi.getFullYear();
        const meseCorrente = oggi.getMonth() + 1; // I mesi in JavaScript vanno da 0 a 11

        if (anno < annoCorrente || (anno === annoCorrente && mese < meseCorrente)) {
            document.getElementById('scadenzaCarta-error').innerText = "La carta è scaduta. Inserisci una data di scadenza valida.";
            document.getElementById('scadenzaCarta-error').style.display = "block";
            isValid = false;
        }
    }

    let cvv = document.getElementById('cvv');
    if (!cvv.value.trim() || !cvvRegex.test(cvv.value.trim())) {
        document.getElementById('cvv-error').innerText = "Inserisci un CVV valido (esattamente 3 cifre).";
        document.getElementById('cvv-error').style.display = "block";
        isValid = false;
    }

    if (!isValid) {
        event.preventDefault();
    }
}