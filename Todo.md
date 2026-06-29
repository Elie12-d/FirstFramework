# Creation framework
- Creation de repository depuis gitHub
- Clonage dans vscode
- creatin de strucure framework et test
- coder le framework
- generation de jar (manuel)
- utilisation du jar dans le projet test
- creation de script sh pour automatisation

## To do list pour sprint1
**Objectif:** Connaitre tous les class controller a l'aide d'une annotation controller(package fourni par le devellopeur)

**Tasks:**
- [x] Creation de l'annotation (se documenter)
- [x]Fabriquer le framework :
    - [x]Getter la valeur du variable par le web.xml
    - [x]Fonctioon init() qui sera appeller une fois (bout de code pour faire marcher les fonctionnalite)
    - [x]Lancer la recherche des Class qui ont l'annotation (fonctions dans class utils)
        - [x]fabriquer une fonction qui retourne une liste de class filtrer par ses package (argument package)
        - [x]fabriquer une fonction qui cherche les annotaion qui existe dans une class et qui retourne cette liste de class(argument listClasse,annotation, niveau[Class, attribute, method])
    - [x]Stocker les cette list dans list String puis out.println(list).
- [x]transformation en JAR

- [x]Tester le framework: (compilation avec le JAR)
    - [x]declarer la variable et definis sa valeur dans le web.xml
    - [x]Creer des class et mettre des annotation, puis voir le resultat dans tomcat pour virifie si le framework marche bien

// pull request adino sprint1

## To do list pour sprint 2
**Object:** Connaitre(lister) les methodes assignées a des URL sinon exception avec liste qu'il connait

**Tasks:**
- [ ] Creation de l'annotaion avec target.METHOD
- [ ] La fonctionnalite
  - [ ] Creation fonction 