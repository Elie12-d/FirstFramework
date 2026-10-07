#!/bin/bash
set -e
echo "Compilation normale des fichiers Java..."
# Création du dossier build s'il n'existe pas
mkdir -p build

# Compilation standard (utilise la version installée par défaut sur votre machine)
javac -parameters --release 17 -cp "lib/*" -d build\
 src/main/java/controller/*.java \
 src/main/java/utils/*.java \
 src/main/java/annotation/*.java \
 src/main/java/http/*.java \
 src/main/java/mapping/*.java \
 src/main/java/view/*.java

echo "Compilation terminée. Les fichiers .class sont dans le dossier 'build'."

echo "Génération du fichier .jar..."
# Crée le fichier URLframework.jar à partir du contenu du dossier build
jar cvf URLframework.jar -C build .

echo "Fichier URLframework.jar généré avec succès !"