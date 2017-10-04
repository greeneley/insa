package geometrie;

/**
 * @author  tluu
 */
public class Conique extends FigureGeometrique 
{
	/**
	 * @uml.property  name="centre"
	 * @uml.associationEnd  
	 */
	private Point centre;
	
	/* Setteurs */	
	/**
	 * @param centre
	 * @uml.property  name="centre"
	 */
	public void setCentre(Point centre)
	{
		this.centre = centre;
	}
	
	/* Getters */
	/**
	 * @return
	 * @uml.property  name="centre"
	 */
	public Point getCentre()
	{
		return this.centre;
	}
	
	/* Constructeurs */
	public Conique()
	{
		this.setCentre(new Point(0.0, 0.0));
	}
	
	/* Methodes */
	public String toString()
	{
		return "";
	}

}
