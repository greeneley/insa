package controllers;

import beans.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import services.FacadeImpl;
import wrappers.WrapperCompetenceMembre;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@SessionAttributes({"courant", "membres", "projets", "competences"})
@RequestMapping("/")
public class Controleur {

    /* ===============================================================
     *                         Properties
     * ===============================================================
     */

    /**
     * Cette classe est un service permettant de gérer les opérations de backend.
     * Notamment gérer les projets, les membres et les compétences.
     */
    @Autowired
    private FacadeImpl facade;


    /* ===============================================================
     *                             Get
     * ===============================================================
     */

    /**
     * Page root du site web lorsqu'un utilisateur souhaite se connecter.
     * Si l'utilisateur est déjà loggué alors il est redirigé vers l'espace membre.
     * Sinon on lui affiche un formulaire de connexion.
     * @param session L'objet contenant la session actuelle.
     * @param model L'objet model du serveur.
     * @return Retourne le nom de la JSP à afficher.
     */
    @GetMapping(value="/")
    public String root(HttpSession session, Model model)
    {
        if(this.isLogged(model))
        {
            System.out.println("Controleur.root:isLogged");
            return "membre";
        }

        model.addAttribute("memberAccueil", new Membre());
        System.out.println("Controleur.root");
        return "accueil";
    }

    /**
     * Page permettant de se déconnecter de l'espace membre.
     * @param session La session en cours d'utilisation.
     * @param model L'obet model du serveur.
     * @return La page d'accueil avec une nouvelle session vierge.
     */
    @GetMapping(value="logout")
    public String logout(HttpSession session, Model model)
    {
        // Purge du model
        model.asMap().clear();

        // Renouveler la session
        session.invalidate();

        System.out.println("Controleur.logout");
        return "redirect:/";
    }


    /* ===============================================================
     *                            POST
     * ===============================================================
     */

    /**
     * Gestion du formulaire de connexion par le serveur.
     * Si les informations entrées sont correctes alors on le connecte à l'espace membre.
     * Sinon on le renvoie au formulaire avec un message d'erreur.
     * @param m L'objet membre issue du formulaire de connexion.
     * @param model L'objet model du serveur.
     * @return La page JSP à afficher.
     */
    @PostMapping(value="member")
    public String member(@ModelAttribute("memberAccueil") @Valid Membre m, BindingResult result, Model model)
    {
        // Le validator vérifie automatiquement les champs via les définitions de la classe.
        if(result.hasErrors())
        {
            System.out.println("Controleur.member:hasError");
            return "accueil";
        }

        Membre membreConnecte = this.facade.logMembre(m.getLogin(), m.getMotdepasse());
        if(membreConnecte != null)
        {
            // Ajout du membre courant dans la session
            // ... preferer ajouter seulement le login la prochaine fois
            model.addAttribute("courant", membreConnecte);

            // Ajout des variables globales dans la mémoire des serveurs
            model.addAttribute("membres", this.facade.getMembres());
            model.addAttribute("projets", this.facade.getProjets());
            model.addAttribute("competences", this.facade.getCompetences());

            System.out.println("Controleur.member:Menu");
            return "membre";
        }
        else
        {
            result.addError(new ObjectError("m", "Les informations entrées ne correspondent pas"));
            System.out.println("Controleur.member:Error");
            return "accueil";
        }
    }

    /**
     * Gestion du formulaire d emise à jour des compétences.
     * @param wcm Classe wrapper contenant les informations du formulaire.
     * @param model L'objet model du serveur.
     * @return La page "membre" avec les nouvelles informations.
     */
    @PostMapping(value="member/updateCompetence")
    public String updateCompetence(WrapperCompetenceMembre wcm, Model model)
    {
        // Récupération du membre courant de la session.
        Membre courant = (Membre)model.asMap().get("courant");

        // Récupération des compétences à ajouter / mettre à jour
        for(CompetenceMembre cm : wcm.getList())
        {
            if(cm == null) continue;
            courant.updateCompetence(this.facade.getLien().get(cm.getCompetence().getIntitule()),
                                     cm.getNiveau(),
                                     cm.getCommentaire()
            );
        }
        model.addAttribute("courant", courant);

        System.out.println("Controleur.updateCompetence - Added "+wcm.getList().size()+" items");
        return "membre";
    }

    /**
     * Gestion du formilaire d'ajout projet.
     * @param p Le projet issue du formulaire.
     * @param model L'objet model du serveur.
     * @return La page "membre" avec les nouvelles informations.
     */
    @PostMapping(value="member/addProjet")
    public String addProjet(Projet p, Model model)
    {
        // Identification du responsable
        Membre responsable = null;
        for(Membre m : this.facade.getMembres())
        {
            if(m.getLogin().equals(p.getResponsable().getLogin()))
            {
                responsable = m;
                break;
            }
        }

        // On vérifie que le membre responsable existe
        if(responsable == null)
        {
            System.out.println("Controleur.addProjet:null_responsable");
            return "membre";
        }

        // Création du projet et ajout du responsable
        Projet projet = new Projet(p.getIntitule(), p.getDescription());
        projet.setResponsable(responsable);

        // Ajout des competences requises pour le projet
        for(Competence c : p.getCompetencesRequises())
        {
            if(c==null) continue;
            projet.addCompetence(this.facade.getLien().get(c.getIntitule()));
        }
        this.facade.getProjets().add(projet);

        System.out.println("Controleur.addProjet:OK");
        return "membre";
    }

    /* ===============================================================
     *                           Methods
     * ===============================================================
     */

    /**
     * Détermine si le membre courant est actuellement loggué.
     * @param model L'objet model du serveur.
     * @return true si l'utilisateur est connecté, false sinon.
     */
   private boolean isLogged(Model model)
   {
       Membre m = (Membre)model.asMap().get("courant");
       if(m != null)
       {
           System.out.println("Controleur.isLogged:true - " + m.getLogin());
           return true;
       }
       else

       {
           System.out.println("Controleur.isLogged:false");
           return false;
       }
   }
}









