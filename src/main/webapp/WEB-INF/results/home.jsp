<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Digitronics - E-commerce elettronica</title>
    <link rel="stylesheet" href="./css/styles.css">
</head>
<body>
<%@ include file="header.jsp" %>

<div class="main">
    <div class="intro">
        <h1>Benvenuto in Digitronics</h1>
        <p>Benvenuto nel tuo mondo digitale: scopri smartphone, smartwatch, tablet e accessori perfetti per te!</p>
    </div>
    <div class="featured-products">
        <h2>VETRINA</h2>
        <div class="product-list">
            <c:forEach var="prodotto" items="${prodottiVetrina}">
                <div class="product">
                    <a href="prodotto?id=${prodotto.idProdotto}">
                        <img src="${prodotto.immagine}" alt="${prodotto.nome}" class="product-img">
                        <h3>${prodotto.nome}</h3>
                    </a>
                    <p>Prezzo: <fmt:formatNumber value="${prodotto.prezzo}" type="currency" currencySymbol="€" maxFractionDigits="2"/></p>
                    <div class="add-to-cart">
                        <form action="add-product-cart" method="post">
                            <input type="hidden" name="idProdotto" value="${prodotto.idProdotto}">
                            <input type="number" name="quantità" value="1" min="1" step="1" class="quantity-input" aria-labelledby="aggiungi-carrello">
                            <button id="aggiungi-carrello" type="submit" class="add-to-cart-button">Aggiungi al carrello</button>
                        </form>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</div>
<%@ include file="footer.jsp" %>
</body>
</html>
