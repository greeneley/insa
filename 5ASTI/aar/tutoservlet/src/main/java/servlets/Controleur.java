package servlets;

import beans.Competence;
import beans.Membre;
import beans.Projet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@WebServlet(name = "Controleur", urlPatterns = "/")
public class Controleur extends HttpServlet {
    private List<Membre> membres;
    private List<Competence> competences;
    private List<Projet> projets;
    private HashMap<String, Competence> lien;

    @Override
    public void init() throws ServletException
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

        System.out.println("Controleur.init");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String todo=request.getParameter("TODO");

        if (todo == null ){
            System.out.println("Controleur.doGet:null");
            request.getRequestDispatcher("/WEB-INF/accueil.jsp").forward(request,response);
        }
        else if (todo.equals("selogguer"))
        {
            String log=request.getParameter("login");
            String mdp=request.getParameter("mdp");
            Membre membreConnecte = null;
            for (Membre m :membres)
            {
                if (m.getLogin().equals(log) &&
                        m.getMotdepasse().equals(mdp))
                {
                    membreConnecte = m;
                    break;
                }
            }

            if(membreConnecte != null)
            {
                // Utilisation de la session
                HttpSession session = request.getSession();

                // le login est mis en session pour s'en souvenir...
                session.setAttribute("util",log);

                // on affiche la page de menu, en lui passant le membre qui vient de se logguer
                session.setAttribute("courant", membreConnecte);

                // On passe les différentes variables créées
                session.setAttribute("projets", this.projets);
                session.setAttribute("membres", this.membres);
                session.setAttribute("competences", this.competences);

                System.out.println("Controleur.doGet:selogguer:Menu");
                request.getRequestDispatcher("/WEB-INF/menu.jsp").forward(request,response);
            }
            else
            {
                request.setAttribute("error", "Login ou mot de passe erroné.");
                System.out.println("Controleur.doGet:selogguer:Error");
                request.getRequestDispatcher("/WEB-INF/accueil.jsp").forward(request,response);
            }
        }
        else if (todo.equals("actualiseCompetence"))
        {
            int limite = Integer.parseInt(request.getParameter("numberOptions"));
            Membre courant = (Membre)request.getSession().getAttribute("courant");
            for(int i=0; i<=limite ; i++)
            {
                String intitule = request.getParameter("select"+Integer.toString(i));
                int niveau = Integer.parseInt(request.getParameter("niveau"+Integer.toString(i)));
                String commentaire = request.getParameter("commentaire"+Integer.toString(i));
                courant.updateCompetence(this.lien.get(intitule), niveau, commentaire);
            }
            request.getSession().setAttribute("courant", courant);

            System.out.println("Controleur.doGet:actualiseCompetence");
            request.getRequestDispatcher("/WEB-INF/menu.jsp").forward(request, response);
        }
        else if (todo.equals("actualiseProjets"))
        {
            // Récupération des informations immédiates
            int limite = Integer.parseInt(request.getParameter("numberOptions"));
            String intitule = request.getParameter("intitule-projet");
            String description = request.getParameter("description-projet");
            String nomResponsable = request.getParameter("responsable");

            // Identification du responsable
            Membre responsable = null;
            for(Membre m : this.membres)
            {
                if(m.getLogin().equals(nomResponsable))
                {
                    responsable = m;
                    break;
                }
            }
            if(responsable == null)
            {
                System.out.println("Controleur.doGet:actualiseProjets:null_responsable");
                request.getRequestDispatcher("/WEB-INF/accueil.jsp").forward(request, response);
                return;
            }

            // Création du projet et ajout des compétences
            Projet p = new Projet(intitule, description);
            p.setResponsable(responsable);
            for(int i=0; i<=limite; i++)
            {
                String intituleCompetence = request.getParameter("select"+Integer.toString(i));
                p.addCompetence(this.lien.get(intituleCompetence));
            }
            this.projets.add(p);
            request.getSession().setAttribute("projets", this.projets);

            System.out.println("Controleur.doGet:actualiseProjets:OK");
            request.getRequestDispatcher("/WEB-INF/menu.jsp").forward(request, response);
        }
        else{
            System.out.println("Controleur.doGet:else");
            request.getRequestDispatcher("/WEB-INF/accueil.jsp").forward(request, response);
        }
    }
}









