package geometrie;

/**
 * @author  tluu
 */
public abstract class FigureGeometrique {
	/**
	 * @uml.property  name="code"
	 */
	int code;
	/**
	 * @uml.property  name="couleur"
	 */
	protected String couleur;
	/**
	 * @uml.property  name="texture"
	 */
	private String texture;
	
	
	/* Setters */
	/**
	 * @param couleur
	 * @uml.property  name="couleur"
	 */
	public void setCouleur(String couleur)
	{
		this.couleur = couleur;
	}
	
	/**
	 * @param texture
	 * @uml.property  name="texture"
	 */
	public void setTexture(String texture)
	{
		this.texture = texture;
	}
	
	/**
	 * @param code
	 * @uml.property  name="code"
	 */
	public void setCode(int code)
	{
		this.code = code;
	}
	
	/* Getters */
	/**
	 * @return
	 * @uml.property  name="couleur"
	 */
	public String getCouleur()
	{
		return this.couleur;
	}
	
	/**
	 * @return
	 * @uml.property  name="texture"
	 */
	public String getTexture()
	{
		return this.texture;
	}
	
	/**
	 * @return
	 * @uml.property  name="code"
	 */
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
