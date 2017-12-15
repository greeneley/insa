package eqsimple;

public class TestEquation {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		EqDegreUn eq = new EqDegreUn(12, -5);
		
		System.out.println(eq);
		System.out.println(eq.isSolved());
		System.out.println(eq.getSolution());
		System.out.println(eq.isSolved());
		
		eq.setCoeffA(4);
		
		System.out.println(eq.isSolved());
		System.out.println(eq.getSolution());
		System.out.println(eq.isSolved());
				

	}

}
