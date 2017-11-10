
public class Main {
	
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Compteur comp_1 = new Compteur("Compteur_1", 10);
		Compteur comp_2 = new Compteur("Compteur_2", 10);
		Compteur comp_3 = new Compteur("Compteur_3", 10);
		Compteur[] list_Compteur = {comp_1, comp_2, comp_3};
		
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

		comp_1.setPriority(10);
		comp_2.setPriority(4);
		comp_3.setPriority(1);
		
		for(int i=0; i<3; i++)
		{
			list_Compteur[i].start();
		}
		
	}

}

