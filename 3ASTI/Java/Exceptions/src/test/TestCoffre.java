/**
 * 
 */
package test;

import valuables.Diamant;
import valuables.Pierre;
import valuables.Topaze;
import coffreSimple.*;

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
		CoffreACode monCoffre = new CoffreACode(2);
		CoffreACode monCoffre2 = new CoffreACode();
		
		
		//Test Ouverture/fermeture
		((CoffreACode) monCoffre).ouverture("1234");
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
		Pierre p2 = new Topaze(6);
		Pierre p3 = new Diamant(2);
		
		monCoffre.ajouterPierre(p1);
		//Le coffre était fermé!
		monCoffre.ouverture("azerty");
		monCoffre.ajouterPierre(p1);
		monCoffre.ajouterPierre(p2);
		//Le coffre devrait être plein
		monCoffre.ajouterPierre(p3);
	
		System.out.println("La valeur de mon coffre non expertisé est " +monCoffre.getValeur());
		
		p1.expertiser();
		//p2.expertiser();
		System.out.println("La valeur de mon coffre expertisé est " +monCoffre.getValeur());
	}

}
