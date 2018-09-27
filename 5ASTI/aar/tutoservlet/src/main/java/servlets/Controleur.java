package servlets;

import beans.Membre;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "Controleur", urlPatterns = "/")
public class Controleur extends HttpServlet {
    private List<Membre> membres;

    @Override
    public void init() throws ServletException {
        // Création des membres
        membres=new ArrayList<>();
        membres.add(new Membre("toto","toto","toto"));
        membres.add(new Membre("Fred","Fred","Fred"));
        membres.add(new Membre("Gerard", "Menfin", "Le Gourmet"));
        membres.add(new Membre("David", "Tennant", "Tenth"));
        membres.add(new Membre("Cave", "Johnson", "CEO"));

        // Création des projets
        // TODO
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String todo=request.getParameter("TODO");
        if (todo==null) {
            System.out.println("Controleur.doGet:null");
            request.getRequestDispatcher("/WEB-INF/accueil.jsp").
                    forward(request,response);
        } else {
            if (todo.equals("selogguer")) {
                System.out.println("Controleur.doGet:selogguer");
                String log=request.getParameter("login");
                String mdp=request.getParameter("mdp");
                for (Membre m :membres) {
                    if (m.getLogin().equals(log) &&
                            m.getMotdepasse().equals(mdp))
                    {
                        // le login est mis en session pour s'en souvenir...
                        request.getSession().setAttribute("util",log);

                        // on affiche la page de menu, en lui passant le membre qui vient de se logguer
                        request.setAttribute("courant",m);

                        System.out.println("Controleur.doGet:selogguer:Menu");
                        request.getRequestDispatcher("/WEB-INF/menu.jsp").
                                forward(request,response);
                    }
                }
                System.out.println("Controleur.doGet:selogguer:Error");;
                request.setAttribute("error", "Login ou mot de passe erroné.");
            }
        }
        request.getRequestDispatcher("/WEB-INF/accueil.jsp").forward(request,response);
    }
}









