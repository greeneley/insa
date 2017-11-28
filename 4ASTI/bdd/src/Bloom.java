import java.security.NoSuchAlgorithmException;

public class Bloom {
	
	private int k;
	private int[] tab;
	private MD5 h;
	
	final   int TAILLE_NORMALISEE_BYTE = 256;
	
	public Bloom(int k)
	{
		this.k   = k;
		
		// Init du filtre a 0
		this.tab = new int[this.TAILLE_NORMALISEE_BYTE];
		for(int i=0; i<this.TAILLE_NORMALISEE_BYTE; i++)
		{
			this.tab[i] = 0;
		}
		
		// Init de la fonction de hachage
		try {
			this.h = new MD5();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void verify(String element)
	{
		// Init de la fonction de hachage
		this.h.update(element);
		this.h.digest();
		
		// Verification		
		for(int i=0; i<this.k; i++)
		{		
			if(this.tab[this.h.hacheK(i)] != 1)
			{
				System.out.println("L'élément '"+element+"' ne fait pas partie du filtre.");
				return;
			}
		}
		System.out.println("L'élément '"+element+"' fait partie du filtre");
	}
	
	public void add(String element)
	{
		this.h.update(element);
		this.h.digest();
		
		for(int i=0; i<this.k; i++)
		{
			this.tab[this.h.hacheK(i)] = 1;
		}
		
		System.out.println("L'élément '"+element+"' a ete ajoute au filtre.");
	}
	
	public static void main(String[] args)
	{
		Bloom filtre = new Bloom(6);
		filtre.add("Reservoir Dogs");
		filtre.add("Shining");
		filtre.add("Inception");
		
		System.out.println("");
		
		filtre.verify("Shining");
		filtre.verify("Brazil");
		filtre.verify("Strech");
	}
}
