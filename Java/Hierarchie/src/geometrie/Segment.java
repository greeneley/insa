package geometrie;


public class Segment {


	/**
	 */
	public double longueur(){
		return this.getDebut().distance(this.getFin());
	}


	/**
	 */
	public String toString(){
		return "["+debut.getLabel()+fin.getLabel()+"]";	
	}


	/**
	 * @uml.property  name="debut"
	 * @uml.associationEnd  multiplicity="(1 1)" inverse="segment:geometrie.Point"
	 */
	private Point debut = new geometrie.Point();


	/**
	 * Getter of the property <tt>debut</tt>
	 * @return  Returns the debut.
	 * @uml.property  name="debut"
	 */
	public Point getDebut() {
		return debut;
	}


	/**
	 * Setter of the property <tt>debut</tt>
	 * @param debut  The debut to set.
	 * @uml.property  name="debut"
	 */
	public void setDebut(Point debut) {
		this.debut = debut;
	}


	/**
	 * @uml.property  name="fin"
	 * @uml.associationEnd  multiplicity="(1 1)" inverse="segment:geometrie.Point"
	 */
	private Point fin = new geometrie.Point();


	/**
	 * Getter of the property <tt>fin</tt>
	 * @return  Returns the fin.
	 * @uml.property  name="fin"
	 */
	public Point getFin() {
		return fin;
	}


	/**
	 * Setter of the property <tt>fin</tt>
	 * @param fin  The fin to set.
	 * @uml.property  name="fin"
	 */
	public void setFin(Point fin) {
		this.fin = fin;
	}



	/**
	 */
	public Segment(Point debut, Point fin){
		this.debut=debut;
		this.fin=fin;
		System.out.println("Un segment de droite vient d'être créé");
	}

}
