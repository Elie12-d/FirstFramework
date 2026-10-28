<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Test framework</title>
</head>

<body>
    <!-- <form action="/Sprint7Bis/save" method="post">
        <input type="text" name="nom" placeholder="Nom">
        <input type="text" name="prenom" placeholder="Prenom">
        <input type="number" name="age" placeholder="Age">
        <button type="submit">
            Envoyer
        </button>
    </form> -->
    <form action="${pageContext.request.contextPath}/upload" method="post" enctype="multipart/form-data">

        <input type="file" name="document">
        <button type="submit">Envoyer</button>
    </form>
</body>

</html>