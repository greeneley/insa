package geometrie;

public class Cercle extends Conique {
	int code;
	private double rayon;
	
	/* Setters */
	public void setRayon(double r)
	{
		this.rayon = r;
	}
	
	
	/* Getters */
	public double getRayon()
	{
		return this.rayon;
	}
	
	public double getPerimetre()
	{
		return (this.getRayon() * 2 * Math.PI);
	}
	
	public double getAire()
	{
		return (Math.PI * Math.pow(this.getRayon(), 2.0));
	}
	
	
	/* Constructeurs */
	public Cercle()
	{
		this.code = 3;
		this.setRayon(0.0);
		System.out.println("\nConstructeur de Cercle par defaut");
	}
	
	public Cercle(Point centre, double r)
	{
		this.code = 3;
		this.setCentre(centre);
		this.setRayon(r);
		System.out.println("\nNouveau cercle");
	}
	
	public Cercle(Point centre, double r, String couleur, String texture)
	{
		this(centre, r);
		this.setCouleur(couleur);
		this.setTexture(texture);
		System.out.println("...Avec couleur et texture");
	}

}
