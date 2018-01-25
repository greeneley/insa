package sgbd.impl;

import java.util.Vector;

import sgbd.stockage.Nuplet;
import sgbd.stockage.Table;

public class TableInt implements Table{
	
	private String name;
	private int records;
	private FichierInt f;
	
	public TableInt(String filePath, int nupletSize){
		this.records = 0;
		this.f       = new FichierInt(filePath, nupletSize);
		this.name    = filePath.substring(filePath.lastIndexOf("/")+1);
	}

	public TableInt(String filePath, int nupletSize, int recordsSize)
	{
		this.records = recordsSize;
		this.f       = new FichierInt(filePath, nupletSize);
		this.name    = filePath.substring(filePath.lastIndexOf("/")+1);
	}
	
	public String getName() {
		return name;
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

	
	
	/* (non-Javadoc)
	 * @see sgbd.stockage.Table#fullScan()
	 * 
	 * Applique la methode de parcours 'Fullscan' dans la TableInt.
	 * 
	 */
	@Override
	public Nuplet[] fullScan() 
	{
		Nuplet[] scan = new NupletInt[this.records];
		
		for(int i=0; i<this.records; i++)
		{
			scan[i] = (NupletInt)this.f.get(i);
		}
		return scan;
	}

	
	public void insert(Nuplet n) 
	{
		// C'est quoi la difference entre ça et put ???
		/* J'aurais bien ajoute un element pos mais problemes ensuite
		 * avec la gestion de la taille,
		 * avec la structure de l'objet qui n'a pas d'id...
		 */
		this.put(n);
	}

	
	/**
	 * Permet de supprimer tous les Nuplet qui possedent la valeur
	 * value à l'emplacement att.
	 * 
	 * @param att
	 * @param value
	 */
	public void delete(int att, Object value) 
	{
		Nuplet[] save = this.fullScan();
		this.records  = 0;
		for(Nuplet n : save)
		{
			if((byte)n.getAtt(att) != (byte)value)
			{
				this.put(n);
			}
		}
	}	
	
	
	/**
	 * Supprime la ligne a l'index row de la table
	 * 
	 * @param row L'index dans la table du Nuplet a supprimer
	 */
	public void deleteRow(int row)
	{
		for(int i=row; i<(this.records-1); i++)
		{
			this.f.store(i, (Nuplet)this.f.get(i+1));
		}
		this.records--;
	}
	
	
	/**
	 * Met a jour tous les Nuplets de la table dont la colonne att est de value oldValue
	 * par la valeur newValue
	 * 
	 * @see sgbd.stockage.Table#update(int, java.lang.Object, java.lang.Object)
	 */
	public void update(int att, Object oldValue, Object newValue) 
	{
		Nuplet current;
		for(int i=0; i<this.size(); i++)
		{
			current = this.get(i);
			if((byte)current.getAtt(att) == (byte)oldValue)
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
			}			
		}
	}
	
	/**
	 * Affecte la valeur newValue a la colonne column dans le Nuplet d'indice row
	 * 
	 * @param row Indice du Nuplet concerne
	 * @param column L'index de la colonne a modifier
	 * @param newValue La nouvelle valeur
	 */
	public void updateInRow(int row, int column, Object newValue)
	{
		Nuplet current = this.get(row);
		Nuplet nouveau = new NupletInt(current.size());
		
		for(int i=0; i<current.size(); i++)
		{
			if(i==column)
			{
				nouveau.putAtt(i, (byte)newValue);
			}
			else
			{
				nouveau.putAtt(i, (byte)current.getAtt(i));
			}
		}
		this.f.store(row, nouveau);
	}
	
	public int getNupletSize()
	{
		return this.f.getNupletSize();
	}

}
