<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <title>Errore generale</title>
    <link rel="stylesheet" href="./css/errorStyles.css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
<%@ include file="header.jsp" %>
<div class="error-container">
    <h1>Qualcosa è andato storto!!</h1>
    <p>Ci scusiamo per l'inconveniente. Si è verificato un errore imprevisto.</p>
    <h4><%= exception.getMessage()%></h4>
    <p>Per favore, prova a:</p>

    <p>Tornare alla <a href="index.html">pagina iniziale</a></p>
    <p> oppure </p>
    <p>Contattare il nostro supporto tecnico se il problema persiste</p>

    <p>Grazie per la tua pazienza e comprensione.</p>
</div>
<%@ include file="footer.jsp" %>
</body>
</html>



