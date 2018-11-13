package wrappers;

import beans.Competence;
import beans.Membre;

import java.util.List;

public class WrapperProjet {

    private Membre responsable;
    private String intitule;
    private String description;
    private List<Competence> competencesList;

    public Membre getResponsable() {
        return responsable;
    }

    public void setResponsable(Membre responsable) {
        this.responsable = responsable;
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Competence> getCompetencesList() {
        return competencesList;
    }

    public void setCompetencesList(List<Competence> competenceList) {
        this.competencesList = competenceList;
    }
}
