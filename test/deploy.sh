#!/bin/bash

# Définition des variables
APP_NAME="TestFramework"
SRC_DIR="src/main/java"
WEB_DIR="src/main/webapps"
BUILD_DIR="build"
LIB_DIR="lib"
TOMCAT_WEBAPPS="/home/elie/apache-tomcat-10.1.28/webapps"
SERVLET_API_JAR="$LIB_DIR/servlet-api.jar"
MY_FRAMEWORK_JAR="$LIB_DIR/URLframework.jar"

# Nettoyage et création du répertoire temporaire
rm -rf $BUILD_DIR
mkdir -p $BUILD_DIR/WEB-INF/classes
mkdir -p $BUILD_DIR/WEB-INF/lib

# Copier les JARs nécessaires dans WEB-INF/lib
cp $SERVLET_API_JAR $BUILD_DIR/WEB-INF/lib/
cp $MY_FRAMEWORK_JAR $BUILD_DIR/WEB-INF/lib/

# Compilation des fichiers Java avec TOUS les JARs nécessaires
find $SRC_DIR -name "*.java" > sources.txt
javac -source 17 -target 17 -cp "$SERVLET_API_JAR:$MY_FRAMEWORK_JAR" \
      -d $BUILD_DIR/WEB-INF/classes @sources.txt
rm sources.txt

# Copier les fichiers web (web.xml, JSP, etc.)
if [ -d "$WEB_DIR" ]; then
    cp -r $WEB_DIR/* $BUILD_DIR/
fi

# Générer le fichier .war dans le dossier build
cd $BUILD_DIR || exit
jar -cvf $APP_NAME.war *
cd ..

# Déploiement dans Tomcat
cp -f $BUILD_DIR/$APP_NAME.war $TOMCAT_WEBAPPS/

echo "Redemarrage de Tomcat pour prendre en compte le nouveau déploiement..."

sudo /home/elie/apache-tomcat-10.1.28/bin/shutdown.sh
sleep 1
sudo /home/elie/apache-tomcat-10.1.28/bin/startup.sh

echo ""
echo "Déploiement terminé. Redémarrez Tomcat si nécessaire."
echo ""