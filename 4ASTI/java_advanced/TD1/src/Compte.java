
public class Compte {
	private int solde = 0;
	private boolean sem = false;
	
	public void ajouter(int somme)
	{
		solde += somme;
		//System.out.print(" ajoute " + somme);
	}
	
	public void retirer(int somme)
	{
		solde -= somme;
		//System.out.print(" retire " + somme);
	}
	
	public void operationNulle(int somme)
	{
		solde += somme;
		//System.out.print(" ajoute " + somme);
		solde -= somme;
		//System.out.print(" retire " + somme);
	}
	
	public int getSolde()
	{
		return solde;
	}
	
	public void lockSem()
	{
		sem = true;
	}
	
	public void openSem()
	{
		sem = false;
	}
	
	public boolean getSem()
	{
		return sem;
	}
}
