package sgbd.impl;

import sgbd.operateurs.Restriction;
import sgbd.stockage.Nuplet;

public class RestrictionInt implements Restriction {

	/* (non-Javadoc)
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
			if((byte)t[i].getAtt(att) == (int)v)
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

	@Override
	public Nuplet[] superieur(Nuplet[] t, int att, Object v) {
		int nb_result = 0;
		int[] egaux = new int[t.length];
		
		for(int i=0; i<t.length; i++)
		{
			if((byte)t[i].getAtt(att) >= (int)v)
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

	@Override
	public Nuplet[] inferieur(Nuplet[] t, int att, Object v) {
		int nb_result = 0;
		int[] egaux = new int[t.length];
		
		for(int i=0; i<t.length; i++)
		{
			if((byte)t[i].getAtt(att) <= (int)v)
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

}
