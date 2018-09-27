package beans;

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
}
