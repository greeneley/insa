package sgbd.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import sgbd.operateurs.Jointure;
import sgbd.stockage.Nuplet;

public class JointureS implements Jointure {
	
	private Nuplet[] tri_t1;
	private Nuplet[] tri_t2;

	@Override
	public Nuplet[] jointure(Nuplet[] t1, Nuplet[] t2, int att1, int att2) 
	{
		// ===== Verification =====
		if(t1.length==0 || t2.length==0)
		{
			System.out.println("Erreur de dimension d'un des arguments.");
			return null;
		}
		
		// On trie les deux Nuplets
		this.triFusion(t1, t2, att1, att2);
		
		// ===== Jointure =====
		// === Variables
		ArrayList<Integer[]> join = new ArrayList<Integer[]>();
		int size_uplets1  = t1[0].size();
		int size_uplets2  = t2[0].size();
		int taille_uplet  = size_uplets1 + size_uplets2;
		int index_t1      = 0;
		int index_t2      = 0;
		
		// === Algo
		// On join d'abord les indexes
		while( (index_t1!=this.tri_t1.length) && (index_t2!=this.tri_t2.length) )
		{
			byte val1 = (byte)this.tri_t1[index_t1].getAtt(att1);
			byte val2 = (byte)this.tri_t2[index_t2].getAtt(att2);
			
			// Si les valeurs match, on va faire un produit cartesien
			if(val1 == val2)
			{
				// Tant que le produit cartesien est possible
				while(val1 == (byte)this.tri_t2[index_t2].getAtt(att2))
				{
					join.add(new Integer[]{index_t1, index_t2});
					index_t2++;
				}
				
				index_t2=index_t1;
				index_t1++;
			}
			// Dans les autres cas, on avance dans les deux arrays
			else if(val1 < val2)
			{
				index_t1++;
			}
			else
			{
				index_t2++;
			}
		}
		
		// === Output
		Nuplet[] result = new Nuplet[join.size()];
		for(int i=0; i<join.size(); i++)
		{
			Nuplet subNuplet = new NupletInt(taille_uplet);
			
			for(int j=0; j<size_uplets1; j++)
			{
				subNuplet.putAtt(j, t1[join.get(i)[0]].getAtt(j) );
			}
					
			for(int j=0; j<+size_uplets2; j++)
			{
				subNuplet.putAtt(j+size_uplets1, t2[join.get(i)[0]].getAtt(j) );
			}
			result[i] = subNuplet;
		}
		return result;
	}
	
	
	public void triFusion(Nuplet[] t1, Nuplet[] t2, int att1, int att2)
	{
		// ===== Variables =====
		// Pour les tri-fusions
		int[][] index_t1 = new int[t1.length][2];
		int[][] index_t2 = new int[t2.length][2];
		int     max_size_t;
		
		// Pour l'output de la procedure
		this.tri_t1   = new Nuplet[t1.length];
		this.tri_t2   = new Nuplet[t2.length];
				
		// ===== Algorithme =====
		if(t1.length>t2.length)
		{
			max_size_t = t1.length;
		}
		else
		{
			max_size_t = t2.length;
		}
		
		// On va creer (salement) un un tableau de {index, valeur_index} pour le tri
		for(int i=0; i<max_size_t; i++)
		{
			try
			{
				index_t1[i] = new int[] {i, (byte)t1[i].getAtt(att1)};
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			try
			{
				index_t2[i] = new int[] {i, (byte)t2[i].getAtt(att2)};
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
		// On applique un tri-fusion sur les deux int[][]
		Arrays.sort(index_t1, Comparator.comparingInt(arr -> arr[1]));
		Arrays.sort(index_t2, Comparator.comparingInt(arr -> arr[1]));
		
		// On cree (encore salement) les nouveaux Nuplets tries
		for(int i=0; i<max_size_t; i++)
		{
			try
			{
				this.tri_t1[i] = t1[index_t1[i][0]];
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			try
			{
				this.tri_t2[i] = t2[index_t1[i][0]];
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}

}
