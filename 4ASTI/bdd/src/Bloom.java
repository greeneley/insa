import java.security.NoSuchAlgorithmException;

public class Bloom {
	
	private int k;
	private int[] tab;
	private MD5 h;
	
	final   int TAILLE_NORMALISEE_BYTE = 256;
	
	
	public void Bloom(int k)
	{
		this.k   = k;
		this.tab = new int[this.TAILLE_NORMALISEE_BYTE];
		for(int i=0; i<this.TAILLE_NORMALISEE_BYTE; i++)
		{
			this.tab[i] = 0;
		}
		
		try {
			this.h = new MD5();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void verify(String element)
	{
		this.h.update(element);
		this.h.digest();
		
		for(int i=0; i<this.k; i++)
		{		
			if(this.h.hacheK(i) != 1)
			{
				System.out.println("L'élément '"+element+"' ne fait pas partie du filtre.");
				return;
			}
			System.out.println("L'élément '"+element+"' fait partie du filtre");
		}
		
	}
	
	public static void main(String[] args)
	{
		//TODO : 	
	}
}
