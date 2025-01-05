<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Operazione Riuscita</title>
    <link rel="stylesheet" href="./css/confirmOperationStyles.css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
<div class="header-admin">
    <a href="index.html" class="logo" aria-label="Home">
        <img src="./images/logo.png" alt="Digitronics Logo" class="logo-img">
    </a>
</div>
<h1>Operazione Riuscita</h1>
<div class="message-container">
    <h1><%=request.getAttribute("successMessage")%></h1>
    <button onclick="location.href='autenticazione-servlet'">Torna all'area utente</button>
</div>
</body>
</html>
