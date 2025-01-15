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
  <% String erroreIndirizzo = (String) request.getAttribute("erroreIndirizzo"); %>
  <% if (erroreIndirizzo != null) { %>
  <div class="errore-indirizzo">
    <p><%= erroreIndirizzo %></p>
  </div>
  <% } %>
  <form class="checkout-form" action="procedi-al-pagamento" method="post" onsubmit="validateCheckoutForm(event)">

    <h3>Dati della Spedizione(Spedizione solo in Italia)</h3>

    <label for="nome">Nome:</label>
    <input type="text" id="nome" name="nome" value="<%= request.getAttribute("nome") != null ? request.getAttribute("nome") : "" %>">
    <p id="nome-error" class="error-message"></p>

    <label for="cognome">Cognome:</label>
    <input type="text" id="cognome" name="cognome" value="<%= request.getAttribute("cognome") != null ? request.getAttribute("cognome") : "" %>">
    <p id="cognome-error" class="error-message"></p>

    <label for="via">Via:</label>
    <input type="text" id="via" name="via">
    <p id="via-error" class="error-message"></p>

    <label for="numerocivico">Numero Civico:</label>
    <input type="text" id="numerocivico" name="numerocivico">
    <p id="numerocivico-error" class="error-message"></p>

    <label for="cap">CAP:</label>
    <input type="text" id="cap" name="cap">
    <p id="cap-error" class="error-message"></p>

    <label for="città">Città:</label>
    <input type="text" id="città" name="città">
    <p id="città-error" class="error-message"></p>

    <label for="telefono">Telefono:</label>
    <input type="text" id="telefono" name="telefono" value="<%= request.getAttribute("telefono") != null ? request.getAttribute("telefono") : "" %>">
    <p id="telefono-error" class="error-message"></p>

    <%
      List<MetodoSpedizione> metodiSpedizione = (List<MetodoSpedizione>) request.getAttribute("metodiSpedizione");
    %>
    <label for="metodoSpedizione">Metodo di Spedizione:</label>
    <select id="metodoSpedizione" name="metodoSpedizione">
      <% for(MetodoSpedizione metodoSpedizione : metodiSpedizione) { %>
      <option value="<%=metodoSpedizione.getNome()%>">
        <%=metodoSpedizione.getNome()%> Prezzo: €<%= String.format("%.2f", metodoSpedizione.getCosto()).replace('.', ',') %>
      </option>
      <% } %>
    </select>

    <div class="total">
      <% double prezzoTotale = (double) request.getAttribute("prezzoTotale"); %>
      Prezzo Totale (Spedizione esclusa): €<%= String.format("%.2f", prezzoTotale).replace('.', ',') %>
    </div>

    <h3>Dati del Pagamento</h3>

    <label for="numeroCarta">Numero Carta:</label>
    <input type="text" id="numeroCarta" name="numeroCarta" maxlength="19" placeholder="1234 5678 9012 3456" oninput="formatCardNumber(this)">
    <p id="numeroCarta-error" class="error-message"></p>

    <label for="nomeIntestatario">Nome Intestatario:</label>
    <input type="text" id="nomeIntestatario" name="nomeIntestatario" placeholder="Mario Rossi">
    <p id="nomeIntestatario-error" class="error-message"></p>

    <div class="payment-row">
      <div class="field">
        <label for="scadenzaCarta">Scadenza (MM/YY):</label>
        <input type="text" id="scadenzaCarta" name="scadenzaCarta" maxlength="5" placeholder="MM/YY" oninput="formatExpirationDate(this)">
        <p id="scadenzaCarta-error" class="error-message"></p>
      </div>
      <div class="field">
        <label for="cvv">CVV:</label>
        <input type="text" id="cvv" name="cvv" maxlength="3" placeholder="123">
        <p id="cvv-error" class="error-message"></p>
      </div>
    </div>

    <button type="submit">Conferma Ordine</button>
  </form>
</div>
<%@ include file="/WEB-INF/results/footer.jsp" %>

<script src="./script/validateFormCheckout.js"></script>

<script>
  // Funzione per il formatting del numero di carta e della data di scadenza
  function formatCardNumber(input) {
    let value = input.value.replace(/\D/g, '');
    input.value = value.replace(/(\d{4})/g, '$1 ').trim();
  }

  function formatExpirationDate(input) {
    let value = input.value.replace(/\D/g, '');
    if (value.length > 2) {
      input.value = value.slice(0, 2) + '/' + value.slice(2, 4);
    } else {
      input.value = value;
    }
  }
</script>
</body>
</html>
