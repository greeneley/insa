package sgbd.impl;

import java.lang.reflect.Array;
import java.util.ArrayList;
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
		int i = 0;
		int j = 0;
		
		// On join d'abord les indexes
		// TODO: Revoir l'algo : il y a un probleme avec le produit cartesien
		while( (i!=this.tri_t1.length) && (j!=this.tri_t2.length) )
		{
			int val1 = (int)this.tri_t1[i].getAtt(att1);
			int val2 = (int)this.tri_t2[j].getAtt(att2);
			if(val1 == val2)
			{
				join.add(new Integer[]{i,j});
				j++;
			}
			else if(val1 < val2)
			{
				i++;
			}
			else
			{
				j++;
			}
		}
		
		// Puis on join les Nuplets
		Nuplet[] result = new Nuplet[]
		return null;
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
				index_t1[i] = new int[] {i, (int)t1[i].getAtt(att1)};
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			try
			{
				index_t2[i] = new int[] {i, (int)t2[i].getAtt(att2)};
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
