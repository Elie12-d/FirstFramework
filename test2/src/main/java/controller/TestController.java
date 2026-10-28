package main.java.controller;

import java.util.ArrayList;
import java.util.List;

import main.java.annotation.ApiREST;
import main.java.annotation.Controller;
import main.java.annotation.UrlMapping;
import main.java.entity.Etudiant;
import main.java.http.HttpMethode;
import main.java.view.ModelAndView;
import main.java.prs.Personne;
import main.java.upload.UploadedFile;

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

    @UrlMapping(url = "/save", method = HttpMethode.POST)
    @ApiREST
    public String save(Personne personne) {

        System.out.println(personne.getNom());
        System.out.println(personne.getPrenom());
        System.out.println(personne.getAge());

        return "Personne enregistrée avec succès : " + personne.getNom() + " " + personne.getPrenom() + ", Age: "
                + personne.getAge();
    }
    // @UrlMapping (url = "/save", method = HttpMethode.POST)
    // @ApiREST
    // public String test(String nom, String prenom, int age) {
    // return "Personne enregistrée avec succès : " + nom + " " + prenom + ", Age: "
    // + age;
    // }

    @UrlMapping(url = "/upload", method = HttpMethode.POST)
    public ModelAndView upload(UploadedFile document) {
        try {
            String chemin = document.save();
            System.out.println("Chemin : " + chemin);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModelAndView("elie");
    }
}