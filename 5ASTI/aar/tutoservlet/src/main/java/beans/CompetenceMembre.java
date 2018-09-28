package beans;

public class CompetenceMembre {

    /* ===============================================================
     *                         Properties
     * ===============================================================
     */

    private int niveau;

    private String commentaire;

    private Membre membre;

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
