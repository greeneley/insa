package valuables;

import coffreSimple.Coffre;

public abstract class Pierre {
	
	protected boolean expertise;
	
	public void setExpertise(boolean bool)
	{
		this.expertise = bool;
	}
	
	public boolean getExpertise()
	{
		return this.expertise;
	}
		
		
	public Pierre(double monVolume){
		this.volume=monVolume;
		this.expertise = false;
	}

	/**
	 * @uml.property  name="valeur" readOnly="true"
	 */
	protected double valeur = (Double) null;

	/**
	 * Getter of the property <tt>valeur</tt>
	 * @return  Returns the valeur.
	 * @uml.property  name="valeur"
	 */
	public double getValeur() {
		return valeur;
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
		 * @uml.associationEnd  inverse="mesPierres:coffreSimple.Coffre"
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
