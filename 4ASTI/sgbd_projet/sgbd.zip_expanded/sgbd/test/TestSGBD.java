package sgbd.test;

import sgbd.impl.JointureInt;
import sgbd.impl.NupletInt;
import sgbd.impl.ProjectionImpl;
import sgbd.impl.RestrictionInt;
import sgbd.impl.TableInt;
import sgbd.stockage.Nuplet;

public class TestSGBD {
	
	private TableInt t;
	
	public TestSGBD(TableInt t)
	{
		this.t = t;
		System.out.println("Init test done.");
	}	
	
	/**
	 * Permet d'effectuer des tests de la classe NupletInt.
	 * 
	 */
	public void testTableInt()
	{
		System.out.println("\n===== LECTURE VIA FULLSCAN =====");
		for(Nuplet n : this.t.fullScan())
		{
			System.out.println(n.toString());
		}
		
		System.out.println("\n===== INSERT =====");
		Nuplet nouveau = new NupletInt(new byte[] {0,120,2,3,4,5,6,7,8,9});
		System.out.println("\nt.insert(nouveau_nuplet)");
		t.insert(nouveau);
		for(Nuplet n : this.t.fullScan())
		{
			System.out.println(n.toString());
		}

		
		System.out.println("\n===== UPDATE =====");
		System.out.println("\nt.update(1, 120, 119);");
		t.update(1, (byte) 120, (byte) 119);
		for(Nuplet n : this.t.fullScan())
		{
			System.out.println(n.toString());
		}
		
		System.out.println("\n===== DELETE =====");
		System.out.println("\nt.delete(1, 119);");
		t.delete(1, (byte) 119);
		for(Nuplet n : this.t.fullScan())
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
	public void testRestrictionInt(int att, Object object)
	{
		System.out.println("\n===== TESTS RESTRICTIONS =====");
		// Notre objet qui permttra de tester les methodes de la classe
		RestrictionInt restrictInt = new RestrictionInt();
		
		System.out.println("[.egalite(4, 45)]");
		for(Nuplet n : restrictInt.egalite(t.fullScan(), att, object))
		{
			System.out.println(n.toString());
		}
		
		System.out.println("[.superieur(4, 45)]");
		for(Nuplet n : restrictInt.superieur(t.fullScan(), att, object))
		{
			System.out.println(n.toString());
		}
		
		System.out.println("[.inferieur(4, 45)]");
		for(Nuplet n : restrictInt.inferieur(t.fullScan(), att, object))
		{
			System.out.println(n.toString());
		}
	}
	
	
	public void testProjectionImpl(Nuplet[] tab, int[] atts)
	{
		System.out.println("\n===== TESTS PROJECTION =====");
		
		// Notre objet qui permettra de tester les methodes de la classe		
		ProjectionImpl pi = new ProjectionImpl();
		
		System.out.println("[.project(tab, {0,3, 7})]");
		Nuplet[] resProj = pi.project(tab, atts); 

		for(Nuplet n : resProj)
		{
			System.out.println(n.toString());
		}
	}
	
	
	public void testJointureBl(Nuplet[] t1, Nuplet[] t2)
	{	
		System.out.println("\n===== TESTS JOINTURE DOUBLE BOUCLES =====");

		JointureInt jointer = new JointureInt();
		
		long startTime = System.currentTimeMillis();
		Nuplet[] result = jointer.jointureBl(t1, t2, 1, 1);
		long endTime = System.currentTimeMillis();
		System.out.println("That took " + (endTime - startTime) + " milliseconds");

	}
	
	
	public void testJointureH(Nuplet[] t1, Nuplet[] t2)
	{
		System.out.println("\n===== TESTS JOINTURE HASHJOIN =====");
		
		JointureInt jointer = new JointureInt();

		long startTime = System.currentTimeMillis();
		Nuplet[] result = jointer.jointureH(t1, t2, 1, 1);
		long endTime = System.currentTimeMillis();
		System.out.println("That took " + (endTime - startTime) + " milliseconds");

	}
	
	
	public void testJointureS(Nuplet[] t1, Nuplet[] t2)
	{
		System.out.println("\n===== TESTS JOINTURE SORT MERGE JOIN =====");
		
		JointureInt jointer = new JointureInt();
		
		long startTime = System.currentTimeMillis();
		Nuplet[] result = jointer.jointureS(t1, t2, 1, 1);
		long endTime = System.currentTimeMillis();
		System.out.println("That took " + (endTime - startTime) + " milliseconds");
	}
	
}
