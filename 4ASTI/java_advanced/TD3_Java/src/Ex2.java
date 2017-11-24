import java.util.Scanner;
import java.io.File;

public class Ex2 {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		/* Variables */
		File fichier;
		
		/* Algorithme */
		
		System.out.println("Entrer un chemin relatif vers un fichier à créer");
		Scanner scan = new Scanner(System.in);
		fichier = new File(scan.next());
		
		if((fichier.exists()))
		{
			System.out.println("Un fichier de même nom existe déjà");
			
		}

	}

}
