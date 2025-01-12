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
    <%= erroreIndirizzo %>
  </div>
  <% } %>
  <form class="checkout-form" action="procedi-al-pagamento" method="post">

    <h3>Dati della Spedizione</h3>
    <label for="nome">Nome:</label>
    <input type="text" id="nome" name="nome" value="<%= request.getAttribute("nome") != null ? request.getAttribute("nome") : "" %>"
           required pattern="^(?!\s*$)[a-zA-Zà-ÿÀ-Ÿ\s']{1,255}$"
           title="Inserisci solo lettere, spazi o apostrofi. Massimo 255 caratteri.">

    <label for="cognome">Cognome:</label>
    <input type="text" id="cognome" name="cognome" value="<%= request.getAttribute("cognome") != null ? request.getAttribute("cognome") : "" %>"
           required pattern="^(?!\s*$)[a-zA-Zà-ÿÀ-Ÿ\s']{1,255}$"
           title="Inserisci solo lettere, spazi o apostrofi. Massimo 255 caratteri.">

    <label for="via">Via:</label>
    <input type="text" id="via" name="via"
           required minlength="1" maxlength="255"
           title="Inserisci il nome della via, da 1 a 255 caratteri.">

    <label for="numerocivico">Numero Civico:</label>
    <input type="text" id="numerocivico" name="numerocivico"
           required pattern="^\d{1,5}(\s?(bis|tris|[a-zA-Z]))?$"
           title="Inserisci un numero civico composto da 1 a 5 cifre, con un eventuale suffisso come 'bis', 'tris' o una lettera.">

    <label for="cap">CAP:</label>
    <input type="text" id="cap" name="cap"
           required pattern="^\d{5}$"
           title="Inserisci il CAP composto esattamente 5 numeri">

    <label for="città">Città:</label>
    <input type="text" id="città" name="città"
           required pattern="^(?!\s*$)[a-zA-Zà-ÿÀ-Ÿ\s']{1,255}$"
           title="Inserisci solo lettere, spazi o apostrofi. Massimo 255 caratteri.">

    <label for="telefono">Telefono:</label>
    <input type="text" id="telefono" name="telefono" value="<%= request.getAttribute("telefono") != null ? request.getAttribute("telefono") : "" %>"
           pattern="^\+?[1-9]\d{1,14}$"
           title="Inserisci un numero di telefono valido, con un massimo di 15 cifre. Può includere un prefisso '+'.">

    <% List<MetodoSpedizione> metodiSpedizione = (List<MetodoSpedizione>) request.getAttribute("metodiSpedizione"); %>
    <label for="metodoSpedizione">Metodo di Spedizione:</label>
    <select id="metodoSpedizione" name="metodoSpedizione" required>
      <% for(MetodoSpedizione metodoSpedizione : metodiSpedizione) { %>
      <option value="<%=metodoSpedizione.getNome()%>">
        <%=metodoSpedizione.getNome()%> Prezzo: €<%= String.format("%.2f", metodoSpedizione.getCosto()).replace('.', ',') %>
      </option>
      <% } %>
    </select>

    <div class="total">
      <% double prezzoTotale = (double) request.getAttribute("prezzoTotale"); %>
      Prezzo Totale (Spedizione esclusa): €<%= String.format("%.2f", (double) prezzoTotale).replace('.', ',') %>
    </div>

    <h3>Dati del Pagamento</h3>
    <label for="numeroCarta">Numero Carta:</label>
    <input type="text" id="numeroCarta" name="numeroCarta" maxlength="19" required
           pattern="(?:\d{4} ){3}\d{4}"
           placeholder="1234 5678 9012 3456"
           title="Inserisci esattamente 16 cifre, separate da spazi"
           oninput="formatCardNumber(this)">

    <label for="nomeIntestatario">Nome Intestatario:</label>
    <input type="text" id="nomeIntestatario" name="nomeIntestatario" required pattern="^(?!\s*$)[a-zA-Zà-ÿÀ-Ÿ\s']{1,255}$"
           placeholder="Mario Rossi"
           title="Inserisci solo lettere, spazi o apostrofi. Massimo 255 caratteri.">

    <div class="payment-row">
      <div class="field">
        <label for="scadenzaCarta">Scadenza (MM/YY):</label>
        <input type="text" id="scadenzaCarta" name="scadenzaCarta" maxlength="5" required
               pattern="^(0[1-9]|1[0-2])\/[0-9]{2}$"
               placeholder="MM/YY"
               title="Inserisci una data nel formato MM/YY"
               oninput="formatExpirationDate(this)">
      </div>
      <div class="field">
        <label for="cvv">CVV:</label>
        <input type="text" id="cvv" name="cvv" maxlength="3" required
               pattern="^[0-9]{3}$"
               placeholder="123"
               title="Inserisci esattamente 3 cifre">
      </div>
    </div>

    <button type="submit">Conferma Ordine</button>
  </form>
</div>
<%@ include file="/WEB-INF/results/footer.jsp" %>
<script>
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
