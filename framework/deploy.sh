#!/bin/bash

echo "compilation compatible avec Java 17 en raison de Tomcat 10.1.28..."
javac -source 17 -target 17 -cp lib/servlet-api.jar -d build src/main/java/controller/FrontController.java

echo "génération du fichier .jar..."
jar cvf URLframework.jar -C build .

echo "copie du .jar dans le dossier lib du projet de test..."
cp URLframework.jar ../test/lib/