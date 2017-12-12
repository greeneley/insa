package sgbd.impl;

import java.io.RandomAccessFile;
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
		if(pos > (this.records-1))
		{
			return null;
		}
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

	public void insert(Nuplet n) 
	{
		// TODO Auto-generated method stub
		// C'est quoi la difference entre ça et put ???
	}

	
	/**
	 * Permet de supprimer tous les Nuplet qui possedent la valeur
	 * value à l'empalcement att.
	 * 
	 * @param att
	 * @param value
	 */
	public void delete(int att, Object value) 
	{
		if(this.getByAtt(att, value).length > 0)
		{
			Nuplet[] save = this.fullScan();
			this.records  = 0;
			this.f.resetLength();
			for(Nuplet n : save)
			{
				if(n.getAtt(att) != value)
				{
					this.put(n);
				}
			}
		}
	}	

	
	@Override
	public void delete(Nuplet n, int att, Object value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Nuplet n, int att, Object oldValue, Object newValue) {
		// TODO Auto-generated method stub
		
	}
	
	
	public void update(int att, Object oldValue, Object newValue) 
	{
		Nuplet current;
		for(int i=0; i<this.size(); i++)
		{
			current = this.get(i);
			System.out.println(current.getAtt(att)+"    "+oldValue);
			if(current.getAtt(att) == oldValue)
			{
				Nuplet nouveau = new NupletInt(current.size());
				for(int j=0; j<att; j++)
				{
					nouveau.putAtt(j, current.getAtt(j));
				}
				nouveau.putAtt(att, newValue);
				for(int j=(att+1); j<current.size(); j++)
				{
					nouveau.putAtt(j, current.getAtt(j));
				}
				this.f.store(i, nouveau);	
				System.out.println("\nok");
			}			
		}
	}

}
