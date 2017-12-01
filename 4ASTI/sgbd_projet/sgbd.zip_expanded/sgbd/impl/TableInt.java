package sgbd.impl;

import java.util.Vector;

import sgbd.stockage.Nuplet;
import sgbd.stockage.Table;

public class TableInt implements Table{
	
	private int records;
	FichierInt f;
	
	public TableInt(String filePath, int nupletSize){
		this.records = 0;
		this.f = new FichierInt(filePath, nupletSize);
	}

	@Override
	public Nuplet get(int pos) {
		return (Nuplet) f.get(pos);
	}

	@Override
	public int size() {
		return this.records;
	}

	@Override
	public void put(Nuplet n) {
		f.store(this.records, n);
		this.records++;
	}

	/** Cette methode peut etre amelioree par un index !
	 * 
	 */
	@Override
	public Nuplet[] getByAtt(int att, Object value) {
		Vector<Nuplet> v = new Vector<Nuplet>();
		for(int i=0;i<this.size();i++){
			Nuplet temp = this.get(i);
			if((byte)(temp.getAtt(att)) == (byte)value){
				v.addElement(temp);
			}
		}
		Nuplet[] ret = new Nuplet[v.size()];
		for(int i=0;i<v.size();i++)
			ret[i] = v.elementAt(i);
		return ret;
	}

	@Override
	public Nuplet[] fullScan() {
		// TODO Auto-generated method stub
		Nuplet[] scan = new NupletInt[this.records];
		
		for(int i=0; i<this.records; i++)
		{
			scan[i] = (NupletInt)this.f.get(i);
		}
		return scan;
	}

	@Override
	public void insert(Nuplet n) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Nuplet n, int att, Object value) 
	{

		// ===== Variables =====
		int[] match = new int[n.size()];
		
		// ===== Algo =====
		// === On va d'abord lister toutes les indexes a garder
		
	
		Nuplet output = new NupletInt(n.size()-1);
		for(int i=0; i<att; i++)
		{
			output.putAtt(i, n.getAtt(i));
		}
		for(int i=(att+1); i<n.size(); i++)
		{
			// Attention a ne pas etre out of index avec output
			output.putAtt(i-1, n.getAtt(i));
		}
		n = output;
		
	}

	@Override
	public void update(Nuplet n, int att, Object oldValue, Object newValue) {
		// TODO Auto-generated method stub
		
	}

}
