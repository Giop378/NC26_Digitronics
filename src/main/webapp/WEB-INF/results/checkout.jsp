<%--
  Created by IntelliJ IDEA.
  User: pomic
  Date: 05/01/2025
  Time: 21:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="java.util.List" %>
<%@ page import="it.unisa.nc26.digitronics.model.bean.Utente" %>
<%@ page import="it.unisa.nc26.digitronics.model.bean.MetodoSpedizione" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="it">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Checkout</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/checkoutStyles.css">
</head>
<body>
<%@ include file="/WEB-INF/results/header.jsp" %>
<div class="container">
  <h2>Checkout</h2>
  <%Utente utente = (Utente) session.getAttribute("utente"); %>
  <form class="checkout-form" action="procedi-al-pagamento" method="post">
    <label for="nome">Nome:</label>
    <input type="text" id="nome" name="nome" value="<%=utente.getNome()%>"required  pattern="[a-zA-Z ]+" title="Inserisci solo lettere e spazi">

    <label for="cognome">Cognome:</label>
    <input type="text" id="cognome" name="cognome" value="<%=utente.getCognome()%>" required   pattern="[a-zA-Z ]+" title="Inserisci solo lettere e spazi">

    <label for="via">Via:</label>
    <input type="text" id="via" name="via" required>

    <label for="numerocivico">Numero Civico:</label>
    <input type="text" id="numerocivico" name="numerocivico" required  pattern="[0-9]{1,4}" title="Inserisci massimo 4 numeri" >

    <label for="cap">CAP:</label>
    <input type="text" id="cap" name="cap" required pattern="[0-9]{5}" title="Inserisci esattamente  5 numeri">

    <label for="telefono">Telefono:</label>
    <input type="text" id="telefono" name="telefono" pattern="[0-9]{9,15}" title="Inserisci da 9 a 15 numeri">


    <% List<MetodoSpedizione> metodiSpedizione = (List<MetodoSpedizione>) request.getAttribute("metodiSpedizione"); %>
    <label for="metodoSpedizione">Metodo di Spedizione:</label>
    <select id="metodoSpedizione" name="metodoSpedizione" required>
      <% for(MetodoSpedizione metodoSpedizione : metodiSpedizione) { %>
      <option value="<%=metodoSpedizione.getNome()%>">
        <%=metodoSpedizione.getNome()%> Prezzo: €<%= String.format("%.2f", (double) metodoSpedizione.getCosto()).replace('.', ',') %>
      </option>
      <% } %>
    </select>

    <div class="total">
      <% double prezzoTotale = (double) request.getAttribute("prezzoTotale"); %>
      Prezzo Totale(Spedizione esclusa): €<%= String.format("%.2f", (double) prezzoTotale).replace('.', ',') %>
    </div>

    <button type="submit">ConfermaOrdine</button>
  </form>
</div>
<%@ include file="/WEB-INF/results/footer.jsp" %>
</body>
</html>
