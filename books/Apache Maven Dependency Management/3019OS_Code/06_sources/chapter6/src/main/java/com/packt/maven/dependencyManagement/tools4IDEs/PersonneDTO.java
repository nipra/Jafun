package com.packt.maven.dependencyManagement.tools4IDEs;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

//import org.hibernate.validator.constraints.NotEmpty;

@ManagedBean
@SessionScoped
public class PersonneDTO implements Serializable {
    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private String nom;

    private String prenom;

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
