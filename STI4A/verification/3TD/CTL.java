import java.util.Arrays;

public class CTL {

    int[][] systeme;
    boolean[][] AP; 


    public CTL(int[][] systeme, boolean[][] AP) {
	this.systeme = systeme;
	this.AP = AP;
    }

    public boolean[] prop(int i) {
	return AP[i];
    }

    public boolean[] faux() {
	boolean[] res = new boolean[systeme.length];
	Arrays.fill(res,false);
	return res;
    }

    public boolean[] vrai() {
	boolean[] res = new boolean[systeme.length];
	Arrays.fill(res,true);
	return res;
    }

    public boolean[] neg(boolean[] a) {
	boolean[] res = new boolean[systeme.length];
	for (int i=0; i<systeme.length; i++)
	    res[i] = !a[i];
	return res;
    }
   
    public boolean[] or(boolean[] a, boolean[] b) {
	boolean[] res = new boolean[systeme.length];
	for (int i=0; i<systeme.length; i++)
	    res[i] = a[i] || b[i];
	return res;
    }

    public boolean[] and(boolean[] a, boolean[] b) {
	boolean[] res = new boolean[systeme.length];
	for (int i=0; i<systeme.length; i++)
	    res[i] = a[i] && b[i];
	return res;
    }

    public boolean[] EX(boolean[] a) {
	boolean[] res = new boolean[systeme.length];
	for (int i=0; i<systeme.length; i++) {
	    res[i] = false;
	    for (int j=0; !res[i] && j<systeme[i].length; j++)
		res[i] = a[systeme[i][j]];
	}
	return res;
    }

    public boolean[] AX(boolean[] a) {
	return neg(EX(neg(a)));
    } 

    public boolean[] EU(boolean[] a, boolean[] b) {
	boolean[] res = faux();
	boolean[] avant;
    	do {
	    avant = res;
	    res = or(b,and(a,EX(res)));
	} 
	while(!Arrays.equals(avant,res));
	return res;
    }

    public boolean[] AU(boolean[] a, boolean[] b) {
	boolean[] res = faux();
	boolean[] avant;
    	do {
	    avant = res;
	    res = or(b,and(a,AX(res)));
	} 
	while(!Arrays.equals(avant,res));
	return res;
    }
}