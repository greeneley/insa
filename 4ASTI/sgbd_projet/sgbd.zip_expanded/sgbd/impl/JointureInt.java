package sgbd.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Hashtable;

import sgbd.operateurs.Jointure;
import sgbd.stockage.Nuplet;

public class JointureInt implements Jointure 
{
	
	public Nuplet[] tri_t1;
	public Nuplet[] tri_t2;

	/* (non-Javadoc)
	 * @see sgbd.operateurs.Jointure#jointureBl(sgbd.stockage.Nuplet[], sgbd.stockage.Nuplet[], int, int)
	 */
	@Override
	public Nuplet[] jointureBl(Nuplet[] t1, Nuplet[] t2, int att1, int att2) 
	{
		// ===== VERIFICATION =====
		if(t1.length==0 || t2.length==0)
		{
			System.out.println("Erreur de dimension d'un des arguments");
			return null;
		}
		
		/* ====================================
		 * ============= Variables ============
		 * ====================================
		*/
		int     nb_match        = 0;
		int[][] indexes         = new int[t1.length*t2.length*t1.length][2];
		int     taille_jointure = t1[0].size() + t2[0].size();
		int     taille_uplet_t1 = t1[0].size();
		
		
		/* ====================================
		 * ============ Algorithme ============
		 * ====================================
		*/
		
		// Parcours de t1
		for(int i=0; i<t1.length; i++)
		{
			// Parcours de t2
			for(int j=0; j<t2.length; j++)
			{
				// Si match
				if(t1[i].getAtt(att1) == t2[j].getAtt(att2))
				{
					// Le couple (i,j) a indexes[k] represente
					// les nuplets de t1 et t2 a joindre
					indexes[nb_match] = new int[] {i,j};
					nb_match++;
				}
			}
		}
		
		// ===== Creation du resultat =====
		Nuplet[] result = new NupletInt[nb_match];
		
		// Pour chaque couple (t1,t2) dans index, on va les concatener
		for(int index=0; index<nb_match; index++)
		{
			// On ajoute un nouvel Nuplet dans le resultat
			NupletInt subNuplet = new NupletInt(taille_jointure);
			
			// D'abord on recopie betement les donnees des k Nuplet de t1
			for(int i=0; i<taille_uplet_t1; i++)
			{
				subNuplet.putAtt(i, t1[indexes[index][0]].getAtt(i) );
			}
			
			// Ensuite on ajoute les donnees du Nuplet de t2 qui match avec celui de t1
			for(int i=0; i<t2[0].size(); i++)
			{
				subNuplet.putAtt(i+taille_uplet_t1, t2[indexes[index][1]].getAtt(i));
			}
			
			// Puis on ajoute le resultat au Nuplet[] final
			result[index] = subNuplet;
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see sgbd.operateurs.Jointure#jointureH(sgbd.stockage.Nuplet[], sgbd.stockage.Nuplet[], int, int)
	 */
	@Override
	public Nuplet[] jointureH(Nuplet[] t1, Nuplet[] t2, int att1, int att2) 
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
		int[][] indexes  = new int[t1.length*t2.length*t1.length][2];
		int     nb_match = 0;
		
		// Pour la jointure
		int taille_jointure = t1[0].size() + t2[0].size(); 
		int taille_uplet_t1 = t1[0].size();
		
		
		/* ====================================
		 * ============ Algorithme ============
		 * ====================================
		*/
		// Construction du tableau de hache : on hache tous les elements de t2
		// Je mets t2 dans le hashtable car c'est la meme chose que si t1 etait dans la table
		for(int i=0; i<t2.length; i++)
		{
			valeur = t2[i].getAtt(att2);
			hash   = valeur.toString();
			array  = hashes.get(hash);
			
			// Si le hash est nouveau
			if(array == null)
			{
				// On le cree et on ajoute l'index du Nuplet
				array = new ArrayList<Integer>();
				array.add(i);
				hashes.put(hash, array);
			}
			else
			{
				// Il existe, on ajoute juste son index
				array.add(i);
			}
		}
	
		// Recherche des matchs dans t1
		for(int i=0; i<t1.length; i++)
		{
			// Acces direct par la cle du hash obtenu de t1
			array = hashes.get(t1[i].getAtt(att1).toString());
			if(array != null)
			{
				// Si match, on joint selon les index trouves dans les valeurs du hashtable
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
				
			// Recopie de t1
			for(int i=0; i<taille_uplet_t1; i++)
			{
				subNuplet.putAtt(i, t1[indexes[index][0]].getAtt(i) );
			}
					
			// Ajout des elements de t2
			for(int i=0; i<t2[0].size(); i++)
			{
				subNuplet.putAtt(i+taille_uplet_t1, t2[indexes[index][1]].getAtt(i));
			}
			result[index] = subNuplet;
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see sgbd.operateurs.Jointure#jointureS(sgbd.stockage.Nuplet[], sgbd.stockage.Nuplet[], int, int)
	 * 
	 * Effectue une jointure de type sort merge.
	 * Pour cela, on a d'abord besoin d'un tri fusion des n-uplets.
	 * Puis on effectue ensuite la jointure en sort-merge.
	 */
	@Override
	public Nuplet[] jointureS(Nuplet[] t1, Nuplet[] t2, int att1, int att2) 
	{
		// ===== Verification =====
		if(t1.length==0 || t2.length==0)
		{
			System.out.println("Erreur de dimension d'un des arguments.");
			return null;
		}
		
		// On trie les deux Nuplets via un tri fusion
		this.triFusion(t1, t2, att1, att2);
		
		/* ====================================
		 * ============= JOINTURE  ============
		 * ====================================
		*/
		
		/* ====================================
		 * ============= Variables ============
		 * ====================================
		*/
		ArrayList<Integer[]> join = new ArrayList<Integer[]>();
		int size_uplets1  = t1[0].size();
		int size_uplets2  = t2[0].size();
		int taille_uplet  = size_uplets1 + size_uplets2;
		int index_t1      = 0;
		int index_t2      = 0;
		
		// === Algo
		// On join d'abord les indexes
		while( (index_t1<this.tri_t1.length) && (index_t2<this.tri_t2.length) )
		{
			byte val1 = (byte)this.tri_t1[index_t1].getAtt(att1);
			byte val2 = (byte)this.tri_t2[index_t2].getAtt(att2);
			
			// Si les valeurs match, on va faire un produit cartesien
			if(val1 == val2)
			{
				// Tant que le produit cartesien est possible
				while(index_t2<this.tri_t2.length && (byte)val1 == (byte)this.tri_t2[index_t2].getAtt(att2))
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
				subNuplet.putAtt(j, this.tri_t1[join.get(i)[0]].getAtt(j) );
			}
					
			for(int j=0; j<+size_uplets2; j++)
			{
				subNuplet.putAtt(j+size_uplets1, this.tri_t2[join.get(i)[1]].getAtt(j) );
			}
			result[i] = subNuplet;
		}
		return result;
	}
		
	public void triFusion(Nuplet[] t1, Nuplet[] t2, int att1, int att2)
	{
		/* ====================================
		 * ============= Variables ============
		 * ====================================
		*/
		// Pour les tri-fusions
		int[][] index_t1 = new int[t1.length][2];
		int[][] index_t2 = new int[t2.length][2];
		int     max_size_t;
		
		// Pour l'output de la procedure
		this.tri_t1   = new Nuplet[t1.length];
		this.tri_t2   = new Nuplet[t2.length];
				
		/* ====================================
		 * ============ Algorithme ============
		 * ====================================
		*/
		if(t1.length>t2.length)
		{
			max_size_t = t1.length;
		}
		else
		{
			max_size_t = t2.length;
		}
		
		// On va creer (salement) un tableau de {index, valeur_index} pour le tri
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
				this.tri_t2[i] = t2[index_t2[i][0]];
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}

}
