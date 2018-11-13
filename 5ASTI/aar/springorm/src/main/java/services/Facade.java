package services;

import beans.Competence;
import beans.CompetenceMembre;
import beans.Membre;
import beans.Projet;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.*;

@Service
public class Facade{

    /* ===============================================================
     *                         Properties
     * ===============================================================
     */

    @PersistenceContext
    private EntityManager em;


    /* ===============================================================
     *                             Init
     * ===============================================================
     */

    @Transactional
    public void init()
    {
        // -----------------------------------
        //         Init des variables
        // -----------------------------------
        List<Competence> competences;
        List<Membre> membres;
        List<Projet> projets;
        Map<String, Competence> lien;

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
        competences = new ArrayList<>();
        Collections.addAll(competences, compFootball, compHandball, compNatation,
                compViolon, compPiano, compChant,
                compCode, compDesign, compRapidite);

        lien = new HashMap<>();
        for(Competence c : competences)
        {
            lien.put(c.getIntitule(), c);
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
        membres = new ArrayList<>();
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
        projets = new ArrayList<>();
        Collections.addAll(projets, projetSport, projetMusique);

        // -----------------------------------
        //           Insertion BDD
        // -----------------------------------
        for(Competence c : competences)
        {
            this.em.persist(c);
        }

        for(Projet p : projets)
        {
            this.em.persist(p);
        }

        for(Membre m : membres)
        {
            this.em.persist(m);
        }
    }


    /* ===============================================================
     *                         CREATE DataBase
     * ===============================================================
     */
    @Transactional
    public void createProjet(Projet p)
    {
        this.em.persist(p);
    }

    /* ===============================================================
     *                         READ DataBase
     * ===============================================================
     */

    @Transactional
    public List<Membre> getMembres()
    {
        Query query = this.em.createQuery("select m from Membre m");
        List<Membre> lm = (List<Membre>) query.getResultList();
        for(Membre m : lm)
        {
            m.getProjetsParticipant().size();
            m.getProjetsResponsable().size();
            m.getCompetences().size();
        }
        return lm;
    }

    @Transactional
    public Membre getMembre(String login)
    {
        for (Membre m : this.getMembres())
        {
            if(m.getLogin().equals(login))
            {
                return m;
            }
        }
        return null;
    }

    @Transactional
    public Competence getCompetence(String intitule)
    {
        Query query = this.em.createQuery("select c from Competence c where c.intitule = :i");
        query.setParameter("i", intitule);
        Competence c = (Competence) query.getSingleResult();
        c.getProjets().size();
        c.getCompetenceMembres().size();
        return c;
    }

    @Transactional
    public List<Competence> getCompetences()
    {
        Query query = this.em.createQuery("select c from Competence c");
        List<Competence> lc = (List<Competence>) query.getResultList();
        return lc;
    }

    @Transactional
    public List<Projet> getProjets()
    {
        Query query = this.em.createQuery("select p from Projet p");
        List<Projet> lp = (List<Projet>) query.getResultList();
        for(Projet p : lp)
        {
            p.getCompetencesRequises().size();
            p.getParticipants().size();
        }
        return lp;
    }


    /* ===============================================================
     *                         UPDATE DataBase
     * ===============================================================
     */
    @Transactional
    public void updateMembre(Membre m)
    {
        this.em.merge(m);
    }

    /* ===============================================================
     *                         Methods
     * ===============================================================
     */

    @Transactional
    public Membre logMembre(String login, String motdepasse)
    {
        Query query = this.em.createQuery("select m from Membre m where m.login = :l and m.motdepasse = :m");
        query.setParameter("l", login);
        query.setParameter("m", motdepasse);

        // Init de tous les objets pour le hors scope
        Membre m = (Membre)query.getSingleResult();
        m.getCompetences().size();
        m.getProjetsResponsable().size();
        m.getProjetsParticipant().size();

        return m;
    }


}
