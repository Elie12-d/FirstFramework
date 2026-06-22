package annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) // indique a Java de conserver l'annotation dans le fichier .class 
// et la rendre accessible via la réflexion lors de l'execution.

@Target(ElementType.TYPE) // .TYPE pour les class
public @interface Controllerako {
    
}