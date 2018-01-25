package sgbd.operateurs;

import sgbd.stockage.Nuplet;

public interface Jointure {
	public Nuplet[] jointureBl(Nuplet[] t1, Nuplet[] t2, int att1, int att2); // Jointure double boucle sur t1.att1 = t2.att2
	public Nuplet[] jointureH(Nuplet[] t1, Nuplet[] t2, int att1, int att2); // Jointure hash join sur t1.att1 = t2.att2
	public Nuplet[] jointureS(Nuplet[] t1, Nuplet[] t2, int att1, int att2); // Jointure sort merge boucle sur t1.att1 = t2.att2
}
