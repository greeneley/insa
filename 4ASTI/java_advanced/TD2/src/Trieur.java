import java.util.Arrays;

/**
 * Tri d'un tableau d'entiers
 * Version mono-thread
 */
public class Trieur extends Thread{
  private int[] t;
  private int[] tA;
  private int[] tB;
  
  private Trieur(int[] t)
  {
    this.t = t;
  }

  public void run()
  {
	 Trieur tableau = new Trieur(t);
	 tableau.trierJoin(0, t.length - 1);
  }
  
  /**
   * Trie un tableau d'entiers par ordre croissant
   * @param t tableau à trier
   */
public static void trier(int[] t)
  {
    Trieur tableau = new Trieur(t);
    tableau.trier(0, t.length - 1);
  }
  
  /**
   * Trie une tranche de t
   * @param debut indice du début de la partie à trier
   * @param fin indice de la fin de la partie à trier
   */
 private void trier(int debut, int fin)
 {
    if (fin - debut < 2)
    {
      if (t[debut] > t[fin])
      {
        echanger(debut, fin);
      }
    }
    else
    {
      int milieu = debut + (fin - debut) / 2;
      trier(debut, milieu);
      trier(milieu + 1, fin);
      triFusion(debut, fin);
    }
 }
 
 private void trierJoin(int debut, int fin)
 {
	 if (fin - debut < 2)
	 {
	    if (t[debut] > t[fin])
	    {
	      echanger(debut, fin);
	    }
	  }
	  else
	  {
	      int milieu = debut + (fin - debut) / 2;
	      this.tA = Arrays.copyOfRange(t, debut, milieu);
	      this.tB = Arrays.copyOfRange(t, milieu + 1, )
	      
	      Trieur trieurA = new Trieur();
	      Trieur trieurB = new Trieur();
	      
	      
	      
	      try {
	    	  //join
	      } catch (InterruptedException e) {
	    	  // TODO Auto-generated catch block
	    	  e.printStackTrace();
	      }
	      triFusion(debut, fin);
	  }
 }
 
 
  /**
   * Echanger t[i] et t[j]
   */
  private void echanger(int i, int j)
  {
    int valeur = t[i];
    t[i] = t[j];
    t[j] = valeur;
  }
  
  /**
   * Fusionne 2 tranches déjà triées du tableau t
   *  - 1ere tranche : de debut a milieu
   *  - 2eme tranche : de milieu + 1 a fin
   * @param milieu indique le dernier indice de la 1ere tranche
   */
  private void triFusion(int debut, int fin)
  {
    // tableau ou va aller la fusion
    int[] tFusion = new int[fin - debut + 1];
    int milieu = (debut + fin) / 2;
    // Indices des éléments a comparer
    int i1 = debut,
        i2 = milieu + 1;
    // Indice de la prochaine case du tableau tFusion a remplir
    int iFusion = 0;
    while (i1 <= milieu && i2 <= fin)
    {
      if (t[i1] < t[i2])
      {
        tFusion[iFusion++] = t[i1++];
      }
      else
      {
        tFusion[iFusion++] = t[i2++];
      }
    }
    if (i1 > milieu)
    {
      // la 1ere tranche est epuisee
      for (int i = i2; i<= fin; )
      {
        tFusion[iFusion++] = t[i++];
      }
    }
    else
    {
      // la 2ee tranche est epuisee
      for (int i = i1; i <= milieu; )
      {
        tFusion[iFusion++] = t[i++];
      }
    }
    // Copie tFusion dans t
    for (int i=0, j=debut; i <= fin - debut; )
    {
      t[j++] = tFusion[i++];
    }
  }
    
    public static void main(String[] args)
    {
    	
      int[] t = {5, 8, 3, 2, 7, 10, 1};
      //Trieur.trier(t);
      
      Trieur monTrieur = new Trieur(t);
      monTrieur.start();
      try {
    	  monTrieur.join();
      } catch (InterruptedException e) {
    	  // TODO Auto-generated catch block
    	  e.printStackTrace();
      }
      for (int i = 0; i < t.length; i++)
      {
        System.out.println(t[i] + " ; ");
      }
      System.out.println();
    }
 }