package geometrie;

public class Quadrilatere {

	/** 
	 * @uml.property name="lesSegments"
	 * @uml.associationEnd multiplicity="(0 -1)" dimension="1" ordering="true" inverse="quadrilatere:geometrie.Segment"
	 */
	private Segment[] lesSegments = new Segment[4];

	/**
	 * Getter of the property <tt>LesSegments</tt>
	 * @return  Returns the segment.
	 * @uml.property  name="LesSegments"
	 */
	public Segment[] getLesSegments() {
		return lesSegments;
	}

	/** 
	 * Setter of the property <tt>lesSegments</tt>
	 * @param lesSegments  The lesSegments to set.
	 * @uml.property  name="lesSegments"
	 */
	public void setLesSegments(Segment[] lesSegments) {
		this.lesSegments = lesSegments;
	}



	/**
	 * Getter of the property <tt>perimetre</tt>
	 * @return  Returns the perimetre.
	 * @uml.property  name="perimetre"
	 */
	public double getPerimetre() {
		double perimetre = 0;
		perimetre = lesSegments[0].longueur() + lesSegments[1].longueur()
				+ lesSegments[2].longueur() + lesSegments[3].longueur();
		return perimetre;
	}


	/**
	 */
	public String toString(){
		return lesSegments[0]+" "+lesSegments[1]+" "+lesSegments[2]+" "+lesSegments[3] ;	
	}


	/**
	 */
	public Quadrilatere(Point p1, Point p2, Point p3, Point p4){
		lesSegments[0] = new Segment(p1,p2);
		lesSegments[1] = new Segment(p2,p3);
		lesSegments[2] = new Segment(p3,p4);
		lesSegments[3] = new Segment(p4,p1);
		System.out.println("Quadrilatère");
	}


	/**
	 */
	public Quadrilatere(){
		this(new Point(),new Point(),new Point(),new Point());
		/*lesSegments[0] = new Segment(new Point(),new Point());
					lesSegments[1] = new Segment(new Point(),new Point());
					lesSegments[2] = new Segment(new Point(),new Point());
					lesSegments[3] = new Segment(new Point(),new Point());*/
	}


}
