package beans;

import javax.persistence.*;

@Entity
public class CompetenceMembre {

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
     * Valeur numérique représentant le niveau de la compétence.
     * Min: 0; Max: 20
     */
    private int niveau;

    /**
     * Commentaire personnalisé sur la compétence du membre.
     */
    private String commentaire;

    /**
     * Le membre auquel est liée cette compétence.
     */
    @ManyToOne
    private Membre membre;

    /**
     * La compétence associée.
     */
    @ManyToOne
    private Competence competence;


    /* ===============================================================
     *                         Constructor
     * ===============================================================
     */

    public CompetenceMembre(){};

    public CompetenceMembre(Membre m, Competence competence, int niveau, String commentaire)
    {
        this.competence  = competence;
        this.niveau      = niveau;
        this.commentaire = commentaire;
        this.membre      = m;

        competence.getCompetenceMembres().add(this);
    }


    /* ===============================================================
     *                           Getters
     * ===============================================================
     */

    public int getId() {
        return id;
    }

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

    public void setId(int id) {
        this.id = id;
    }

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
