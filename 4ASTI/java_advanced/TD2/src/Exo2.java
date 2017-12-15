
public class Exo2 
{

	public static void main(String[] args) 
	{
		int[] tab = {1};
		Producteur   prod  = new Producteur(tab);
		Consommateur cons1 = new Consommateur(tab);
		Consommateur cons2 = new Consommateur(tab);
		
		prod.start();
		cons1.start();
		cons2.start();
	}

}
