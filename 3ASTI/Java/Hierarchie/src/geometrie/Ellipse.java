package geometrie;

/**
 * @author  tluu
 */
public class Ellipse extends Conique
{
	/**
	 * @uml.property  name="a"
	 */
	private double a; //demi grand axe
	/**
	 * @uml.property  name="b"
	 */
	private double b; //demi petit axe
	// On pourrait definir un private double foyer si on s'en sert beaucoup
	
	/* Setters */
	/**
	 * @param a
	 * @uml.property  name="a"
	 */
	public void setA(double a)
	{
		this.a = a;
	}
	
	/**
	 * @param b
	 * @uml.property  name="b"
	 */
	public void setB(double b)
	{
		this.b = b;
	}
	
	/* Getters */
	/**
	 * @return
	 * @uml.property  name="a"
	 */
	public double getA()
	{
		return this.a;
	}
	
	/**
	 * @return
	 * @uml.property  name="b"
	 */
	public double getB()
	{
		return this.b;
	}
	
	public double getDistFoyer()
	{
		return Math.sqrt(Math.pow(this.getA(), 2.0) - Math.pow(this.getB(), 2.0));
	}
	
	/* Constructeurs */
	public Ellipse()
	{
		this.setA(0.0);
		this.setB(0.0);
		System.out.println("\nConstructeur d'Ellipse par defaut");
	}
	
	public Ellipse(Point centre, double a, double b)
	{
		this.setCentre(centre);
		this.setA(a);
		this.setB(b);
		System.out.println("\nNouvelle Ellipse");
	}
	
	public Ellipse(Point centre, double a, double b, String couleur, String texture)
	{
		this(centre, a, b);
		this.setCouleur(couleur);
		this.setTexture(texture);
		System.out.println("...avec couleur et texture");
	}
	
	/* Methodes */

}
