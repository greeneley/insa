/**
 * 
 */
package valuables;

/** 
 * @author ceichler
 */
public class Topaze extends Pierre {

	/**
	 * @param monVolume
	 */
	public Topaze(double monVolume) {
		super(monVolume);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see coffreSimple.Pierre#expertiser()
	 */
	@Override
	public void expertiser() {
		 this.valeur=100*this.getVolume();

	}

}
