
/**
 * Tri d'un tableau d'entiers
 * Version mono-thread
 */
public class Trieur extends Thread{
  private int[] t;
  private int debut;
  private int fin;
  private Notice notice;
  
  private Trieur(int[] t)
  {
    this.t = t;
  }
  
  private Trieur(int[] t, int debut, int fin)
  {
    this.t     = t;
    this.debut = debut;
    this.fin   = fin;
  }
  
  private Trieur(int[] t, int debut, int fin, Notice notice)
  {
    this.t      = t;
    this.debut  = debut;
    this.fin    = fin;
    this.notice = notice;
  }
 
  public void run()
  {
	  /* JOIN
	   this.trierJoin();
	   */
	 synchronized(this.notice)
	 {
		 this.trierWait();
		 this.notice.notify();
	 }
  }
  
  /**
   * Trie un tableau d'entiers par ordre croissant
   * @param t tableau à trier
   * @throws InterruptedException 
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
 
 private void trierJoin()
 {
	 if (this.fin - this.debut < 2)
	 {
	    if (t[this.debut] > t[this.fin])
	    {
	      echanger(this.debut, this.fin);
	    }
	    System.out.println("Fin " + this.getName() + "["+this.debut+";"+this.fin+"]");
		  
	  }
	  else
	  {
	      int milieu = this.debut + (this.fin - this.debut) / 2;
	      
	      Trieur tA = new Trieur(this.t, this.debut, milieu);
	      Trieur tB = new Trieur(this.t, milieu + 1, this.fin);
	      
	      tA.start();
	      tB.start(); 
	      try {
			tA.join();
			tB.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      triFusion(this.debut, this.fin);
	      System.out.println("Fin " + this.getName() + "["+this.debut+";"+this.fin+"]");
	  }
 }
 
 private void trierWait()
 {
	 if (this.fin - this.debut < 2)
	 {
	    if (t[this.debut] > t[this.fin])
	    {
	      echanger(this.debut, this.fin);
	    }
	    System.out.println("Fin " + this.getName() + "["+this.debut+";"+this.fin+"]");
		  
	  }
	  else
	  {
	      int milieu = this.debut + (this.fin - this.debut) / 2;
	      
	      Notice notice = new Notice();
	      Trieur tA = new Trieur(this.t, this.debut, milieu, notice);
	      Trieur tB = new Trieur(this.t, milieu + 1, this.fin, notice);
	      
	      synchronized(notice)
	      {
	    	  try {
	    		tA.start();
	    		tB.start();
				notice.wait();
				notice.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	      }
	   
	      triFusion(this.debut, this.fin);
	      System.out.println("Fin " + this.getName() + "["+this.debut+";"+this.fin+"]");
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
 
      /* JOIN
      Trieur monTrieur = new Trieur(t, 0, t.length - 1);
      monTrieur.start();
      try {
		monTrieur.join();
      } catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
      }
      */
      
      Notice maNotice  = new Notice();
      Trieur monTrieur = new Trieur(t, 0, t.length - 1, maNotice);
      synchronized(maNotice)
      {
    	  try {
    		monTrieur.start();
    		maNotice.wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      }
      
      for (int i = 0; i < t.length; i++)
      {
        System.out.println(t[i] + " ; ");
      }
      System.out.println();
    }
 }