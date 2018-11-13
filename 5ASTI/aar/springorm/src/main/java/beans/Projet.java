package beans;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Entity
public class Projet {

    /* ===============================================================
     *                         Properties
     * ===============================================================
     */

    /**
     * ID auto-incrémentée de pour la table.
     */
    @Id
    @GeneratedValue
    private int id;

    /**
     * L'intitulé du projet.
     */
    private String intitule;

    /**
     * La description du projet.
     */
    private String description;

    /**
     * Le membre responsable de ce projet.
     * Note: un responsable n'est pas forcément membre !
     */
    @ManyToOne
    private Membre responsable;

    /**
     * La liste des membres participants.
     */
    @ManyToMany
    private Set<Membre> participants;

    /**
     * La liste des compétences nécessaire au bon fonctionnement de ce projet.
     */
    @ManyToMany
    private Set<Competence> competencesRequises;


    /* ===============================================================
     *                        Constructors
     * ===============================================================
     */

    public Projet(){};

    /**
     * Crée un nouveau projet vide avec les paramètres fournis.
     * @param intitule
     * @param description
     */
    public Projet(String intitule, String description)
    {
        this.intitule = intitule;
        this.description = description;

        this.participants = new HashSet<>();
        this.competencesRequises = new HashSet<>();
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

    public Membre getResponsable() {
        return responsable;
    }

    public Set<Membre> getParticipants() {
        return participants;
    }

    public Set<Competence> getCompetencesRequises() {
        return competencesRequises;
    }


    /* ===============================================================
     *                           Setters
     * ===============================================================
     */

    public void setId(int id) {
        this.id = id;
    }

    public void setIntitule(String intituleP) {
        this.intitule = intituleP;
    }

    public void setDescription(String descriptionP) {
        this.description = descriptionP;
    }

    public void setResponsable(Membre responsable) {
        this.responsable = responsable;
    }

    public void setParticipants(Set<Membre> participants) {
        this.participants = participants;
    }

    public void setCompetencesRequises(Set<Competence> competencesRequis) {
        this.competencesRequises = competencesRequis;
    }


    /* ===============================================================
     *                           Methods
     * ===============================================================
     */

    /**
     * Ajoute un membre en tant que participant s'il n'est pas déjà participant.
     * @param participant
     */
    public void addParticipant(Membre participant)
    {
        if(!this.hasParticipant(participant))
        {
            this.participants.add(participant);
        }
    }

    /**
     * Retire le membre de la liste des participants.
     * @param participant
     */
    public void removeParticipant(Membre participant)
    {
        Iterator<Membre> i = this.participants.iterator();
        while(i.hasNext())
        {
            Membre m = i.next();
            if(m.equals(participant))
            {
                i.remove();
                break;
            }
        }
    }

    /**
     * Ajoute la compétence à la liste des compétences nécessaires s'il n'est pas présent.
     * @param competence
     */
    public void addCompetence(Competence competence)
    {
        if(!this.competencesRequises.contains(competence))
        {
            this.competencesRequises.add(competence);
        }
        competence.getProjets().add(this);
    }

    /**
     * Indique si le membre participe au projet ou non.
     * @param m Le membre à tester.
     * @return true si le membre participe; false sinon.
     */
    public boolean hasParticipant(Membre m)
    {
        return this.participants.contains(m);
    }

    /**
     * Indique si le membre possède une compétence nécessaire au projet.
     * @param m Le membre dont les compétences sont à tester.
     * @return true si au moins une compétence est nécessaire au projet.
     */
    public boolean isCompatible(Membre m)
    {
        for(Competence c : this.competencesRequises)
        {
            for(CompetenceMembre cm : m.getCompetences())
            {
                if(cm.getCompetence().equals(c)) return true;
            }
        }
        return false;
    }
}
