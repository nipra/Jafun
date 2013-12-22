package com.packt.maven.dependencyManagement.tools4IDEs;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "syntheseBean")
@ViewScoped
public class SyntheseBean implements Serializable {

    private String nom;

    private String prenom;

    public SyntheseBean() {
        super();
    }

    public String goToGestionRessource() {
        return "gestionRessource";
    }

    public String goToGestionCadre() {
        return "gestionCadre";
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

}
