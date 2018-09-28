package beans;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Projet {

    /* ===============================================================
     *                         Properties
     * ===============================================================
     */

    private String intitule;

    private String description;

    private Membre responsable;

    private List<Membre> participants;

    private List<Competence> competencesRequises;


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

        this.participants = new ArrayList<>();
        this.competencesRequises = new ArrayList<>();
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

    public Membre getResponsable() {
        return responsable;
    }

    public List<Membre> getParticipants() {
        return participants;
    }

    public List<Competence> getCompetencesRequises() {
        return competencesRequises;
    }


    /* ===============================================================
     *                           Setters
     * ===============================================================
     */

    public void setIntitule(String intituleP) {
        this.intitule = intituleP;
    }

    public void setDescription(String descriptionP) {
        this.description = descriptionP;
    }

    public void setResponsable(Membre responsable) {
        this.responsable = responsable;
    }

    public void setParticipants(List<Membre> participants) {
        this.participants = participants;
    }

    public void setCompetencesRequises(List<Competence> competencesRequis) {
        this.competencesRequises = competencesRequis;
    }


    /* ===============================================================
     *                           Methods
     * ===============================================================
     */
    public void addParticipant(Membre participant)
    {
        this.participants.add(participant);
    }

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

    public void addCompetence(Competence competence)
    {
        if(!this.competencesRequises.contains(competence))
        {
            this.competencesRequises.add(competence);
        }
    }

    public boolean hasParticipant(Membre m)
    {
        return this.participants.contains(m);
    }

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
