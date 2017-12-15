/**
 * 
 */
package coffreSimple;

import java.util.ArrayList;
import exceptions.NotExpertisedException;

import valuables.Pierre;


/**
 * Un coffre simple, sans code
 * @author  ceichler
 */
public class Coffre {

	/**
	 * Valeur du contenu du coffre, nul à l'init
	 * @uml.property  name="valeur"
	 */
	private double valeur = 0;
	/**
	 * Contenance max du coffre (en nombre de pierre)
	 * @uml.property  name="volumeMax"
	 */
	private final int volumeMax;


	/**
	 * Booléen indiquant l'état fermé/ouvert du coffre
	 * @uml.property  name="ouvert"
	 */
	private boolean ouvert=true;


	/**
	 * Instancie un coffre par défaut; son volume maximum est 1
	 */
	public Coffre() {
		volumeMax=1;
		System.out.println("Constructeur de coffre par défaut");
	}


	/**
	 * Instancie un coffre ayant un certain volume maximum
	 * @param maxVol volume maximal du coffre
	 */
	public Coffre(int maxVol) {
		volumeMax=maxVol;
		System.out.println("Constructeur de coffre avec volume");
	}

	/**
	 * @return  the valeur
	 * @uml.property  name="valeur"
	 */
	public double getValeur() throws NotExpertisedException {
		//Si return valeur, pas terrible, on ne change jamais la valeur!

		this.valeur=0;

		for(Pierre p : mesPierres) {

			// Pour chaque Pierres 
			//On somme la valeur de la pierre avec la valeur actuelle du coffre

			if(!p.getExpertise())
			{
				throw new NotExpertisedException("La Pierre n'est pas expertisee");
			}
			//et si p n'a pas de valeur?

		}

		return this.valeur;
	}

	/**
	 * @return  the volume max
	 * @uml.property  name="volumeMax"
	 */
	public int getVolumeMax() {
		return volumeMax;
	}



	/**
	 * @return  the estOuvert
	 * @uml.property  name="ouvert"
	 */
	public boolean isOuvert() {
		return ouvert;
	}

	/**
	 * Ouverture du coffre
	 */
	public void ouverture(){
		if(this.ouvert)System.out.println("Tentative d'ouverture d'un coffre ouvert...");
		this.ouvert=true;
	}

	/**
	 * Fermeture du coffre
	 */
	public void fermeture(){
		if(!this.ouvert)System.out.println("Tentative de fermeture d'un coffre fermé...");
		this.ouvert=false;
	}

	/**
	 * @uml.property  name="mesPierres"
	 * @uml.associationEnd  multiplicity="(0 -1)" dimension="1" ordering="true" inverse="monCoffre:coffreSimple.Pierre"
	 */
	private ArrayList<Pierre> mesPierres = new ArrayList<Pierre>();


	/**
	 * Getter of the property <tt>mesPierres</tt>
	 * @return  Returns the mesPierres.
	 * @uml.property  name="mesPierres"
	 */
	public ArrayList<Pierre> getMesPierres() {
		return mesPierres;
	}



	/**
	 * Test si le coffre est plein (s'il contient autant de pierres que son volume Max)
	 */

	public boolean estPlein(){
		return this.mesPierres.size()==this.volumeMax;			
	}

	/**
	 * Ajoute une Pierre au coffre
	 * @param pierre la pierre à ajouter
	 */

	public void ajouterPierre(Pierre pierre){
		if(!ouvert) System.out.println("Impossible d'ajouter une Pierre; le coffre est fermé!");
		else if(estPlein()) System.out.println("Impossible d'ajouter une Pierre; le coffre est plein!");
		else mesPierres.add(pierre);
	}

	/**
	 * Retire une Pierre du coffre
	 * @param pierre la pierre à retirer
	 */

	public void retirerPierre(Pierre pierre){
		if(!ouvert)System.out.println("Impossible de retirer une Pierre; le coffre est fermé!");
		else if(mesPierres.contains(pierre))System.out.println("Impossible de retirer la pierre spécifiée; elle n'est pas dans ce coffre!");
		else mesPierres.remove(pierre);
	}
}
