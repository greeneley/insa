import java.util.concurrent.TimeUnit;

public class Compteur extends Thread {
	
	/* ================= VARIABLES =============== */
	
	protected String name;
	protected int    n;
	protected int    valeurMin = 0;
	protected int    valeurMax = 3000;
	protected static int POS   = 1;
	
	/* =============== CONSTRUCTORS ============== */
	
	public Compteur(int n)
	{
		this.setName("MonCompteur");
		this.setN(n);
	}
	
	public Compteur(String name, int n)
	{
		this.setName(name);
		this.setN(n);
	}
	
	/* ================= GET/SET ================= */
	
	public String getNameCompteur() 
	{
		return name;
	}

	public void setNameCompteur(String newName) 
	{
		this.name = newName;
	}

	public int getN() 
	{
		return n;
	}

	public void setN(int value) 
	{
		this.n = value;
	}
	
	public int getPos() 
	{
		return Compteur.POS;
	}

	public void setPos(int value) 
	{
		Compteur.POS = value;
	}
	
	/* ================ START ==================== */
	public void run()
	{
		try{
			this.compte();
			System.out.println(" à la position "+this.getPos());
			this.incrPos();
		}
		catch (InterruptedException e){
			System.err.println("Le processus a ete interrompu");
			return;
		}
	}
	
	/* ================= METHODES ================ */
	
	public void affiche()
	{
		System.out.println(this.getName()+" : "+this.getN());
	}
	
	public void compte() throws InterruptedException
	{
		
		for(int i=0; i<this.getN(); i++)
		{
			System.out.println(this.getName()+" : "+i);
			TimeUnit.MILLISECONDS.sleep(this.generate());
		}
		System.out.print("*** "+this.getName()+" a fini de compter jusqu'à "+this.getN());
	}
	
	public int generate()
	{
		int random;
		random = (int)(Math.random()*(this.valeurMax - this.valeurMin +1)) + this.valeurMin;
		return random;
	}
	
	
		/* ====== POSITION ====== */
	public void incrPos() 
	{
		this.setPos(this.getPos()+1);
	}
	
	public void resetPos()
	{
		this.setPos(1);
	}

}
