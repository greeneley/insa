package sgbd.test;

import sgbd.stockage.*;
import sgbd.console.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.Scanner;

import sgbd.impl.*;

public class Main 
{
	public static final int datasetSize = 100;
	public static final int nupletSize = 10;
	
	public static void testSGBD(TableInt t)
	{
		TestSGBD Test = new TestSGBD(t);
		
		// Tests basiques : fullscan, restriction, projection
		Test.testTableInt();
		Test.testRestrictionInt(4, (byte)45);
		Test.testProjectionImpl(t.fullScan(), new int[]{0,3,7});
		
		Random gen1 = new Random();	
		Random gen2 = new Random();
		Nuplet[] u1 = new NupletInt[250];
		Nuplet[] u2 = new NupletInt[250];
		
		// On genere une table de 250 nuplets de taille 5
		System.out.println("Generation de tables aleatoires...");		
		for(int i=0; i<250; i++)
		{
			u1[i] = new NupletInt(new byte[] {(byte)gen1.nextInt(100), (byte)gen1.nextInt(100), (byte)gen1.nextInt(100), (byte)gen1.nextInt(100), (byte)gen1.nextInt(100)});
			u2[i] = new NupletInt(new byte[] {(byte)gen2.nextInt(100), (byte)gen2.nextInt(100), (byte)gen2.nextInt(100), (byte)gen2.nextInt(100), (byte)gen2.nextInt(100)});	
		}
		System.out.println("Done.");
		
		// Test des jointures
		Test.testJointureBl(u1, u2);
		Test.testJointureH(u1, u2);
		Test.testJointureS(u1, u2);
	}
	
	public static void testConsole()
	{
		System.out.println("\n====== TEST CONSOLE SGBD =====");
		System.out.println("Pour voir les commandes a disposition, taper 'help;'");
		// Shell
		Shell terminal = new Shell();
		terminal.read();
	}
	
	public static void main(String[] args)
	{
		/* ========================
		 *          INIT
		 * ========================
		 */
		System.out.println("Creation d'une table...");
		// Génération des données
		Nuplet[] tab = new NupletInt[datasetSize];
		for(int i=0;i<datasetSize;i++)
		{
			tab[i] = new NupletInt(nupletSize);
			for(int j=0;j<nupletSize;j++)
			{
				tab[i].putAtt(j, (byte)(j+i));
			}
		}

		// Implémentation avec Table
		TableInt tSGBD = new TableInt("./table", nupletSize);
		for(int i=0;i<datasetSize;i++)
		{
			tSGBD.put(tab[i]);
		}
		System.out.println("Done.");
		
		
		/* ========================
		 *          TEST
		 * ========================
		 */
		
		Scanner s = new Scanner(System.in);
		System.out.print("Voulez vous lancer le test des fonctions SGBD avant de lancer le test en console ? (y/n) : ");
		if(s.next().equals("y"))
		{
			testSGBD(tSGBD);
		}
		testConsole();
		s.close();
		
	}
}

