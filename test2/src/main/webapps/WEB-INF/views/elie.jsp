<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Elie</title>
</head>
<body>

<h1>Vue elie.jsp</h1>
<h2>${message}</h2>

<%
    java.util.List<main.java.entity.Etudiant> etudiants =
            (java.util.List<main.java.entity.Etudiant>) request.getAttribute("etudiants");
    if (etudiants != null) {
        out.println("<ul>");
        for (main.java.entity.Etudiant etudiant : etudiants) {
            out.println("<li>" + etudiant.getId() + " - " + etudiant.getNom() + "</li>");
        }
        out.println("</ul>");
    }
%>
</body>
</html>
