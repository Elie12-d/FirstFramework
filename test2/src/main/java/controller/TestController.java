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
import main.java.annotation.Auth;
import main.java.session.Session;

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

    @Auth
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

    @ApiREST
    @UrlMapping(url = "/session/add", method = HttpMethode.POST)
    public String ajouterSession(Session session) {
        session.setAttribute("nom", "Rakoto");
        return "Attribut nom ajouté avec succès";
    }

    @ApiREST 
    @UrlMapping (url = "/session/remove", method = HttpMethode.GET)
    public String removeSession(Session session) {
        session.removeAttribute("nom");
        return "La cle nom a ete supprime avec succes";

    }

    @ApiREST
    @UrlMapping (url = "/session/update", method = HttpMethode.POST)
    public String modifierSession(Session session) {
        session.setAttribute("nom", "Jean");
        return "Nom modifié avec succès";
    }

    @ApiREST
    @UrlMapping(url = "/session/get", method = HttpMethode.GET)
    public String lireSession(Session session) {
        Object nom = session.getAttribute("nom");
        if (nom == null) {
            return "Aucun nom dans la session";
        }
        return "Nom enregistré : " + nom;
    }

    @ApiREST
    @UrlMapping(url = "/user/add", method = HttpMethode.GET)
    public String addUser(Session session) {
        session.setAttribute("user", "utilisateur");
        return "user add successfully";
    }

}