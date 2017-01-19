package eqsimple;

public class Rationnel implements Operations<Rationnel>{

	private int numerateur;
	private int denominateur;
	
	
	/* Setters */
	public void setNumerateur(int nb)
	{
		this.numerateur = nb;
	}
	
	public void setDenominateur(int nb)
	{
		if(nb!=0)
		{
			this.denominateur = nb;
		}
		else
		{
			System.out.println("Erreur b est nul !");
		}
	}
	
	
	/* Getters */
	public int getNumerateur()
	{
		return this.numerateur;
	}
	
	public int getDenominateur()
	{
		return this.denominateur;
	}
	
	public double getValeur()
	{
		if(this.getDenominateur()!=0)
		{
			double result = (this.getNumerateur())/(this.getDenominateur());
			return result;
		}
		else
		{
			System.out.println("Valeur non definie !");
			return 0;
		}
	}
	
	
	/* Constructeurs */
	public Rationnel()
	{
		this.setNumerateur(0);
		this.setDenominateur(1);
	}
	
	public Rationnel(int nbA, int nbB)
	{
		this.setNumerateur(nbA);
		this.setDenominateur(nbB);
	}
	
	
	/* Methodes */
	public void inverser()
	{
		int tmp = this.getNumerateur();
		this.setNumerateur(this.getDenominateur());
		this.setDenominateur(tmp);
	}
	
	public void opposer()
	{
		this.setNumerateur(-this.getNumerateur());
	}

	public Rationnel addition(Rationnel a) {
		// TODO Auto-generated method stub
		return null;
	}

	public Rationnel soustraction(Rationnel n) {
		// TODO Auto-generated method stub
		return null;
	}

	public Rationnel multiplication(Rationnel b) {
		// TODO Auto-generated method stub
		return null;
	}

	public Rationnel division(Rationnel b) {
		// TODO Auto-generated method stub
		return null;
	}

	public Rationnel inverser(Rationnel b) {
		// TODO Auto-generated method stub
		return null;
	}

	public Rationnel opposer(Rationnel b) {
		// TODO Auto-generated method stub
		return null;
	}

}
