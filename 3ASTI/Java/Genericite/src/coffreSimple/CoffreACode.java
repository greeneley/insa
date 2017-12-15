/**
 * 
 */
package coffreSimple;

/** 
 * @author ceichler
 */
public class CoffreACode extends Coffre {

	/**
	 * Constructeur par défaut de Coffre, le Code est initialisé à "1234"
	 */
	public CoffreACode() {
		super();
	}

	/**
	 * Instancie un coffre ayant un certain volume maximum
	 * @param maxVol volume maximal du coffre
	 */
	public CoffreACode(int maxVol) {
		super(maxVol);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Code d'ouverture
	 * @uml.property  name="monCode"
	 */
	private String monCode = "1234";

	/**
	 * Changement de code du coffre
	 * @param nveauCode le nouveau code, valable après invocation
	 * @param ancienCode l'ancien code valable au moment de l'invocation
	 */
	public void setMonCode(String nveauCode, String ancienCode){
		if(ancienCode==monCode) monCode=nveauCode;
		else
		{
			System.err.println("L'ancien code ne correspond pas !");
		}
	}
	
	/**
	 * Ouverture d'un coffre à code
	 * @param code le code à renseigner 
	 */
	public void ouverture(String code){
		if(code==monCode) super.ouverture();
		else
		{
			//msg d'erreur
			System.err.println("Mauvais code!");
		}
		
	}
	
	@Override
	/**
	 * Ouverture sans code, affichage d'une alerte
	 */
	public void ouverture(){
		//affichage "rentrez code"
		System.out.println("Rentrez un code!");
	}
}
