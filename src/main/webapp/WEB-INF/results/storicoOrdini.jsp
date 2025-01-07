<%--
  Created by IntelliJ IDEA.
  User: vincenzo
  Date: 05/01/25
  Time: 22:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="java.util.List" %>
<%@ page import="it.unisa.nc26.digitronics.model.bean.Ordine" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="./css/storicoOrdini.css">
    <title>Storico Ordini</title>
</head>
<body>
<%@include file="header.jsp"%>

<div class="main">
    <h1>Storico Ordini</h1>
    <% List<Ordine> ordini = (List<Ordine>) request.getAttribute("ordini");
        if(ordini.isEmpty()) { %>
    <p>Non hai effettuato nessun ordine.</p>
    <%}
    else{
        for(Ordine ordine : ordini) { %>
    <div class="order-card" id="order<%= ordine.getIdOrdine() %>">
        <h2>ID Ordine: <%= ordine.getIdOrdine() %></h2>
        <p>Nome: <%= ordine.getNome() %> <%= ordine.getCognome() %></p>
        <p>Indirizzo: <%= ordine.getVia() %>, <%= ordine.getNumeroCivico() %></p>
        <p>CAP: <%= ordine.getCap() %></p>
        <p>Città: <%= ordine.getCittà() %></p>
        <p>Telefono: <%= ordine.getTelefono() %></p>
        <p>Prezzo Totale: €<%= String.format("%.2f", ordine.getTotale()) %></p>
        <p>Metodo di Pagamento: <%= ordine.getNomeMetodoSpedizione() %></p>
        <p>Data: <%= ordine.getDataOrdine()%></p>
        <form action="${pageContext.request.contextPath}/showItemsOrder" method="get">
            <input type="hidden" name="idOrdine" value="<%= ordine.getIdOrdine() %>">
            <button type="submit">Visualizza i prodotti dell'ordine</button>
        </form>
    </div>
    <% }
    } %>
</div>

<%@include file="footer.jsp"%>

</body>
</html>

