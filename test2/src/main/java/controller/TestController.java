package main.java.controller;

import java.util.ArrayList;
import java.util.List;

import main.java.annotation.ApiREST;
import main.java.annotation.Controller;
import main.java.annotation.UrlMapping;
import main.java.entity.Etudiant;
import main.java.http.HttpMethode;
import main.java.view.ModelAndView;

@Controller
public class TestController {

    @UrlMapping(url = "/hello", method = HttpMethode.GET)
    public ModelAndView hello() {
        ModelAndView mv = new ModelAndView("elie");
        mv.addObject("message", "Bonjour depuis le framework");
        List<Etudiant> etudiants = new ArrayList<>();
        etudiants.add(new Etudiant(1, "Elie"));
        etudiants.add(new Etudiant(2, "Naina"));
        mv.addObject("etudiants", etudiants);
        return mv;
    }

    @UrlMapping(url = "/api/etudiants", method = HttpMethode.GET)
    @ApiREST
    public List<Etudiant> getEtudiants() {
        List<Etudiant> etudiants = new ArrayList<>();
        etudiants.add(new Etudiant(1, "Elie"));
        etudiants.add(new Etudiant(2, "esther"));
        return etudiants;
    }
    
    @ApiREST
    @UrlMapping (url = "/save", method = HttpMethode.POST)
    public String save(String nom, String prenom) {
        return "Nom: " + nom + ", Prenom: " + prenom;
    }
}