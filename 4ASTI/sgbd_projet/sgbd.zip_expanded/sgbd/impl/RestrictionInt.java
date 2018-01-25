package sgbd.impl;

import sgbd.operateurs.Restriction;
import sgbd.stockage.Nuplet;

public class RestrictionInt implements Restriction {

	/**
	 * Renvoie l'ensemble des Nuplets dont la colonne att est egale a v
	 * 
	 * @see sgbd.operateurs.Restriction#egalite(sgbd.stockage.Nuplet[], int, java.lang.Object)
	 */
	@Override
	public Nuplet[] egalite(Nuplet[] t, int att, Object v) 
	{
		int nb_result = 0;
		int[] egaux   = new int[t.length];
		
		for(int i=0; i<t.length; i++)
		{
			// On va chercher a stocker les indices egaux
			if((byte)t[i].getAtt(att) == (byte)v)
			{
				// Par ordre chronologique, l'indice
				egaux[nb_result] = i;
				nb_result++;
			}
		}
		
		Nuplet[] result = new NupletInt[nb_result];
		/* Result ne contiendra que les Nuplet egaux
		 * car on n'y a stocke que les indices
		 */
		for(int i=0; i<nb_result; i++)
		{
			result[i] = t[egaux[i]];
		}
		return result;
	}

	/**
	 * Renvoie l'ensemble des Nuplets dont la colonne att est >= a v
	 * 
	 * @see sgbd.operateurs.Restriction#superieur(sgbd.stockage.Nuplet[], int, java.lang.Object)
	 */
	@Override
	public Nuplet[] superieur(Nuplet[] t, int att, Object v) {
		int nb_result = 0;
		int[] egaux = new int[t.length];
		
		for(int i=0; i<t.length; i++)
		{
			if((byte)t[i].getAtt(att) >= (byte)v)
			{
				egaux[nb_result] = i;
				nb_result++;
			}
		}
		
		Nuplet[] result = new NupletInt[nb_result];
		for(int i=0; i<nb_result; i++)
		{
			result[i] = t[egaux[i]];
		}
		return result;
	}

	/**
	 * Renvoie l'ensemble des Nuplets dont la colonne att est <= a v
	 * 
	 * @see sgbd.operateurs.Restriction#superieur(sgbd.stockage.Nuplet[], int, java.lang.Object)
	 */
	@Override
	public Nuplet[] inferieur(Nuplet[] t, int att, Object v) {
		int nb_result = 0;
		int[] egaux = new int[t.length];
		
		for(int i=0; i<t.length; i++)
		{
			if((byte)t[i].getAtt(att) <= (byte)v)
			{
				egaux[nb_result] = i;
				nb_result++;
			}
		}
		
		Nuplet[] result = new NupletInt[nb_result];
		for(int i=0; i<nb_result; i++)
		{
			result[i] = t[egaux[i]];
		}
		return result;
	}

	
	/**
	 * Renvoie l'ensemble des Nuplet dont l'indice dans la table est <= a index
	 * 
	 * @param t     La table concernee
	 * @param index L'index a comparer
	 * @return      Les Nuplets qui remplissent la condition
	 */
	public Nuplet[] inferieurId(Nuplet[] t, int index) 
	{		
		int max = t.length<=index ? t.length : index+1; 
		Nuplet[] result = new NupletInt[index+1];
		for(int i=0; i<max; i++)
		{
			result[i] = t[i];
		}
		return result;
	}

	/**
	 * Renvoie l'ensemble des Nuplet dont l'indice dans la table est >= a index
	 * 
	 * @param t     La table concernee
	 * @param index L'index a comparer
	 * @return      Les Nuplets qui remplissent la condition
	 */
	public Nuplet[] superieurId(Nuplet[] t, int index) 
	{
		int max = t.length<=index ? 0 : t.length;
		
		if(max==0)
		{
			return null;
		}
		
		Nuplet[] result = new NupletInt[t.length-index-1];
		for(int i=0; i<t.length-index-1; i++)
		{
			result[i] = t[i+index];
		}
		return result;
	}

}
