Sprint 1:
creer class controller
ex nom: mg.itu.elie.controller

test
creer class; declare comme controller execute dans tomcat

mila manana bout de code
soit executer au demarrage de l'application soit au premiere appelle du FrontServlet [Mampiasa Listner]

sprint1 : mila fantatra ny controller rehetra
ServletController   
List String listController

Boucler out.print(list)

Mijery anle annotation soit All(classpath) soit list package(le dev no mi fournir Package)
2 facons : soit par convention(asiana Controller ny farany) soit par configuration(annotation, configuration)

web.xml test variable package
dans la fonction init() recuperena aloha ny valeur anle package web.xml

class utils(ao @ framework): methode(nomPackage, annotaion, niveau(class, attribut, method))

Conseils: Decomposena foana ny asa.

------------------Raha mampiasa listner-----------------------
ao @ web.xml anle test no declarena ny listner

Sprint 2:
creer une nouvelle annotaion (au niveau methode) pour les URL(comme getMapping en Spring boot)
il liste la methode consigné a cette url ou s'il ne le connait pas, eleve une exception et liste les url qu'il connait

Sprint 3:
Test controller, url("/test") -> mandeha();



Sprint 3 bis: