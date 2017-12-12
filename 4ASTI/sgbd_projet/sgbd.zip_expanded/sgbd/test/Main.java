package sgbd.test;

import sgbd.stockage.*;

import java.util.Arrays;
import java.util.Comparator;

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
		System.out.println("\n===== LECTURE VIA FULLSCAN =====");
		for(Nuplet n : t.fullScan())
		{
			System.out.println(n.toString());
		}
		
		System.out.println("\n===== INSERT =====");
		
		
		System.out.println("\n===== UPDATE =====");
		System.out.println("\nt.update(1, 100, 200);");
		t.update(1, (byte) 100, (byte) 70);
		for(Nuplet n : t.fullScan())
		{
			System.out.println(n.toString());
		}
		
		System.out.println("\n===== DELETE =====");
		System.out.println("\nt.update(1, 100, 200);");
		t.delete(1, (byte) 70);
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
		System.out.println("\n===== TESTS RESTRICTIONS =====");
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
		System.out.println("\n===== TESTS PROJECTION =====");
		
		// Notre objet qui permettra de tester les methodes de la classe		
		ProjectionImpl pi = new ProjectionImpl();
		
		System.out.println("[.project()]");
		Nuplet[] resProj = pi.project(tab, atts); 

		for(Nuplet n : resProj)
		{
			System.out.println(n.toString());
		}
	}
	
	
	public static void testJointureBl()
	{	
		System.out.println("\n===== TESTS JOINTURE DOUBLE BOUCLES =====");
		
		JointureBl jointer = new JointureBl();
		Nuplet[] u1 = new NupletInt[3];
		Nuplet[] u2 = new NupletInt[3];
		
		u1[0] = new NupletInt(new byte[] {0,1,2,3,4});
		u1[1] = new NupletInt(new byte[] {1,2,3,4,5});
		u1[2] = new NupletInt(new byte[] {9,1,9,9,9});
		
		u2[0] = new NupletInt(new byte[] {8,1,8,8,8});
		u2[1] = new NupletInt(new byte[] {1,7,3,4,5});
		u2[2] = new NupletInt(new byte[] {9,2,9,9,9});

		long startTime = System.currentTimeMillis();
		Nuplet[] result = jointer.jointure(u1, u2, 1, 1);
		long endTime = System.currentTimeMillis();
		System.out.println("That took " + (endTime - startTime) + " milliseconds");
		
		for(Nuplet n : result)
		{
			System.out.println(n);	
		}

	}
	
	
	public static void testJointureH()
	{
		System.out.println("\n===== TESTS JOINTURE HASHJOIN =====");
		
		JointureH jointer = new JointureH();
		Nuplet[] u1 = new NupletInt[3];
		Nuplet[] u2 = new NupletInt[3];
		
		u1[0] = new NupletInt(new byte[] {0,1,2,3,4});
		u1[1] = new NupletInt(new byte[] {1,2,3,4,5});
		u1[2] = new NupletInt(new byte[] {9,1,9,9,9});
		
		u2[0] = new NupletInt(new byte[] {8,1,8,8,8});
		u2[1] = new NupletInt(new byte[] {1,7,3,4,5});
		u2[2] = new NupletInt(new byte[] {9,2,9,9,9});

		long startTime = System.currentTimeMillis();
		Nuplet[] result = jointer.jointure(u1, u2, 1, 1);
		long endTime = System.currentTimeMillis();
		System.out.println("That took " + (endTime - startTime) + " milliseconds");
		
		for(Nuplet n : result)
		{
			System.out.println(n);	
		}
	}
	
	
	public static void testJointureS()
	{
		System.out.println("\n===== TESTS JOINTURE SORT MERGE JOIN =====");
		
		JointureS jointer = new JointureS();
		Nuplet[] u1 = new NupletInt[3];
		Nuplet[] u2 = new NupletInt[3];
		
		u1[0] = new NupletInt(new byte[] {0,1,2,3,4});
		u1[1] = new NupletInt(new byte[] {1,2,3,4,5});
		u1[2] = new NupletInt(new byte[] {9,1,9,9,9});
		
		u2[0] = new NupletInt(new byte[] {8,1,8,8,8});
		u2[1] = new NupletInt(new byte[] {1,7,3,4,5});
		u2[2] = new NupletInt(new byte[] {9,2,9,9,9});

		long startTime = System.currentTimeMillis();
		Nuplet[] result = jointer.jointure(u1, u2, 1, 1);
		long endTime = System.currentTimeMillis();
		System.out.println("That took " + (endTime - startTime) + " milliseconds");
		
		for(Nuplet n : result)
		{
			System.out.println(n);	
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
		System.out.println("===== Creation d'une table =====");
		t = new TableInt("/tmp/table2", nupletSize);
		for(int i=0;i<datasetSize;i++){
			t.put(tab[i]);
			}
		
		/* ========================
		 *          TEST
		 * ========================
		 */
		//testOriginaux();
		testTableInt();
		//testRestrictionInt(4, 45);
		//testProjectionImpl(t.fullScan(), new int[]{0,3,7});
		//testJointureBl();
		//testJointureH();
		//testJointureS();
	}
}

