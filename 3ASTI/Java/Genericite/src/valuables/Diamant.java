/**
 * 
 */
package valuables;

/** 
 * @author ceichler
 */
public class Diamant extends Pierre {

	/**
	 * @param monVolume
	 */
	public Diamant(double monVolume) {
		super(monVolume);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see coffreSimple.Pierre#expertiser()
	 */
	@Override
	public void expertiser() {
		 this.valeur=1000*this.getVolume();

	}

}
