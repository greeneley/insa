package sgbd.console;

import java.util.Scanner;

public class Shell 
{
	private String    input;
	private SGBD      sgbd;
	
	public Shell()
	{
		this.sgbd   = new SGBD();
		
		System.out.println("Init shell done");
	}
	
	/**
	 * Remplace les ensemble d'un ou plusieurs espaces par un seul espace 
	 */
	public void normalizeInput()
	{
		this.input = this.input.replaceAll("\\s+", " ");
	}
	
	public void deleteSpaces(String s)
	{
		s.replaceAll("\\s+", "");
	}
	
	/**
	 * Permet de lire et interpreter les inputs de l'utilisateur. 
	 */
	public void read()
	{
		Scanner sc = new Scanner(System.in);  

		while(this.sgbd.isRunning())
		{
			// On recupere l'input de l'utilisateur
			System.out.print(this.sgbd.getPrompt());;
			this.input = sc.next();
			this.input += sc.nextLine();

			this.normalizeInput();
			if(!this.sgbd.parse(this.input))
			{
				System.out.println("Requete non reconnue");
			}
			System.out.println("");
		}
		
		sc.close();
		
		System.out.println("FIN DU TEST CONSOLE");
	}
}
