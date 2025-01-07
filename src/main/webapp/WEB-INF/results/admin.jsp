<%--
  Created by IntelliJ IDEA.
  User: DomeA
  Date: 03/01/2025
  Time: 12:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="it.unisa.nc26.digitronics.model.bean.Prodotto" %>
<%@ page import="java.util.List" %>
<%@ page import="it.unisa.nc26.digitronics.model.bean.Categoria" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Area Amministratore</title>
    <link rel="stylesheet" href="./css/adminStyles.css">
</head>
<body>
<div class="header-admin">
    <a href="index.html" class="logo" aria-label="Home">
        <img src="./images/logo.png" alt="Digitronics Logo" class="logo-img">
    </a>
</div>
<h1>Area Amministratore</h1>
<div class="button-container">
    <button onclick="location.href='admin-choice-servlet?choice=addproduct'">Aggiungi Prodotto</button>
    <button onclick="location.href='admin-choice-servlet?choice=modifyproduct'">Modifica Prodotto</button>
    <button onclick="location.href='admin-choice-servlet?choice=deleteproduct'">Rimuovi Prodotto</button>
    <button onclick="location.href='${pageContext.request.contextPath}/storicoOrdini'">Visualizza Ordini</button>
    <button onclick="location.href='logout'">Esci</button>
</div>
</body>
</html>

