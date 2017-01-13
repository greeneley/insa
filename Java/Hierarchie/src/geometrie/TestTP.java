package geometrie;

public class TestTP {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Point p1 = new Point(5.1, 2.0, "A");
		Point p2 = new Point();
		Point p3 = new Point(1.1,6.0, "B");
		Point p4 = new Point(3.8,7.5, "C");
		
		//System.out.println(p1 + "  " + p2 );
		//System.out.println("Distance " + p1.distance(p2) );
		
		//Quadrilatere quad=new Quadrilatere();
		//System.out.println(quad);
		
		System.out.println(" ====== Exercice 1 ======");
		System.out.println("\nQuestion c)");
		Segment s1 = new Segment(p1, p2);
		System.out.println(s1);
		System.out.println("Longueur du segment : "+s1.longueur());
		
		System.out.println("\nQuestion d)");
		Quadrilatere quad2 = new Quadrilatere(p1,p2,p3,p4);
		System.out.println(quad2);
		System.out.println("Perimetre du quadrilatere : "+quad2.getPerimetre());
		
		
		System.out.println("\n ====== Exercice 2 ======");
		System.out.println("\nQuestion c)");
		Rectangle rect1 = new Rectangle();
		System.out.println("Perimetre du rectangle 1 : "+rect1.getPerimetre());
		
		System.out.println("\nQuestion d)");
		Point pd1 = new Point(0.0, 5.0);
		Point pd2 = new Point(10.0, 0.0);
		Rectangle rect2 = new Rectangle(pd1, pd2);
		System.out.println("Perimetre du rectangle 2 : "+rect2.getPerimetre());
		
		System.out.println("\n ====== Exercice 3 ======");
		Rectangle rect3 = new Rectangle(pd1, pd2, "Bleu", "Mousseuse");
		System.out.println("Perimetre du rectangle 3 : "+rect3.getPerimetre());
		System.out.println("Couleur du rectangle 3 : "+rect3.getCouleur());
		System.out.println("Modification de la couleur en jaune !");
		rect3.setCouleur("Jaune");
		System.out.println("Couleur du rectangle 3 : "+rect3.getCouleur());
		System.out.println("Texture du rectangle 3 : "+rect3.getTexture());
		
		Cercle cercle = new Cercle(p1, 10.0, "Azur", "Fer");
		System.out.println("Centre du cercle : "+cercle.getCentre());
		System.out.println("Rayon du cercle : "+cercle.getRayon());
		System.out.println("Perimetre du cercle : "+cercle.getPerimetre());
		System.out.println("Aire du cercle : "+cercle.getAire());
		System.out.println("Couleur du cercle : "+cercle.getCouleur());
		System.out.println("Texture du cercle : "+cercle.getTexture());
		
		Ellipse ellipse = new Ellipse(p3, 20.0, 13.0, "Vert", "Argileuse");
		System.out.println("Centre de l'ellipse : "+ellipse.getCentre());
		System.out.println("Demi-grand axe de l'ellipse : "+ellipse.getA());
		System.out.println("Demi-petit axe de l'ellipse : "+ellipse.getB());
		System.out.println("Distance au foyer de l'ellipse : "+ellipse.getDistFoyer());
		System.out.println("Couleur de l'ellipse : "+ellipse.getCouleur());
		System.out.println("Texture de l'ellipse : "+ellipse.getTexture());
		
		System.out.println("\n ====== Exercice 4 ======");
		System.out.println("\nQuestion b)");
		Rectangle r = new Rectangle();
		r.dessiner(4);
		r.dessiner();
		Point p = new Point();
		r.dessiner(p);
		
		System.out.println("\nQuestion c)");
		Quadrilatere quad = new Quadrilatere();
		double perquar = quad.getPerimetre();
		System.out.println("");
		Rectangle rect = new Rectangle();
		double perrect = rect.getPerimetre();
		
		System.out.println("\nQuestion e)");
		FigureGeometrique [] listFig = new FigureGeometrique[3];
		listFig[0] = new Rectangle();
		listFig[1] = new Quadrilatere();
		listFig[2] = new Cercle();
		for (int i=0; i<=2; i++)
		{
			listFig[i].dessiner();
			//listFrig[i].getPerimetre();
		}
		((Rectangle)listFig[0]).getPerimetre();
		
		// Question f
		//Rectangle rec = new Quadrilatere();
		
		System.out.println("\nQuestion h)");
		for(int i=0; i<=2; i++)
		{
			System.out.println(listFig[i].code);
		}
		/*
		System.out.println(((Quadrilatere)listFig[0]).code);
		System.out.println(((Rectangle)listFig[0]).code);
		System.out.println(((Cercle)listFig[0]).code);
		*/
		
		System.out.println("\n ====== Exercice 5 ======");
		System.out.println("\nQuestion a)");
		quad.dessiner();
		System.out.println("\nQuestion b)");
		System.out.println("\nQuestion c)");
		System.out.println("\nQuestion d)");
		
		
	}

}
