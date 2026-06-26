package controller;

import annotation.Controllerako;
import annotation.GetMapping;

@Controllerako
public class ElieController {
    
    @GetMapping("/")  // ← Mapping pour la racine
    public String index() {
        return "Page d'accueil";
    }
    
    @GetMapping("/hello")
    public String hello() {
        return "Hello World";
    }
}