
public class Main {
	
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Compteur comp_1 = new Compteur("Compteur_1", 10);
		Compteur comp_2 = new Compteur("Compteur_2", 10);
		Compteur comp_3 = new Compteur("Compteur_3", 10);
		
		/* ======== METHODE PROCEDURALE =======
		try {
			comp_1.compte();
			comp_2.compte();
			comp_3.compte();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
		/* =========== EXERCICES 2 ============ */
		comp_1.resetPos(); // methode affectant une var statique
		
		comp_1.start();
		comp_2.start();
		comp_3.start();
	}

}

