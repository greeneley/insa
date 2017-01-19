package eqsimple;


public class Complexe<T> implements Operations<T>
{
	private double reel;
	private double imaginaire;
	
	/* Setters */
	public void setReel(double nb)
	{
		this.reel = nb;	
	}
	
	public void setImaginaire(double nb)
	{
		this.imaginaire = nb;
	}
	
	/* Getters */
	public double getReel()
	{
		return this.reel;
	}
	
	public double getImaginaire()
	{
		return this.imaginaire;
	}
	
	/* Constructeurs */
	
	
	/* Methodes */
	
	public T addition(T b) {
		// TODO Auto-generated method stub
		return null;
	}

	public T soustraction(T b) {
		// TODO Auto-generated method stub
		return null;
	}

	public T multiplication(T b) {
		// TODO Auto-generated method stub
		return null;
	}

	public T division(T b) {
		// TODO Auto-generated method stub
		return null;
	}

	public T inverser(T b) {
		// TODO Auto-generated method stub
		return null;
	}

	public T opposer(T b) {
		// TODO Auto-generated method stub
		return null;
	}
}