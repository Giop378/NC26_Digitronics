<%--
  Created by IntelliJ IDEA.
  User: pomic
  Date: 07/07/2024
  Time: 06:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="it.unisa.nc26.digitronics.model.bean.Ordine" %>
<%@ page import="it.unisa.nc26.digitronics.model.bean.ItemOrdine" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Conferma Ordine</title>
    <link rel="stylesheet" href="./css/confirmOrderStyles.css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
<%@ include file="/WEB-INF/results/header.jsp" %>
<div class="container">
    <h1>Conferma Ordine</h1>
    <h2>Dettagli Ordine</h2>
    <%
        Ordine ordine = (Ordine) request.getAttribute("ordine");
    %>
    <p><b>Nome:</b> <%= ordine.getNome() %> <%= ordine.getCognome() %></p>
    <p><b>Indirizzo:</b> <%= ordine.getVia() %>, <%= ordine.getNumeroCivico() %>, <%= ordine.getCittà() %></p>
    <p><b>CAP:</b> <%= ordine.getCap() %></p>
    <p><b>Telefono:</b> <%= ordine.getTelefono() %></p>
    <p><b>Metodo di Spedizione:</b> <%= ordine.getNomeMetodoSpedizione() %></p>
    <p><b>Data ordine:</b> <%= ordine.getDataOrdine() %></p>
    <p><b>Prezzo Totale:</b> €<%= String.format("%.2f", ordine.getTotale()).replace('.', ',') %></p>

    <h2>Item Ordine</h2>
    <div class="table-container">
        <table>
            <tr>
                <th>Nome</th>
                <th>Immagine</th>
                <th>Prezzo</th>
                <th>Quantità</th>
                <th>Totale</th>
            </tr>
            <%
                List<ItemOrdine> itemsOrdine = (List<ItemOrdine>) request.getAttribute("itemsOrdine");
                for (ItemOrdine item : itemsOrdine) {
            %>
            <tr>
                <td><%= item.getNome() %></td>
                <td><img src="<%= item.getImmagine() %>" alt="<%= item.getNome() %>" class="product-img"/></td>
                <td>€<%= String.format("%.2f", item.getPrezzo()).replace('.', ',') %></td>
                <td><%= item.getQuantità() %></td>
                <td>€<%= String.format("%.2f", (item.getPrezzo() * item.getQuantità())).replace('.', ',') %></td>
            </tr>
            <%
                }
            %>
            <tr>
                <td colspan="4" class="total">Prezzo Totale:</td>
                <td>€<%= String.format("%.2f", ordine.getTotale()).replace('.', ',') %></td>
            </tr>
        </table>
    </div>
</div>
<%@ include file="/WEB-INF/results/footer.jsp" %>
</body>
</html>
