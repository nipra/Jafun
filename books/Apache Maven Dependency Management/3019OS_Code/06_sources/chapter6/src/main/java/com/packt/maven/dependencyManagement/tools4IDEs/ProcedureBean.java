package com.packt.maven.dependencyManagement.tools4IDEs;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.event.ActionEvent;

@ManagedBean
public class ProcedureBean {

    @ManagedProperty(value = "#{personneDTOIHM}")
    private PersonneDTOIHM personneDTOIHM;

    @ManagedProperty(value = "#{personneDTO}")
    private PersonneDTO personneDTO;

    public ProcedureBean() {
        super();
    }

    public void enregistrerValidationListener(ActionEvent event) {
        System.out.println("coucou");
    }

    public String enregistrer() {
        //		FacesContext context = FacesContext.getCurrentInstance();
        //		HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
        //		PersonneDTO personneDTO  = (PersonneDTO)session.getAttribute("personneDTO");

        System.out.println("ENRES");
        this.personneDTO.setNom(this.getPersonneDTOIHM().getNom());
        this.personneDTO.setPrenom(this.getPersonneDTOIHM().getPrenom());

        System.out.println(" Nom : " + personneDTO.getNom());
        System.out.println(" Prenom : " + personneDTO.getPrenom());

        //		syntheseBean.setNom(this.nom);
        //		syntheseBean.setPrenom(this.prenom);
        //		syntheseBean.setDateNaissance(this.dateNaissance);

        return null;
    }

    public PersonneDTOIHM getPersonneDTOIHM() {
        return personneDTOIHM;
    }

    public void setPersonneDTOIHM(PersonneDTOIHM personneDTOIHM) {
        this.personneDTOIHM = personneDTOIHM;
    }

    public PersonneDTO getPersonneDTO() {
        return personneDTO;
    }

    public void setPersonneDTO(PersonneDTO personneDTO) {
        this.personneDTO = personneDTO;
    }

}
