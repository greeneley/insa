package controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import services.Facade;
import beans.Membre;
import beans.Projet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import services.FacadeImpl;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@RequestMapping("/")
public class Controleur {

    @Autowired
    private FacadeImpl facade;

    @GetMapping(value="/")
    public String root()
    {
        System.out.println("Controleur.root");
        return "accueil";
    }

    @PostMapping(value="selogguer")
    public String selogguer(Membre m, Model model)
    {
        Membre membreConnecte = this.facade.logMembre(m.getLogin(), m.getMotdepasse());
        if(membreConnecte != null)
        {
            // on affiche la page de menu, en lui passant le membre qui vient de se logguer
            model.addAttribute("courant", membreConnecte);

            // On passe les différentes variables créées
            model.addAttribute("projets", this.facade.getProjets());
            model.addAttribute("membres", this.facade.getMembres());
            model.addAttribute("competences", this.facade.getCompetences());
            System.out.println("Controleur.doGet:selogguer:Menu");
            return "menu";
        }
        else
        {
            model.addAttribute("error", "Login ou mot de passe erroné.");
            System.out.println("Controleur.doGet:selogguer:Error");
            return "accueil";
        }
    }

        /*
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String todo=request.getParameter("TODO");

        if (todo == null ){
            request.getSession().invalidate();
            System.out.println("Controleur.doGet:null");
            request.getRequestDispatcher("/WEB-INF/accueil.jsp").forward(request,response);
        }
        else if (todo.equals("selogguer"))
        {


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
         */
}









