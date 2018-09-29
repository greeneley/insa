package servlets;

import Modele.Facade;
import beans.Competence;
import beans.Membre;
import beans.Projet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletConfig;
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

    @Autowired
    private Facade facade;

    @Override
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);
        System.out.println("Controleur.init");
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String todo=request.getParameter("TODO");

        if (todo == null ){
            request.getSession().invalidate();
            System.out.println("Controleur.doGet:null");
            request.getRequestDispatcher("/WEB-INF/accueil.jsp").forward(request,response);
        }
        else if (todo.equals("selogguer"))
        {
            String log=request.getParameter("login");
            String mdp=request.getParameter("mdp");
            Membre membreConnecte = this.facade.getConnection(log, mdp);
            if(membreConnecte != null)
            {
                // Utilisation de la session
                HttpSession session = request.getSession();

                // on affiche la page de menu, en lui passant le membre qui vient de se logguer
                session.setAttribute("courant", membreConnecte);

                // On passe les différentes variables créées
                session.setAttribute("projets", this.facade.getProjets());
                session.setAttribute("membres", this.facade.getMembres());
                session.setAttribute("competences", this.facade.getCompetences());
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
                courant.updateCompetence(this.facade.getLien().get(intitule), niveau, commentaire);
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
            for(Membre m : this.facade.getMembres())
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
                p.addCompetence(this.facade.getLien().get(intituleCompetence));
            }
            this.facade.getProjets().add(p);
            request.getSession().setAttribute("projets", this.facade.getProjets());

            System.out.println("Controleur.doGet:actualiseProjets:OK");
            request.getRequestDispatcher("/WEB-INF/menu.jsp").forward(request, response);
        }
        else{
            System.out.println("Controleur.doGet:else");
            request.getRequestDispatcher("/WEB-INF/accueil.jsp").forward(request, response);
        }
    }
}









