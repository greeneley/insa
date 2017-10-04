package geometrie;


public class Point {

	/**
	 * @uml.property  name="x"
	 */
	private double x;

	/** 
	 * Getter of the property <tt>x</tt>
	 * @return  Returns the x.
	 * @uml.property  name="x"
	 */
	public double getX() {
		return x;
	}

	/**
	 * @uml.property  name="y"
	 */
	private double y;

	/** 
	 * Getter of the property <tt>y</tt>
	 * @return  Returns the y.
	 * @uml.property  name="y"
	 */
	public double getY() {
		return y;
	}

	/**
	 */
	public double distance(Point unPoint){
		return Math.sqrt((x-unPoint.x)*(x-unPoint.x) + (y-unPoint.y)*(y-unPoint.y)); }

	/** 
	 * Setter of the property <tt>x</tt>
	 * @param x  The x to set.
	 * @uml.property  name="x"
	 */
	public void setX(double x) {
		this.x = x;
	}

	/** 
	 * Setter of the property <tt>y</tt>
	 * @param y  The y to set.
	 * @uml.property  name="y"
	 */
	public void setY(double y) {
		this.y = y;
	}

	public String toString(){


		return label + "(" + x +", " + y +  ")";

	}

	/**
	 * @uml.property  name="label"
	 */
	private String label;

	/**
	 * Getter of the property <tt>label</tt>
	 * @return  Returns the label.
	 * @uml.property  name="label"
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Setter of the property <tt>label</tt>
	 * @param label  The label to set.
	 * @uml.property  name="label"
	 */
	public void setLabel(String label) {
		this.label = label;
	}


	/**
	 */
	public Point(){
		this(0.0,0.0,"O");
		//System.out.println("utilisation du constructeur par défaut");

	}


	/**
	 */
	public Point(double unX, double unY){
		this(unX, unY, null);
		//System.out.println("utilisation du constructeur à deux arguments");
	}


	/**
	 */
	public Point(double unX, double unY, String unLabel){
		//System.out.println("utilisation du constructeur à trois arguments");
		x = unX;
		y = unY;
		label = unLabel; }

}
