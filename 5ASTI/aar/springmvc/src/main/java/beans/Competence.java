package beans;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

public class Competence {

    /* ===============================================================
     *                         Properties
     * ===============================================================
     */

    /**
     * L'intitulé de la compétence.
     */
    @NotEmpty
    private String intitule;

    /**
     * La description de la compétence.
     */
    private String description;

    /**
     * La liste des projets nécessitant la présence de cette compétence.
     */
    private List<Projet> projets;

    /**
     * La liste des CompetenceMembre liés à cette compétence.
     */
    private List<CompetenceMembre> competenceMembres;


    /* ===============================================================
     *                         Constructor
     * ===============================================================
     */

    public Competence(){};

    public Competence(String intitule, String description)
    {
        this.intitule = intitule;
        this.description = description;

        this.projets = new ArrayList<>();
        this.competenceMembres = new ArrayList<>();
    }


    /* ===============================================================
     *                           Getters
     * ===============================================================
     */

    public String getIntitule() {
        return intitule;
    }

    public String getDescription() {
        return description;
    }

    public List<Projet> getProjets() {
        return projets;
    }

    public List<CompetenceMembre> getCompetenceMembres() {
        return competenceMembres;
    }


    /* ===============================================================
     *                           Setters
     * ===============================================================
     */

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setProjets(List<Projet> projets) {
        this.projets = projets;
    }

    public void setCompetenceMembres(List<CompetenceMembre> competenceMembres) {
        this.competenceMembres = competenceMembres;
    }


    /* ===============================================================
     *                           Methods
     * ===============================================================
     */
}
