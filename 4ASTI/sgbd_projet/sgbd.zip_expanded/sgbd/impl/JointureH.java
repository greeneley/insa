package sgbd.impl;

import java.util.ArrayList;
import java.util.Hashtable;

import sgbd.operateurs.Jointure;
import sgbd.stockage.Nuplet;

public class JointureH implements Jointure {

	@Override
	public Nuplet[] jointure(Nuplet[] t1, Nuplet[] t2, int att1, int att2) 
	{
		if(t1.length==0 || t2.length==0)
		{
			System.out.println("Erreur de dimension d'un des arguments !");
			return null;
		}		
		
		/* ====================================
		 * ============= Variables ============
		 * ====================================
		*/
		// Pour la table de hache
		Hashtable<String, ArrayList<Integer>> hashes = new Hashtable<String, ArrayList<Integer>>(t1.length);
		ArrayList<Integer>                    array;
		Object                                valeur;
		String                                hash;
		
		// Pour les matchs t1/t2
		//ArrayList<int[]> indexes  = new ArrayList<int[]>();
		int[][] indexes  = new int[t1.length*t2.length*t1.length][2];
		int     nb_match = 0;
		
		// Pour la jointure
		int taille_jointure = t1[0].size() + t2[0].size(); 
		int taille_uplet_t1 = t1[0].size();
		
		
		/* ====================================
		 * ============ Algorithme ============
		 * ====================================
		*/
		// Construction du tableau de hache
		// Je met t2 dans le hashtable car c'est la meme chose que si t1 etait dans la table
		for(int i=0; i<t2.length; i++)
		{
			valeur = t2[i].getAtt(att2);
			hash   = valeur.toString();
			array  = hashes.get(hash);
			
			if(array == null)
			{
				array = new ArrayList<Integer>();
				array.add(i);
				hashes.put(hash, array);
			}
			else
			{
				array.add(i);
			}
		}
	
		// Recherche des matchs dans t1
		for(int i=0; i<t1.length; i++)
		{
			array = hashes.get(t1[i].getAtt(att1).toString());
			if(array != null)
			{
				for(int index_t2 : array)
				{
					//indexes.add(new int[] {i, index_t2});
					indexes[nb_match] = new int[] {i,index_t2};
					nb_match++;
				}
			}
		}
		
		// Jointure
		Nuplet[] result = new NupletInt[nb_match];
		
		for(int index=0; index<nb_match; index++)
		{
			Nuplet subNuplet = new NupletInt(taille_jointure);
				
			for(int i=0; i<taille_uplet_t1; i++)
			{
				subNuplet.putAtt(i, t1[indexes[index][0]].getAtt(i) );
			}
					
			for(int i=0; i<t2[0].size(); i++)
			{
				subNuplet.putAtt(i+taille_uplet_t1, t2[indexes[index][1]].getAtt(i));
			}
			result[index] = subNuplet;
		}
		return result;
	}

}
