package sgbd.impl;

import sgbd.operateurs.Projection;
import sgbd.stockage.Nuplet;

public class ProjectionImpl implements Projection 
{

	/**
	 * Permet de faire une projection d'un Nuplet[] t sur un ensemble d'indice atts.
	 * Une projection consiste à ne garder que les valeurs qui nous intéressent.
	 * Exemple : project([7,8,9,1,0,4,6,7], [1,2,3]) = [8,9,1].
	 * 
	 * @param  t    Le Nuplet[] sur lequel effectuer la projection.
	 * @param  atts La liste des indexes (attributs) sur lesquels projeter.
	 * @return 		Le Nuplet[] issu de la projection.
	 * @see         sgbd.operateurs.Projection
	 */
	@Override
	public Nuplet[] project(Nuplet[] t, int[] atts) 
	{		
		// ===== Variables =====
		Nuplet[] result = new Nuplet[t.length];
		
		for(int i=0; i<t.length; i++)
		{
			Nuplet nuplet = new NupletInt(atts.length);
			for(int j=0; j<atts.length; j++)
			{
				nuplet.putAtt(j, t[i].getAtt(atts[j]));;
			}
			result[i] = nuplet;
		}
		
		return result;
	}
}
