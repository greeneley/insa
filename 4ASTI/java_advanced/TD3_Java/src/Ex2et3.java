import java.util.Scanner;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Ex2et3 {
	
	public static void exo2()
	{
		/* Variables */
		File fd;
		FileOutputStream fichier;
		
		/* Algorithme */
		
		System.out.println("Entrer un chemin relatif vers un fichier à créer");
		Scanner scan = new Scanner(System.in);
		fd = new File(scan.next());
		
		if((fd.exists()))
		{
			System.out.println("Un fichier de même nom existe déjà.\nFin du programme.");
		}
		else
		{
			System.out.println("Creation d'un fichier et ecriture...");
			try {
				fichier = new FileOutputStream(fd);
				fichier.write(new byte[] {1,2,3,4,5,6,7,8,9});
				fichier.close();	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("... done.");
		}
	}
	
	
	public static void exo3()
	{
		/* Variables */
		String name;
		File dd;
		File fd;
		FileOutputStream stream;
		DataOutputStream fichier;
		
		/* Algorithme */
		System.out.println("Nous allons creer un un fichier 'donnees.txt'");
		System.out.println("Entrer un chemin (absolu ou relatif) vers le repertoire ou ecrire :");
		Scanner scan = new Scanner(System.in);
		
		name = scan.next();
		dd = new File(name);
		
		if(name.endsWith("/"))
		{
			name += "donnees.txt";
		}
		else
		{
			name += "/donnees.txt";
		}
		fd = new File(name);
		
		if((fd.exists()))
		{
			System.out.println("Erreur : un fichier 'donnees.txt' existe deja");
		}
		else
		{
			try {
				System.out.println("Creation du/des repertoire(s)...");
				if(dd.mkdirs())
				{
					stream  = new FileOutputStream(fd);
					fichier = new DataOutputStream(stream);
					System.out.println("...Creation d'un fichier...");
					fichier.writeBytes("Donnees inutilisables entrantes et sortantes");
					fichier.close();
					stream.close();
				}	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("... done.");
		}
	}
	
	public static void main(String[] args) 
	{
		//exo2()
		exo3();
	}

}
