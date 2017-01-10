package geometrie;

public abstract class FigureGeometrique {
	int code;
	protected String couleur;
	private String texture;
	
	
	/* Setters */
	public void setCouleur(String couleur)
	{
		this.couleur = couleur;
	}
	
	public void setTexture(String texture)
	{
		this.texture = texture;
	}
	
	public void setCode(int code)
	{
		this.code = code;
	}
	
	/* Getters */
	public String getCouleur()
	{
		return this.couleur;
	}
	
	public String getTexture()
	{
		return this.texture;
	}
	
	public int getCode()
	{
		return this.code;
	}
	
	/*	
	public double getPerimetre()
	{
		return 0.0;
	}
	*/
		
	/* Constructeurs */
	public FigureGeometrique()
	{
		// Constructeur par defaut
		this.setCode(0);
		this.setCouleur("Aucune");
		this.setTexture("Aucune");
	}
	
	public FigureGeometrique(String couleur, String texture)
	{
		this.setCode(0);
		this.setCouleur(couleur);
		this.setTexture(texture);
	}
	
	/* Methodes */
	protected void dessiner()
	{
		System.out.println("Dessin d'une figure géométrique");
	}
	
	public void dessiner(int zone)
	{
		System.out.println("Dessin de la zone "+zone+" d'une figure géométrique");
	}
	
	public abstract String toString();
	
}
