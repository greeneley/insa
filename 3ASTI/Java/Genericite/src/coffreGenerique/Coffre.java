/**
 * 
 */
package coffreGenerique;

import java.util.ArrayList;
import java.util.ListIterator;

import valuables.Pierre;
import valuables.Topaze;
import valuables.Diamant;


/**
 * Un coffre simple, sans code
 * @author  ceichler
 */
public class Coffre<T extends ObjetsPrecieux> implements ObjetsPrecieux{

	/**
	 * Valeur du contenu du coffre, nul à l'init
	 * @uml.property  name="valeur"
	 */
	private double valeur = 0.0;
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
		this.volumeMax=1;
		System.out.println("Constructeur de coffre par défaut");
	}

	
	/**
	 * Instancie un coffre ayant un certain volume maximum
	 * @param maxVol volume maximal du coffre
	 */
	public Coffre(int maxVol) {
		this.volumeMax=maxVol;
		System.out.println("Constructeur de coffre avec volume");
	}

	/**
	 * @return  the valeur
	 * @uml.property  name="valeur"
	 */
	public double getValeur() {
		this.valeur=0.0;
		for(T monObjet: this.getMesObjets())
		{
			this.valeur += monObjet.getValeur();
		}
		
		return this.valeur;
	}

	/**
	 * @return  the volume max
	 * @uml.property  name="volumeMax"
	 */
	public int getVolumeMax() {
		return this.volumeMax;
	}



	/**
	 * @return  the estOuvert
	 * @uml.property  name="ouvert"
	 */
	public boolean isOuvert() {
		return this.ouvert;
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
	 * @uml.property  name="mesObjets"
	 * @uml.associationEnd  multiplicity="(0 -1)" dimension="1" ordering="true" inverse="monCoffre:coffreSimple.Objet"
	 */
	private ArrayList<T> mesObjets = new ArrayList<T>();


	/**
	 * Getter of the property <tt>mesObjets</tt>
	 * @return  Returns the mesObjets.
	 * @uml.property  name="mesObjets"
	 */
	public ArrayList<T> getMesObjets() {
		return this.mesObjets;
	}

		
		
	/**
	 * Test si le coffre est plein (s'il contient autant de pierres que son volume Max)
	 */
	
	public boolean estPlein(){
		return this.mesObjets.size()==this.volumeMax;			
	}

	/**
	 * Ajoute une Objet au coffre
	 * @param T l'objet a ajouter
	 */
	
	public void ajouterObjet(T p1){
		if(!ouvert) System.out.println("Impossible d'ajouter un objet precieux; le coffre est fermé!");
		else if(estPlein()) System.out.println("Impossible d'ajouter un objet precieux; le coffre est plein!");
		else mesObjets.add(p1);
	}
	
	/**
	 * Retire une Objet du coffre
	 * @param T l'objet a retirer
	 */
	
	public void retirerObjet(T objet){
		if(!ouvert)System.out.println("Impossible de retirer un objet precieux; le coffre est fermé!");
		else if(mesObjets.contains(objet))System.out.println("Impossible de retirer l'objet precieux spécifiee; il n'est pas dans ce coffre!");
		else mesObjets.remove(objet);
	}
	

	/**
	 * Recherche la presence d'une pierre dans le coffre
	 * @param valeur La valeur de la pierre à rechercher
	 * @return true si la pierre est présente. false sinon
	 */
	public T rechercheObjet(double valeur)
	{
		T monObjet = null;
		boolean presence = false;

		ArrayList<T> alp = this.getMesObjets();
		if(alp == null)
		{
			System.out.println("Il n'y a pas de pierres !");
			return null;
		}
		
		for(ListIterator<T> iter = alp.listIterator(); iter.hasNext() && !presence;)
		{
			T tmp = iter.next();
			if(tmp.getValeur()==valeur)
			{
				System.out.println("Une pierre correspondante a ete trouvee");
				monObjet = tmp;
				presence = true;
			}
		}
		if(monObjet == null)
		{
			System.out.println("Aucune pierre ne correspond !");
		}
		return (T)monObjet;
		
	}
}
