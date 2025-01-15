<%@ page import="it.unisa.nc26.digitronics.model.bean.Categoria" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Modifica del prodotto</title>
    <link rel="stylesheet" href="./css/modifyProductStyles.css">
    <script src="./script/modifyDelete.js"></script>
    <script src="./script/validateFormModificaProdotto.js"></script>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
<div class="header">
    <a href="index.html" class="logo" aria-label="Home">
        <img src="./images/logo.png" alt="Digitronics Logo" class="logo-img">
    </a>
    <div class="spacer"></div>
    <div class="search-container">
        <input type="text" placeholder="Cerca i nostri prodotti..." class="search-bar" aria-label="Cerca" aria-labelledby="results">
        <div class="search-results" id="results"></div>
    </div>
</div>

<div id="intro">
    <h1>Modifica prodotto</h1>
    <h3>Seleziona un prodotto dalla barra di ricerca per modificarlo</h3>
</div>
<form action="modify-product-servlet" method="post" onsubmit="return validateFormModificaProdotto(event)">
    <input type="hidden" id="id" name="id" value="0">

    <label for="nome">Nome:</label>
    <input type="text" id="nome" name="nome" ><br>
    <div id="nome-warning" class="warning" style="display:none; color:red;"></div><br>

    <label for="descrizione">Descrizione:</label>
    <input type="text" id="descrizione" name="descrizione" ><br>
    <div id="descrizione-warning" class="warning" style="display:none; color:red;"></div><br>

    <label for="prezzo">Prezzo:</label>
    <input type="text" id="prezzo" name="prezzo"  ><br>
    <div id="prezzo-warning" class="warning" style="display:none; color:red;"></div><br>

    <label for="vetrina">In Vetrina:</label>
    <input type="checkbox" id="vetrina" name="vetrina" value="true"><br>


    <label for="quantita">Quantità:</label>
    <input type="number" id="quantita" name="quantita"  ><br>
    <div id="quantita-warning" class="warning" style="display:none; color:red;"></div><br>

    <label for="categoria">Categoria:</label>
    <select id="categoria" name="nomecategoria" >
        <% List<Categoria> categorie = (List<Categoria>) application.getAttribute("categorie");
            for (Categoria categoria : categorie) { %>
        <option  value="<%=categoria.getNome()%>"><%=categoria.getNome()%></option>
        <% } %>
    </select><br>



    <input type="submit" value="Invia">
</form>
</body>
</html>
