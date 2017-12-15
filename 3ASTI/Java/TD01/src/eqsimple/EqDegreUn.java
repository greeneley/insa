/**
 * Une équation de dégré un de type aX+b = 0,  avec a et b des float.
 */

package eqsimple;

public class EqDegreUn {

	/**
	 * @return the coeffA
	 */
	public float getCoeffA() {
		return coeffA;
	}

	/**
	 * Setter of coeffA, whose modification may lead the equation to be unsolved
	 * @param coeffA the coeffA to set
	 */
	public void setCoeffA(float coeffA) {
		if(this.coeffA!=coeffA){
			this.coeffA = coeffA;
			solved = false;
		}
		
	}

	/**
	 *  Setter of coeffA, whose modification may lead the equation to be unsolved
	 * @return the coeffB
	 */
	public float getCoeffB() {
		return coeffB;
	}

	/**
	 * @param coeffB the coeffB to set
	 */
	public void setCoeffB(float coeffB) {
		if(this.coeffB!=coeffB){
			this.coeffB = coeffB;
			solved = false;
		}
	}

	/**
	 * @return the solved
	 */
	public boolean isSolved() {
		return solved;
	}

	/**
	 * @return the solution
	 */
	public float getSolution() {
		solve();  // Discutable si on pense qu'un getter ne modifie pas l'objet.
		return solution;
	}

	/**
	 * Coefficient a
	 */
	private float coeffA;
	/**
	 * Coefficient b
	 */
	private float coeffB;
	
	/**
	 * Booléen précisant si la solution actuelle est correcte
	 */
	private boolean solved;
	
	/**
	 * Solution de l'équation
	 */
	private float solution;

	/**
	 * Construction d'une équation nécessitant des valeurs pour a et b
	 * @param coeffA
	 * @param coeffB
	 */
	public EqDegreUn(float coeffA, float coeffB) {
		this.coeffA = coeffA;
		this.coeffB = coeffB;
		
		solution = 0;
		solved = false;
	}

	/**
	 * Transformation de l'équation en string pour affichage
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Equation : " + coeffA + " X + " + coeffB + " = 0";
	}
	
	/**
	 * Résolution de l'équation
	 */
	public void solve(){
		if(!solved){
			solved  = true;
			//Attention, pas de test d'exception
			solution = -coeffB / coeffA;
		}
		
	}
	
	
	
	
}
