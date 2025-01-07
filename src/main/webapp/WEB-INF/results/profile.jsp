<%--
  Created by IntelliJ IDEA.
  User: pomic
  Date: 21/06/2024
  Time: 19:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Profilo Utente</title>
    <link rel="stylesheet" href="./css/styles.css">
    <link rel="stylesheet" href="./css/profileStyles.css">
</head>
<body>
<%@ include file="/WEB-INF/results/header.jsp" %>

<div class="profile-container">
    <div class="profile-header">
        <h2>Profilo Utente</h2>
    </div>
    <div class="profile-info">
        <div><strong>ID Utente:</strong> ${utente.idUtente}</div>
        <div><strong>Nome:</strong> ${utente.nome}</div>
        <div><strong>Cognome:</strong> ${utente.cognome}</div>
        <div><strong>Email:</strong> ${utente.email}</div>
        <div><strong>Data di Nascita:</strong> ${utente.dataDiNascita}</div>
    </div>
    <div class="profile-buttons">
        <form action="logout" method="post">
            <input type="hidden" name="action" value="logout">
            <button type="submit" class="profile-button">Logout</button>
        </form>
        <form action="${pageContext.request.contextPath}/storicoOrdini" method="get">
            <button type="submit" class="profile-button">Storico Ordini</button>
        </form>
    </div>
</div>

<%@ include file="/WEB-INF/results/footer.jsp" %>
</body>
</html>
