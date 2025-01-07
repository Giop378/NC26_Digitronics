<%@ page import="it.unisa.nc26.digitronics.model.bean.Categoria" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Upload del prodotto</title>
    <link rel="stylesheet" href="./css/addProductStyles.css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
<div class="header-admin">
    <a href="index.html" class="logo" aria-label="Home">
        <img src="./images/logo.png" alt="Digitronics Logo" class="logo-img">
    </a>
</div>
<form action="add-product-servlet" method="post" enctype="multipart/form-data">
    <label for="nome">Nome:</label>
    <input type="text" id="nome" name="nome" required><br>

    <label for="descrizione">Descrizione:</label>
    <input type="text" id="descrizione" name="descrizione" required><br>

    <label for="prezzo">Prezzo(in centesimi):</label>
    <input type="number"  min="0" step="0.01" id="prezzo" name="prezzo" required><br>

    <label for="vetrina">In Vetrina:</label>
    <input type="checkbox" id="vetrina" name="vetrina" value="true"><br>


    <label for="quantita">Quantità:</label>
    <input type="number"  min="0" step="1" id="quantita" name="quantita" required><br>

    <label for="categoria">Categoria:</label>
    <select id="categoria" name="nomecategoria" required>
        <% List<Categoria> categorie = (List<Categoria>) application.getAttribute("categorie");
            for (Categoria categoria : categorie) { %>
                <option  value="<%=categoria.getNome()%>"><%=categoria.getNome()%></option>
        <% } %>
    </select><br>

    <label for="file">File da caricare:</label>
    <input type="file" id="file" name="file" required><br>

    <input type="submit" value="Invia">
</form>
</body>
</html>