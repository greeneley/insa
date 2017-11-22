import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

/**
 * Trivial example showing how to use Choco Solver
 * to solve the equation system
 * x + y < 5
 * x * y = 4
 * with x in [0,5] and y in {2, 3, 8}
 *
 * @author Charles Prud'homme, Jean-Guillaume Fages
 * @since 9/02/2016
 */
public class Main {

    public static void main(String[] args) {
    	
    	/* ====== EXEMPLES ======= */
        // 1. Create a Model
        Model model = new Model("my first problem");
        // 2. Create variables
        IntVar x = model.intVar("X", 0, 5);                 // x in [0,5]
        IntVar y = model.intVar("Y", new int[]{2, 3, 8});   // y in {2, 3, 8}
        // 3. Post constraints
        model.arithm(x, "+", y, "<", 5).post(); // x + y < 5
        model.times(x,y,4).post();              // x * y = 4
        // 4. Solve the problem
        model.getSolver().solve();
        // 5. Print the solution
        System.out.println(x); // Prints X = 2
        System.out.println(y); // Prints Y = 2
        
        
        /* =====================================
           ====== DISTRIBUTEUR DE MONNAIE ====== 
           ===================================== */
        
        // ======== CREATION DU MODELE =========
        Model exo1 = new Model("Retour de monnaie");
        
        // ============ CONSTANTES =============
        // Les valeurs representent le nombre de pieces dans la machine
        int T   = 200;
        int P   = 40;
        
        int E1  = 50; 
        int E2  = 50;
        int C50 = 50;
        int C20 = 50;
        int C10 = 10;
      
        // ============ VARIABLES ==============
        IntVar x1 = exo1.intVar("T", 0, 50);
        IntVar y1 = exo1.intVar("P", 0, 50);
        
        IntVar a  = exo1.intVar("a", 0, E1);
        IntVar b  = exo1.intVar("b", 0, E2);
        IntVar c  = exo1.intVar("c", 0, C50);
        IntVar d  = exo1.intVar("d", 0, C20);
        IntVar e  = exo1.intVar("e", 0, C10);
        
        // =========== CONTRAINTES ============
        
      
        
    }
}
