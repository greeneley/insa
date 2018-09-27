package beans;

import java.util.List;

public class Competence {

    /* ===============================================================
     *                         Properties
     * ===============================================================
     */

    private String intitule;

    private String description;

    private List<Projet> projets;

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
