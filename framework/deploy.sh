#!/bin/bash

echo "compilation compatible avec Java 17 en raison de Tomcat 10.1.28..."
javac --release 17 -cp lib/servlet-api.jar -d build src/main/java/controller/*.java src/main/java/utils/*.java src/main/java/annotation/*.java src/main/java/listner/*.java

echo "génération du fichier .jar..."
jar cvf URLframework.jar -C build .

echo "copie du .jar dans le dossier lib du projet de test..."
cp URLframework.jar ../test/lib/