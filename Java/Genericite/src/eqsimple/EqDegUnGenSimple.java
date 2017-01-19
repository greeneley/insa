package eqsimple;

public class EqDegUnGenSimple<T extends Operations<T>>{
	//aX + b = 0
	private T a;
	private T b;
	
	
	/* Setters */
	public void setA(T a) 
	{
		this.a = a;
	}
	
	public void setB(T b) 
	{
		this.b = b;
	}
	
	/* Getters */
	public T getA() 
	{
		return this.a;
	}
	
	public T getB() 
	{
		return this.b;
	}
	
	public double getSolution()
	{
		double X = 0.0;
		return X;
		
	}

}
