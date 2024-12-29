<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <title>header</title>
  <link rel="stylesheet" href="./css/header.css">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
<div class="header">
  <a href="index.html" class="logo" aria-label="Home">
    <img src="./images/logo.png" alt="Digitronics Logo" class="logo-img">
  </a>
  <div class="search-container">
    <input type="text" placeholder="Cerca i nostri prodotti..." class="search-bar" aria-label="Cerca" aria-labelledby="results">
    <div class="search-results" id="results"></div>
  </div>

  <div class="icon-container">
    <a href="user-servlet" class="icon-access" aria-label="Accedi">
      <img src="./images/login.png" alt="Accedi" class="icon-img">
    </a>
    <a href="carrello" class="icon-cart" aria-label="Carrello">
      <img src="./images/carrello.png" alt="Carrello" class="icon-img">
    </a>
  </div>
</div>
<div class="nav-items">
  <c:forEach var="categoria" items="${categorie}">
    <a href="categoria?categoria=${categoria.nome}" aria-label="${categoria.nome}">
        ${categoria.nome}
    </a>
  </c:forEach>
</div>
<script src="./script/header.js"></script>
</body>
</html>
