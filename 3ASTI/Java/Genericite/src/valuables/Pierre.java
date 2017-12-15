package valuables;

import coffreGenerique.ObjetsPrecieux;
import coffreSimple.Coffre;

public abstract class Pierre implements ObjetsPrecieux {
	

	

		
		
	public Pierre(double monVolume){
		volume=monVolume;
	}

	/**
	 * @uml.property  name="valeur" readOnly="true"
	 */
	protected double valeur;
	
	public void setValeur(double valeur){
		this.valeur = valeur;
	}

	/**
	 * Getter of the property <tt>valeur</tt>
	 * @return  Returns the valeur.
	 * @uml.property  name="valeur"
	 */
	public double getValeur() {
		return this.valeur;
	}

	/**
	 * @uml.property  name="volume" readOnly="true"
	 */
	private final double volume;

	/**
	 * Getter of the property <tt>volume</tt>
	 * @return  Returns the volume.
	 * @uml.property  name="volume"
	 */
	public double getVolume() {
		return volume;
	}

		
		/**
		 */
		public abstract void expertiser();

		/**
		 * @uml.property  name="monCoffre"
		 * @uml.associationEnd  
		 */
		private Coffre monCoffre;

		/**
		 * Getter of the property <tt>monCoffre</tt>
		 * @return  Returns the monCoffre.
		 * @uml.property  name="monCoffre"
		 */
		public Coffre getMonCoffre() {
			return monCoffre;
		}


		/**
		 * Setter of the property <tt>monCoffre</tt>
		 * @param monCoffre  The monCoffre to set.
		 * @uml.property  name="monCoffre"
		 */
		public void setMonCoffre(Coffre monCoffre) {
			this.monCoffre = monCoffre;
		}



}
