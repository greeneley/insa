package sgbd.test;

import sgbd.stockage.*;
import sgbd.impl.*;

public class Main 
{
	
	public static final int datasetSize = 100;
	public static final int nupletSize = 10;
	
	private static Table t;
	
	
	/**
	 * Ce sont les tests originallement presents dans le fichier.
	 * 
	 */
	public static void testOriginaux()
	{
		System.out.println("Lecture d'une table");
		for(int i=0;i<datasetSize;i++){
			System.out.println(t.get(i).toString());
			}
		
		// Utilisation de getByAtt
		System.out.println("Test GetByAtt");
		Nuplet[] res = t.getByAtt(4, (byte)50);
		for(Nuplet n : res) 
		{
			System.out.println(n.toString());
		}
	}
	
	
	/**
	 * Permet d'effectuer des tests de la classe NupletInt.
	 * 
	 */
	public static void testTableInt()
	{
		System.out.println("===== LECTURE VIA FULLSCAN =====");
		for(Nuplet n : t.fullScan())
		{
			System.out.println(n.toString());
		}
	}
	

	/**
	 * Permet d'effectuer des tests sur la classe RestrictionInt.
	 * 
	 * @param att	 Index de l'attribut a tester.
	 * @param object Valeur a laquelle effectuer les tests de comparaison avec l'attribut.
	 */
	public static void testRestrictionInt(int att, int object)
	{
		System.out.println("===== TESTS RESTRICTIONS =====");
		// Notre objet qui permttra de tester les methodes de la classe
		RestrictionInt restrictInt = new RestrictionInt();
		
		System.out.println("[.egalite()]");
		for(Nuplet n : restrictInt.egalite(t.fullScan(), att, object))
		{
			System.out.println(n.toString());
		}
		
		System.out.println("[.superieur()]");
		for(Nuplet n : restrictInt.superieur(t.fullScan(), att, object))
		{
			System.out.println(n.toString());
		}
		
		System.out.println("[.inferieur()]");
		for(Nuplet n : restrictInt.inferieur(t.fullScan(), att, object))
		{
			System.out.println(n.toString());
		}
	}
	
	public static void testProjectionImpl(Nuplet[] tab, int[] atts)
	{
		System.out.println("===== TESTS PRJECTION =====");
		// Notre objet qui permettra de tester les methodes de la classe		
		ProjectionImpl pi = new ProjectionImpl();
		
		System.out.println("[.project()]");
		Nuplet[] resProj = pi.project(tab, atts); 

		for(Nuplet n : resProj)
		{
			System.out.println(n.toString());
		}
	}

	
	public static void main(String[] args)
	{
		
		/* ========================
		 *          INIT
		 * ========================
		 */
		// Génération des données
		Nuplet[] tab = new NupletInt[datasetSize];
		for(int i=0;i<datasetSize;i++){
			tab[i] = new NupletInt(nupletSize);
			for(int j=0;j<nupletSize;j++){
				tab[i].putAtt(j, (byte)(j+i));
			}
		}

		// Implémentation avec Table
		System.out.println("------------------------------------------------");	
		System.out.println("Creation d'une table");
		t = new TableInt("/tmp/table2", nupletSize);
		for(int i=0;i<datasetSize;i++){
			t.put(tab[i]);
			}
		
		/* ========================
		 *          TEST
		 * ========================
		 */
		//testOriginaux();
		//testTableInt();
		//testRestrictionInt(4, 45);
		//testProjectionImpl(t.fullScan(), new int[]{0,3,7});
		//TODO : JointureBl
		
		
		
	}
	
	
	

}

