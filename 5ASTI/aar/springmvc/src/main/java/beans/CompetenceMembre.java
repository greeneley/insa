package beans;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

public class CompetenceMembre {

    /* ===============================================================
     *                         Properties
     * ===============================================================
     */

    /**
     * Valeur numérique représentant le niveau de la compétence.
     * Min: 0; Max: 20
     */
    @Min(0)
    @Max(20)
    @NotEmpty
    private int niveau;

    /**
     * Commentaire personnalisé sur la compétence du membre.
     */
    private String commentaire;

    /**
     * Le membre auquel est liée cette compétence.
     */
    private Membre membre;

    /**
     * La compétence associée.
     */
    private Competence competence;


    /* ===============================================================
     *                         Constructor
     * ===============================================================
     */

    public CompetenceMembre(){};

    public CompetenceMembre(Competence competence, int niveau, String commentaire)
    {
        this.competence  = competence;
        this.niveau      = niveau;
        this.commentaire = commentaire;
    }


    /* ===============================================================
     *                           Getters
     * ===============================================================
     */

    public int getNiveau() {
        return niveau;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public Membre getMembre() {
        return membre;
    }

    public Competence getCompetence() {
        return competence;
    }


    /* ===============================================================
     *                           Setters
     * ===============================================================
     */

    public void setNiveau(int niveau) {
        this.niveau = niveau;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public void setMembre(Membre membre) {
        this.membre = membre;
    }

    public void setCompetence(Competence competence) {
        this.competence = competence;
    }


    /* ===============================================================
     *                           Methods
     * ===============================================================
     */

}
