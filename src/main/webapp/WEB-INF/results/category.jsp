<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="it.unisa.nc26.digitronics.model.bean.Prodotto" %>
<%@ page import="java.util.List" %>
<%@ page import="it.unisa.nc26.digitronics.model.bean.Categoria" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Digitronics - Prodotti per Categoria</title>
    <link rel="stylesheet" href="./css/styles.css">
</head>
<body>
<%@ include file="/WEB-INF/results/header.jsp" %>

<div class="main">
    <div class="intro">
        <h1>${categoriaScelta.nome}</h1>
    </div>

    <div class="category-image">
        <img src="${categoriaScelta.immagine}" alt="${categoriaScelta.nome}" class="category-img">
        <p class="category-description">${categoriaScelta.descrizione}</p>
    </div>

    <div class="featured-products">
        <h2>PRODOTTI</h2>
        <div class="product-list" id="lista-prodotti">
            <c:forEach var="prodotto" items="${prodottiPerCategoria}">
                <div class="product">
                    <a href="dettagliProdotto?id=${prodotto.idProdotto}">
                        <img src="${prodotto.immagine}" alt="${prodotto.nome}" class="product-img">
                        <h3>${prodotto.nome}</h3>
                    </a>
                    <p>Prezzo: <fmt:formatNumber value="${prodotto.prezzo}" type="currency" currencySymbol="€" maxFractionDigits="2"/></p>
                    <div class="add-to-cart">
                        <form action="aggiungi-prodotto-carrello" method="post">
                            <input type="hidden" name="idProdotto" value="${prodotto.idProdotto}">
                            <input type="number" name="quantità" value="1" min="1" step="1" class="quantity-input" aria-labelledby="aggiungi-carrello">
                            <button id="aggiungi-carrello" type="submit" class="add-to-cart-button">Aggiungi al carrello</button>
                        </form>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>

    <div class="pagination">
        <button id="pagina-precedente" onclick="cambiaPagina(-1)">Precedente</button>
        <span id="info-pagina"></span>
        <button id="pagina-successiva" onclick="cambiaPagina(1)">Successiva</button>
    </div>


</div>


<%@ include file="/WEB-INF/results/footer.jsp" %>
<script src="./script/paging.js"></script>
</body>
</html>