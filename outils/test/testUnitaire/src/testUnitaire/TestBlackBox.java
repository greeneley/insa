package testUnitaire;

import static org.junit.Assert.*;

import org.junit.Test;

import johnArithmetics.MathsUtils;

public class TestBlackBox {
	
	int X = 42;

	@Test
	public void testJohnBetterPPCM() {
		/* java.lang.ArithmeticException: / by zero
		 * assertEquals(0, MathsUtils.johnBetterPPCM(0, 0));
		 */
		assertEquals(0, MathsUtils.johnBetterPPCM(0, X));
		/* Division par zero
		 * java.lang.ArithmeticException: / by zero
		 */
		//assertEquals(0, MathsUtils.johnBetterPPCM(X, 0));
		assertEquals(X, MathsUtils.johnBetterPPCM(X, X));
		assertEquals(840, MathsUtils.johnBetterPPCM(60, 168));
	}

	@Test
	public void testJohnFaultyPPCM() {
		assertEquals(0, MathsUtils.johnFaultyPPCM(0, 0));
		assertEquals(0, MathsUtils.johnFaultyPPCM(0, X));
		assertEquals(0, MathsUtils.johnFaultyPPCM(X, 0));
		
		/* On attendait 42 mais on obtient 84
		 * java.lang.AssertionError: expected:<42> but was:<84>
		 */
		//assertEquals(X, MathsUtils.johnFaultyPPCM(X, X));


		assertEquals(840, MathsUtils.johnFaultyPPCM(60, 168));
	}

	@Test
	public void testJohnCorrectPPCM() {
		assertEquals(0, MathsUtils.johnCorrectPPCM(0, 0));
		assertEquals(0, MathsUtils.johnCorrectPPCM(0, X));
		assertEquals(0, MathsUtils.johnCorrectPPCM(X, 0));
		assertEquals(X, MathsUtils.johnCorrectPPCM(X, X));
		assertEquals(840, MathsUtils.johnCorrectPPCM(60, 168));
	}

}
