/**
 * 
 */
package test;

import valuables.Pierre;
import valuables.Topaze;
//import coffreSimple.*;
import coffreGenerique.*;

/**
 * @author ceichler
 *
 */
public class TestCoffre {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//Créons un coffre pouvant contenir jusqu'à 2 pierres
		CoffreACode<Pierre> monCoffre = new CoffreACode<Pierre>(2);
		CoffreACode<Pierre> monCoffre2 = new CoffreACode<Pierre>(2);
		CoffreACode<Coffre<Pierre>> monCoffre3 = new CoffreACode<Coffre<Pierre>>(2);
		
		
		//Test Ouverture/fermeture
		((CoffreACode<Pierre>) monCoffre).ouverture("1234");
		if(!monCoffre.isOuvert()) System.err.println("Probleme d'ouverture");
		
		monCoffre.fermeture();
		if(monCoffre.isOuvert()) System.err.println("Probleme de fermeture");
		
		//Test set code
		monCoffre.setMonCode("azerty","qwerty");
		monCoffre.setMonCode("azerty","1234");
		
		//Test getValeur
		System.out.println("La valeur de mon coffre vide est " +monCoffre.getValeur());
		
		//Création et ajout de pierres
		//Pierre p1 = new Pierre(5);
		Topaze p1 = new Topaze(5);
		p1.setValeur(10.0);
		Pierre p2 = new Topaze(6);
		p2.setValeur(20.0);
		Pierre p3 = new Topaze(2);
		p3.setValeur(10.0);
		Pierre p4 = new Topaze(5);
		p4.setValeur(20.0);
		
		monCoffre.ajouterObjet(p1);
		System.out.println("Valeur de la pierre p1 est : "+p1.getValeur());
		//Le coffre était fermé!
		monCoffre.ouverture("azerty");
		monCoffre.ajouterObjet(p1);
		monCoffre.ajouterObjet(p2);
		p4 = (Pierre)monCoffre.rechercheObjet(10.0);
		//System.out.println("Valeur de la pierre p4 est :"+p4.getValeur());
		//Le coffre devrait être plein
		monCoffre.ajouterObjet(p3);
		monCoffre2.ajouterObjet(p3);
		monCoffre2.ajouterObjet(p4);
	
		System.out.println("La valeur de mon coffre non expertisé est " +monCoffre.getValeur());
		
		p1.expertiser();
		p2.expertiser();
		System.out.println("La valeur de mon coffre expertisé est " +monCoffre.getValeur());
		System.out.println();
		System.out.println("Debut du coffre-ception");
		monCoffre3.ajouterObjet(monCoffre);
		monCoffre3.ajouterObjet(monCoffre2);
		System.out.println("La valeur de mon coffre de coffres est : " +monCoffre3.getValeur());
		System.out.println("La valeur des coffres que contient mon coffre est :");
		for(Coffre<Pierre> iter: monCoffre3.getMesObjets())
		{
			System.out.println("Valeur du coffre : " +iter.getValeur());
		}
	}

}
