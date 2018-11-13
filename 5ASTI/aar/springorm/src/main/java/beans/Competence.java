package beans;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Competence {

    /* ===============================================================
     *                         Properties
     * ===============================================================
     */

    /**
     * ID auto-incrémentée pour la base.
     */
    @Id
    @GeneratedValue
    private int id;

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
    @ManyToMany(mappedBy = "competencesRequises")
    private Set<Projet> projets;

    /**
     * La liste des CompetenceMembre liés à cette compétence.
     */
    @OneToMany(mappedBy = "competence")
    private Set<CompetenceMembre> competenceMembres;


    /* ===============================================================
     *                         Constructor
     * ===============================================================
     */

    public Competence(){};

    public Competence(String intitule, String description)
    {
        this.intitule = intitule;
        this.description = description;

        this.projets = new HashSet<>();
        this.competenceMembres = new HashSet<>();
    }


    /* ===============================================================
     *                           Getters
     * ===============================================================
     */

    public int getId() {
        return id;
    }

    public String getIntitule() {
        return intitule;
    }

    public String getDescription() {
        return description;
    }

    public Set<Projet> getProjets() {
        return projets;
    }

    public Set<CompetenceMembre> getCompetenceMembres() {
        return competenceMembres;
    }


    /* ===============================================================
     *                           Setters
     * ===============================================================
     */

    public void setId(int id) {
        this.id = id;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setProjets(Set<Projet> projets) {
        this.projets = projets;
    }

    public void setCompetenceMembres(Set<CompetenceMembre> competenceMembres) {
        this.competenceMembres = competenceMembres;
    }


    /* ===============================================================
     *                           Methods
     * ===============================================================
     */
}
