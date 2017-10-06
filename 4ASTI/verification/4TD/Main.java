import java.util.Arrays;

public class Main {

    public static String affiche(boolean[] b) {
	StringBuffer res = new StringBuffer();
	res.append("{ ");
	for (int i=0; i<b.length;i++) {
	    if (b[i])
		res.append(i+" ");
	}
	res.append("}");
	return res.toString();
    }

    public static void main(String[] args) {
	int[][] systeme = 
	    { 
		{3,4},
		{1},		
		{4,6}, 
		{2,8}, 
		{2,4,5}, 
		{4,6,9},
		{0,7},
		{2,8},
		{1},
		{2,5}
	    };
	boolean[][] AP = {{true,true,false,true,false,
			   false,false,false,true,true}};


	CTL ctl = new CTL(systeme,AP);

	// calcul de p
	boolean[] p = ctl.prop(0);
	System.out.println("p = " + affiche(p));

	// calcul de EX(p)
	boolean[] a1 = ctl.EX(ctl.prop(0));
	System.out.println("EX(p) = " + affiche(a1));

	// calcul de AX(p)
	boolean[] a2 = ctl.AX(ctl.prop(0));
	System.out.println("AX(p)= " + affiche(a2));

	// calcul de AX(EX(p))
	boolean[] a3 = ctl.AX(a1);
	System.out.println("AX(EX(p))= " + affiche(a3));

	// calcul de EX(AX(p))
	boolean[] a4 = ctl.EX(a2);
	System.out.println("EX(AX(p))= " + affiche(a4));


	// calcul de EF(p)
	boolean[] b1 = ctl.EU(ctl.vrai(),ctl.prop(0));
	System.out.println("EF(p) = " + affiche(b1));

	// calcul de AF(p)
	boolean[] b2 = ctl.AU(ctl.vrai(),ctl.prop(0));
	System.out.println("AF(p) = " + affiche(b2));

	// calcul de EG(p) = !AF(!p)
	boolean[] b3 = ctl.neg(ctl.AU(ctl.vrai(),ctl.neg(ctl.prop(0))));
	System.out.println("EG(p) = " + affiche(b3));

	// calcul de AG(p) = !EF(!p)
	boolean[] b4 = ctl.neg(ctl.EU(ctl.vrai(),ctl.neg(ctl.prop(0))));
	System.out.println("AG(p) = " + affiche(b4));
    }
}
