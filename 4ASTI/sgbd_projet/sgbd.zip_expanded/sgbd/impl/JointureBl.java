package sgbd.impl;

import sgbd.operateurs.Jointure;
import sgbd.stockage.Nuplet;

public class JointureBl implements Jointure {

	@Override
	public Nuplet[] jointure(Nuplet[] t1, Nuplet[] t2, int att1, int att2) 
{
		// ===== VERIFICATION =====
		if(t1.length==0 || t2.length==0)
		{
			System.out.println("Erreur de dimision d'un des arguments");
			return null;
		}
		
		// ===== VARIABLES =====
		int     nb_match        = 0;
		int[][] indexes         = new int[t1.length*t2.length*t1.length][2];
		int     taille_jointure = t1[0].size() + t2[0].size();
		int     taille_uplet_t1 = t1[0].size();
		
		// ===== ALGORITHME =====
		for(int i=0; i<t1.length; i++)
		{
			for(int j=0; j<t2.length; j++)
			{
				if(t1[i].getAtt(att1) == t2[j].getAtt(att2))
				{
					// Le couple (i,j) a indexes[k] represente
					//    les nuplets de t1 et t2 a concatener
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
			// On ajouter un nouvel Nuplet dans le resultat
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

}
