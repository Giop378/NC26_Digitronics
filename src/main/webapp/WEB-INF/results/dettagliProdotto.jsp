<%--
  Created by IntelliJ IDEA.
  User: vincenzo
  Date: 02/01/25
  Time: 22:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="it.unisa.nc26.digitronics.model.bean.Utente" %>
<%@ page import="it.unisa.nc26.digitronics.model.bean.Recensione" %>
<%@ page import="java.util.List" %>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dettagli Prodotto</title>
    <link rel="stylesheet" href="./css/dettagliProdotto.css">
    <script>
        function toggleFeedbackForm() {
            const form = document.getElementById('feedback-form');
            form.style.display = form.style.display === 'block' ? 'none' : 'block';
        }
    </script>
</head>
<body>
<%@ include file="/WEB-INF/results/header.jsp" %>

<div class="container">
    <!-- Dettagli prodotto -->
    <div class="product-container">
        <div class="product-image">
            <img src="${product.immagine}" alt="${product.nome}">
        </div>
        <div class="product-details">
            <h1>${product.nome}</h1>
            <p class="product-description">${product.descrizione}</p>
            <p class="product-price"><strong>Prezzo:</strong> ${product.prezzo} €</p>
            <form action="aggiungi-prodotto-carrello" method="post">
                <input type="hidden" name="idProdotto" value="${product.idProdotto}">
                <input type="number" name="quantità" value="1" min="1" step="1" class="quantity-input" aria-labelledby="aggiungi-carrello">
                <button id="aggiungi-carrello" type="submit" class="add-to-cart-button">Aggiungi al carrello</button>
            </form>
        </div>
    </div>

    <!-- Sezione recensioni -->
    <section class="reviews">
        <h2>Recensioni</h2>
        <%
            Utente currentUser = (Utente) request.getSession().getAttribute("utente");
            if (currentUser != null) {
        %>
        <button onclick="toggleFeedbackForm()" class="add-feedback-button">Aggiungi una recensione</button>

        <div id="feedback-form" class="feedback-form" style="display: none;">
            <form action="${pageContext.request.contextPath}/aggiungiRecensione" method="get">
                <input type="hidden" name="productId" value="${product.idProdotto}">
                <label for="title">Titolo:</label>
                <input type="text" id="title" name="title" required>
                <label for="description">Descrizione:</label>
                <textarea id="description" name="description" required></textarea>
                <label for="score">Punteggio:</label>
                <input type="number" id="score" name="score" min="1" max="5" required>
                <input type="hidden" name="userId" value="<%= currentUser.getIdUtente() %>">
                <button type="submit">Invia</button>
            </form>
        </div>
        <% } else { %>
        <p>Effettua il <a href="autenticazione-servlet?action=login">login</a> per aggiungere una recensione.</p>
        <% } %>

        <!-- Elenco recensioni -->
        <div class="reviews-list">
            <%
                List<Recensione> recensioni = (List<Recensione>) request.getAttribute("recensioni");
                if (recensioni == null || recensioni.isEmpty()) {
            %>
            <p>Non ci sono recensioni per questo prodotto.</p>
            <%
            } else {
                for (Recensione recensione : recensioni) {
            %>
            <div class="review-item">
                <p><strong>Utente:</strong> <%= recensione.getUtente().getNome() + " " + recensione.getUtente().getCognome() %></p>
                <p><strong>Titolo:</strong> <%= recensione.getTitolo() %></p>
                <p><strong>Descrizione:</strong> <%= recensione.getDescrizione() %></p>
                <p><strong>Punteggio:</strong> <%= recensione.getPunteggio() %> / 5</p>
            </div>
            <%
                    }
                }
            %>
        </div>
    </section>
</div>

<%@ include file="/WEB-INF/results/footer.jsp" %>
</body>
</html>