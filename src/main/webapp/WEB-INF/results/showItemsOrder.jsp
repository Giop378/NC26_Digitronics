<%--
  Created by IntelliJ IDEA.
  User: vincenzo
  Date: 05/01/25
  Time: 22:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="java.util.List" %>
<%@ page import="it.unisa.nc26.digitronics.model.bean.ItemOrdine" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="./css/showItemsOrder.css">
    <title>Dettagli Ordine</title>
</head>
<body>
<%@include file="header.jsp"%>
<div class="content-wrapper">
    <div class="main">
        <h1>Dettagli Ordine</h1>
        <% List<ItemOrdine> itemOrdineList = (List<ItemOrdine>) request.getAttribute("itemOrdineList");
            for (ItemOrdine itemOrdine : itemOrdineList) { %>
        <div class="item-card">
            <a href="dettagliProdotto?id=<%= itemOrdine.getIdProdotto() %>">
                <img src="<%= itemOrdine.getImmagine() %>" alt="Immagine Prodotto">
            </a>
            <div class="item-details">
                <h2><%= itemOrdine.getNome() %></h2>
                <p>ID: <%= itemOrdine.getIdItemOrdine()%></p>
                <p>Prezzo: <%= String.format("%.2f", itemOrdine.getPrezzo()) %>€</p>
                <p>Quantità: <%= itemOrdine.getQuantità() %></p>
            </div>
        </div>
        <% } %>
    </div>
    <div id="spacer"></div>
</div>

<%@include file="footer.jsp"%>

</body>
</html>
