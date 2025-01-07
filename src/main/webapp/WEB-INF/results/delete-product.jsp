<%@ page import="it.unisa.nc26.digitronics.model.bean.Categoria" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: vincenzo
  Date: 06/01/25
  Time: 20:07
  To change this template use File | Settings | File Templates.
--%>
<html>
<head>
    <title>Visualizza ed Elimina Prodotto</title>
    <script src="./script/modifyDelete.js"></script>
    <link rel="stylesheet" href="./css/modifyProductStyles.css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
<div class="header">
    <a href="index.html" class="logo" aria-label="Home">
        <img src="./images/logo.png" alt="FrostCampania Logo" class="logo-img">
    </a>
    <div class="spacer"></div>
    <div class="search-container">
        <input type="text" placeholder="Cerca i nostri prodotti..." class="search-bar" aria-label="Cerca" aria-labelledby="results">
        <div class="search-results" id="results"></div>
    </div>
</div>

<div id="intro">
    <h1>Visualizza ed Elimina Prodotto</h1>
    <h3>Seleziona un prodotto dalla barra di ricerca per visualizzarlo</h3>
</div>
<form action="delete-product-servlet" method="post">

    <label for="id">ID Prodotto:</label>
    <input type="number" id="id" name="id">

    <label for="nome">Nome:</label>
    <input type="text" id="nome" name="nome" disabled><br>

    <label for="descrizione">Descrizione:</label>
    <input type="text" id="descrizione" name="descrizione" disabled><br>

    <label for="prezzo">Prezzo:</label>
    <input type="number" id="prezzo" name="prezzo" disabled><br>

    <label for="vetrina">In Vetrina:</label>
    <input type="checkbox" id="vetrina" name="vetrina" disabled><br>


    <label for="quantita">Quantità:</label>
    <input type="number" id="quantita" name="quantita" disabled><br>

    <label for="categoria">Categoria:</label>
    <select id="categoria" name="nomecategoria" disabled>
        <% List<Categoria> categorie = (List<Categoria>) application.getAttribute("categorie");
            for (Categoria categoria : categorie) { %>
        <option  value="<%=categoria.getNome()%>"><%=categoria.getNome()%></option>
        <% } %>
    </select><br>

    <input type="submit" value="Elimina">
</form>
</body>
</html>
