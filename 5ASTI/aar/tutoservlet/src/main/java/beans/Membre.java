package beans;

import java.util.List;

public class Membre {

    /* ===============================================================
     *                         Properties
     * ===============================================================
     */

    /**
     * Login du membre.
     */
    private String login;

    /**
     * Mot de passe du membre.
     */
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


}
