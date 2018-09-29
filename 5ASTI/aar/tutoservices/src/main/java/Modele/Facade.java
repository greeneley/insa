package Modele;

import beans.Competence;
import beans.Membre;
import beans.Projet;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Service
public class Facade {

    /* ===============================================================
     *                         Properties
     * ===============================================================
     */

    private List<Membre> membres;
    private List<Competence> competences;
    private List<Projet> projets;
    private HashMap<String, Competence> lien;


    /* ===============================================================
     *                         Init
     * ===============================================================
     */

    @PostConstruct
    public void init()
    {
        // -----------------------------------
        //      Création des competences
        // -----------------------------------

        // --- Competences sportives
        Competence compFootball = new Competence("Football", "Performance au football");
        Competence compHandball = new Competence("Handball", "Performance au handball");
        Competence compNatation = new Competence("Natation", "Performance en natation");

        // --- Competences musicales
        Competence compViolon   = new Competence("Violon", "Maîtrise du violon");
        Competence compPiano    = new Competence("Piano", "Maîtrise du piano");
        Competence compChant    = new Competence("Chant", "Maîtrise du chant");

        // --- Competences informatiques
        Competence compCode = new Competence("Programmation", "Niveau de programmation");
        Competence compDesign = new Competence("Design", "Niveau de design");
        Competence compRapidite = new Competence("Rapidite", "Vitesse de travail");

        // --- Sauvegarde
        this.competences = new ArrayList<>();
        Collections.addAll(this.competences, compFootball, compHandball, compNatation,
                compViolon, compPiano, compChant,
                compCode, compDesign, compRapidite);

        this.lien = new HashMap<>();
        for(Competence c : this.competences)
        {
            this.lien.put(c.getIntitule(), c);
        }

        // -----------------------------------
        //      Création des membres
        // -----------------------------------

        // --- Creation individuel et ajout des competences
        Membre tt = new Membre("toto","toto","toto");
        tt.addCompetence(compViolon, 4, "Rien");
        tt.addCompetence(compPiano, 7, "Rien");
        tt.addCompetence(compFootball, 4, "Rien");

        Membre ff = new Membre("Fred","Fred","Fred");
        Membre dt = new Membre("David_Tennant", "doctorwho", "Tenth");
        Membre mm = new Membre("Marty_McFly", "backtothefuture", "Chicken");
        Membre gv = new Membre("Garrus_Vakarian", "masseffect", "Archangel");
        Membre lo = new Membre("Lena_Oxton", "overwatch", "Tracer");
        Membre em = new Membre("Ellen_Mclain", "aperturescience", "GLaDOS");

        // --- Sauvegarde
        this.membres = new ArrayList<>();
        Collections.addAll(membres, tt, ff, dt, mm, gv, lo, em);


        // -----------------------------------
        //      Création des projets
        // -----------------------------------

        // --- Projet sportif
        Projet projetSport = new Projet("Sport", "Entrainement pour les compétitions d'été.");
        projetSport.addCompetence(compFootball);
        projetSport.addCompetence(compHandball);
        projetSport.addCompetence(compNatation);
        projetSport.setResponsable(gv); // Garrus

        // --- Projet musical
        Projet projetMusique = new Projet("Musique", "Préparation pour le Printemps de Bourges.");
        projetMusique.addCompetence(compViolon);
        projetMusique.addCompetence(compPiano);
        projetMusique.addCompetence(compChant);
        projetMusique.setResponsable(tt); // toto
        projetMusique.addParticipant(tt);

        // --- Sauvegarde
        this.projets = new ArrayList<>();
        Collections.addAll(this.projets, projetSport, projetMusique);
    }

    /* ===============================================================
     *                         Methods
     * ===============================================================
     */

    public List<Membre> getMembres() {
        return membres;
    }

    public List<Competence> getCompetences() {
        return competences;
    }

    public List<Projet> getProjets() {
        return projets;
    }

    public HashMap<String, Competence> getLien() {
        return lien;
    }
    /* ===============================================================
     *                         Methods
     * ===============================================================
     */

    public Membre getConnection(String login, String motdepasse)
    {
        Membre membreConnecte = null;
        for (Membre m : this.membres)
        {
            if (m.getLogin().equals(login) &&
                    m.getMotdepasse().equals(motdepasse))
            {
                membreConnecte = m;
                break;
            }
        }
        return membreConnecte;
    }

}
