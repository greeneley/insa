package beans;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Membre {

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
     * Sete des compétences du membre;
     */
    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "membre")
    private Set<CompetenceMembre> competences;

    /**
     * Sete des projets dont le membre participe.
     */
    @ManyToMany(mappedBy = "participants")
    private Set<Projet> projetsParticipant;

    /**
     * Sete des projets dont le membre est responsable (mais pas nécessairement participant).
     */
    @OneToMany(mappedBy = "responsable")
    private Set<Projet> projetsResponsable;


    /* ===============================================================
     *                         Constructor
     * ===============================================================
     */

    /**
     * Constructeur par défaut
     */
    public Membre(){}

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

        this.competences = new HashSet<>();
        this.projetsResponsable = new HashSet<>();
        this.projetsParticipant = new HashSet<>();
    }

    /* ===============================================================
     *                           Getters
     * ===============================================================
     */
    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getMotdepasse() {
        return motdepasse;
    }

    public String getSurnom() {
        return surnom;
    }

    public Set<CompetenceMembre> getCompetences() {
        return competences;
    }

    public Set<Projet> getProjetsParticipant() {
        return projetsParticipant;
    }

    public Set<Projet> getProjetsResponsable() {
        return projetsResponsable;
    }


    /* ===============================================================
     *                           Setters
     * ===============================================================
     */

    public void setId(int id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setMotdepasse(String motdepasse) {
        this.motdepasse = motdepasse;
    }

    public void setSurnom(String surnom) {
        this.surnom = surnom;
    }

    public void setCompetences(Set<CompetenceMembre> competences) {
        this.competences = competences;
    }

    public void setProjetsParticipant(Set<Projet> projetsParticipant) {
        this.projetsParticipant = projetsParticipant;
    }

    public void setProjetsResponsable(Set<Projet> projetsDirigeant) {
        this.projetsResponsable = projetsDirigeant;
    }


    /* ===============================================================
     *                         Methods
     * ===============================================================
     */

    /**
     * Redéfinition de la méthode equals pour la classe Membre.
     * @param o L'objet à tester.
     * @return true si c'est le même membre, false sinon.
     */
    @Override
    public boolean equals(Object o)
    {
        if(o == null) return false;
        if(o == this) return true;
        if(!(o instanceof Membre)) return false;

        Membre test = (Membre)o;
        return this.login.equals(test.getLogin())
                && this.motdepasse.equals(test.getMotdepasse())
                && this.surnom.equals(test.getSurnom());
    }

    /**
     * Ajoute une competence au membre.
     * @param competence La competence à ajouter.
     * @param niveau Le niveau de la compétence.
     * @param commentaires Un commentaire.
     */
    public void addCompetence(Competence competence, int niveau, String commentaires)
    {
        this.competences.add(new CompetenceMembre(this, competence, niveau, commentaires));
    }

    /**
     * Indique si le membre est responsable du projet.
     * @param projet Le projet a tester.
     * @return true si le membre est responsable, false sinon.
     */
    public boolean isResponsable(Projet projet)
    {
        return this.equals(projet.getResponsable());
    }

    /**
     * Ajoute ou met à jour une CompetenceMembre au membre.
     * @param competence La competence à mettre à jour.
     * @param niveau Le niveau de la compétence.
     * @param commentaire Un commentaire à propos de la CompetenceMembre.
     */
    public void updateCompetence(Competence competence, int niveau, String commentaire)
    {
        boolean isNew = true;
        for(CompetenceMembre c : this.competences)
        {
            if(c.getCompetence().getIntitule().equals(competence.getIntitule()))
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
