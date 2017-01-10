package geometrie;

public class Rectangle extends Quadrilatere 
{
   int code;
   
   /* Getters */
   public double getPerimetre()
   {
	   System.out.println("Calcul du périmètre d'un rectangle");
	   double perimetre = 0.0;
	   Segment[] segments = this.getLesSegments();
	   perimetre = segments[0].longueur() * 2 + segments[1].longueur() * 2;
	   return perimetre;
   }
   
   /* Constructeurs */
   public Rectangle()
   {
	   // Constructeur par defaut
	   this.code = 2;
	   System.out.println("\nConstructeur de Rectangle");
   }
   
   public Rectangle(Point hg, Point bd)
   {
	   super(hg, new Point(hg.getX(), bd.getY()), bd, new Point(bd.getX(), hg.getY()));
	   this.code = 2;
   }
   
   public Rectangle(Point hg, Point bd, String couleur, String texture)
   {
	   this(hg, bd);
	   this.setCouleur(couleur);
	   this.setTexture(texture);
   }
   
   /* Methodes */
   public void dessiner()
   {
	   System.out.println("Dessin d'une figure rectangulaire");
   }
   
   public void dessiner(Point p)
   {
	   System.out.println("Dessin d'un rectangle");
   }
}
