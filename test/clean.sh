#!/bin/bash

echo "🧹 Nettoyage complet..."

# Arrêter Tomcat
sudo /home/john/apache-tomcat-10.1.55/bin/shutdown.sh
sleep 2

# Supprimer l'application déployée
sudo rm -rf /home/john/apache-tomcat-10.1.55/webapps/TestFramework*
sudo rm -rf /home/john/apache-tomcat-10.1.55/work/Catalina/localhost/TestFramework

# Nettoyer le build local
cd ~/FirstFramework/test
rm -rf build/
rm -rf src/main/webapps/WEB-INF/classes/

echo "🔨 Compilation..."

# Créer les répertoires
mkdir -p build/WEB-INF/classes
mkdir -p build/WEB-INF/lib

# Copier les JARs
cp lib/*.jar build/WEB-INF/lib/

# Compiler
find src/main/java -name "*.java" > sources.txt

if javac -source 17 -target 17 \
    -cp "lib/*:build/WEB-INF/classes" \
    -d build/WEB-INF/classes \
    @sources.txt; then
    echo "✅ Compilation réussie"
else
    echo "❌ Erreur de compilation"
    rm sources.txt
    exit 1
fi

rm sources.txt

# Vérifier les classes
echo ""
echo "📋 Classes compilées :"
ls -la build/WEB-INF/classes/controller/

echo ""
echo "📦 Création du WAR..."

# Créer le WAR
cd build
jar -cvf TestFramework.war * > /dev/null
cd ..

# Copier dans Tomcat
sudo cp build/TestFramework.war /home/john/apache-tomcat-10.1.55/webapps/

echo ""
echo "🚀 Démarrage de Tomcat..."
sudo /home/john/apache-tomcat-10.1.55/bin/startup.sh

sleep 3

if pgrep -f "apache-tomcat-10.1.55" > /dev/null; then
    echo "✅ Tomcat a redémarré avec succès"
else
    echo "⚠️  Tomcat ne semble pas avoir redémarré"
fi

echo ""
echo "✅ Déploiement terminé !"
echo "📝 URLs de test :"
echo "   http://localhost:8080/TestFramework/"
echo "   http://localhost:8080/TestFramework/test"
echo "   http://localhost:8080/TestFramework/hello"
echo ""
echo "📋 Logs : sudo tail -f /home/john/apache-tomcat-10.1.55/logs/catalina.out"