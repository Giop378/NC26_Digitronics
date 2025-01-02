<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="it">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Digitronics - E-commerce elettronica</title>
  <link rel="stylesheet" href="./css/loginStyles.css">
</head>
<body>
<%@ include file="header.jsp" %>
<div class="login-container">
  <div class="login-box">
    <h2>Login</h2>
    <form action="autenticazione-servlet" method="post">
      <div class="input-group">
        <label for="email">Email</label>
        <input type="email" id="email" name="email" required pattern="[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}">
      </div>
      <div class="input-group">
        <label for="password">Password</label>
        <input type="password" id="password" name="password" required>
      </div>
      <button type="submit" class="login-button">Login</button>
    </form>
    <p id="error-message" class="error-message"></p>
    <p class="register-link">Non hai un account? <a href="autenticazione-servlet?action=register">Registrati qui</a></p>
  </div>
</div>
<%@ include file="footer.jsp" %>
</body>
</html>
