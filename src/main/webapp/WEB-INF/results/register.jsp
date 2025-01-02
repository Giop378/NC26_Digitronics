<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Digitronics - E-commerce elettronica</title>
    <link rel="stylesheet" href="./css/loginStyles.css">
    <script>
        function validatePassword() {
            var password = document.getElementById("password").value;
            var confirmPassword = document.getElementById("confirm-password").value;

            if (password != confirmPassword) {
                document.getElementById("error-message").innerHTML = "Le password non corrispondono";
                return false;
            }
            return true;
        }
    </script>
</head>
<body>
<%@ include file="header.jsp" %>
<div class="login-container">
    <div class="register-box">
        <h2>Registrati</h2>
        <form action="registrazione-servlet" method="post" onsubmit="return validatePassword()">
            <div class="input-group">
                <label for="nome">Nome</label>
                <input type="text" id="nome" name="nome" required pattern="[a-zA-Z ]+" title="Inserisci solo lettere e spazi">
            </div>
            <div class="input-group">
                <label for="cognome">Cognome</label>
                <input type="text" id="cognome" name="cognome" required pattern="[a-zA-Z ]+" title="Inserisci solo lettere e spazi">
            </div>
            <div class="input-group">
                <label for="email">Email</label>
                <input type="email" id="email" name="email" required pattern="[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}">
            </div>
            <div class="input-group">
                <label for="password">Password</label>
                <input type="password" id="password" name="password" required minlength="8">
            </div>
            <div class="input-group">
                <label for="confirm-password">Conferma Password</label>
                <input type="password" id="confirm-password" name="confirm-password" required minlength="8">
            </div>
            <div class="input-group">
                <label for="datadinascita">Data di Nascita</label>
                <input type="date" id="datadinascita" name="datadinascita" required>
            </div>
            <button type="submit" class="login-button">Registrati</button>
            <p id="error-message" class="error-message"></p>
        </form>
    </div>
</div>
<%@ include file="footer.jsp" %>
</body>
</html>

