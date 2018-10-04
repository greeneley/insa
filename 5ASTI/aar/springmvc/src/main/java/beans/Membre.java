package beans;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

public class Membre {

    /* ===============================================================
     *                         Properties
     * ===============================================================
     */

    /**
     * Login du membre.
     */
    @NotEmpty
    @Size(min = 3)
    private String login;

    /**
     * Mot de passe du membre.
     */
    @NotEmpty
    private String motdepasse;

    /**
     * Surnom du membre.
     */
    private String surnom;

    /**
     * Liste des compétences du membre;
     */
    private List<CompetenceMembre> competences;

    /**
     * Liste des projets dont le membre participe.
     */
    private List<Projet> projetsParticipant;

    /**
     * Liste des projets dont le membre est responsable (mais pas nécessairement participant).
     */
    private List<Projet> projetsResponsable;


    /* ===============================================================
     *                         Constructor
     * ===============================================================
     */

    /**
     * Constructeur par défaut
     */
    public Membre(){};

    /**
     * Crée un nouveau membre avec les informations fournies.
     * @param login Le login du membre.
     * @param motdepasse Le mot de passe du membre.
     * @param surnom Le surnom du membre.
     */
    public Membre(String login, String motdepasse, String surnom)
    {
        this.login      = login;
        this.motdepasse = motdepasse;
        this.surnom     = surnom;

        this.competences = new ArrayList<>();
        this.projetsResponsable = new ArrayList<>();
        this.projetsParticipant = new ArrayList<>();
    }

    /* ===============================================================
     *                           Getters
     * ===============================================================
     */

    public String getLogin() {
        return login;
    }

    public String getMotdepasse() {
        return motdepasse;
    }

    public String getSurnom() {
        return surnom;
    }

    public List<CompetenceMembre> getCompetences() {
        return competences;
    }

    public List<Projet> getProjetsParticipant() {
        return projetsParticipant;
    }

    public List<Projet> getProjetsResponsable() {
        return projetsResponsable;
    }

    /* ===============================================================
     *                           Setters
     * ===============================================================
     */

    public void setLogin(String login) {
        this.login = login;
    }

    public void setMotdepasse(String motdepasse) {
        this.motdepasse = motdepasse;
    }

    public void setSurnom(String surnom) {
        this.surnom = surnom;
    }

    public void setCompetences(List<CompetenceMembre> competences) {
        this.competences = competences;
    }

    public void setProjetsParticipant(List<Projet> projetsParticipant) {
        this.projetsParticipant = projetsParticipant;
    }

    public void setProjetsResponsable(List<Projet> projetsDirigeant) {
        this.projetsResponsable = projetsDirigeant;
    }

    /* ===============================================================
     *                         Methods
     * ===============================================================
     */

    @Override
    public boolean equals(Object o)
    {
        if(o == null) return false;
        if(o == this) return true;
        if(!(o instanceof Membre)) return false;

        Membre test = (Membre)o;
        if(this.login == test.login
            && this.motdepasse == test.motdepasse
            && this.surnom     == test.surnom)
        {
            return true;
        }
        return false;
    }

    public void addCompetence(Competence competence, int niveau, String commentaires)
    {
        this.competences.add(new CompetenceMembre(competence, niveau, commentaires));
    }

    public boolean isResponsable(Projet projet)
    {
        return this.equals(projet.getResponsable());
    }

    public void updateCompetence(Competence competence, int niveau, String commentaire)
    {
        boolean isNew = true;
        for(CompetenceMembre c : this.competences)
        {
            if(c.getCompetence().getIntitule() == competence.getIntitule())
            {
                c.setNiveau(niveau);
                c.setCommentaire(commentaire);
                isNew = false;
                break;
            }
        }

        if(isNew)
        {
            this.addCompetence(competence, niveau, commentaire);
        }
    }
}
