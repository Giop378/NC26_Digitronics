const prodottiPerPagina = 8;
const prodotti = document.querySelectorAll('.product');
const totalePagine = Math.ceil(prodotti.length / prodottiPerPagina);
let paginaCorrente = 1;

function mostraPagina(pagina) {
    const inizio = (pagina - 1) * prodottiPerPagina;
    const fine = inizio + prodottiPerPagina;

    prodotti.forEach((prodotto, index) => {
        if (index >= inizio && index < fine) {
            prodotto.style.display = 'block';
        } else {
            prodotto.style.display = 'none';
        }
    });

    document.getElementById('info-pagina').textContent = 'Pagina: ' + paginaCorrente;
    document.getElementById('pagina-precedente').disabled = paginaCorrente === 1;
    document.getElementById('pagina-successiva').disabled = paginaCorrente === totalePagine;
}

function cambiaPagina(direzione) {
    paginaCorrente += direzione;
    mostraPagina(paginaCorrente);
}

mostraPagina(paginaCorrente);