package sgbd.console;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;

import sgbd.impl.JointureInt;
import sgbd.impl.NupletInt;
import sgbd.impl.ProjectionImpl;
import sgbd.impl.RestrictionInt;
import sgbd.impl.TableInt;
import sgbd.stockage.Nuplet;


/**
 * @author root
 *
 */
public class SGBD 
{
	private Adaptater[]           adaptaters;  // Fait le lien entre R.E. et actions
	private RestrictionInt        restrictor;  // Classe pour les restrictions
	private ProjectionImpl        projector;   // Classe pour les projections
	private JointureInt           jointor;     // inutilisee
	private Matcher               m;		   // Permet de reconnaitre les R.E.
	private String                prompt;      // Le prompt affiche pour l'utilisateur
	private File                  dbFd;        // File vers le fichier de la BDD utilise
	private String                dbFolder;    // Chemin vers dossier des DB
	private String                tableFolder; // Chemin vers dossier des tables
	private boolean               status;      // Indique si le parser est up
	private Map<String, TableInt> db;          // Map qui repertorie les tables de la BDD
	
	
	public SGBD()
	{
		this.initAdaptater();
		this.initFolders();
		this.status     = true;
		this.db         = null;
		this.dbFd       = null;
		this.m          = null;
		this.restrictor = new RestrictionInt();
		this.projector  = new ProjectionImpl();
		this.jointor    = new JointureInt();
		this.prompt     = "SGBD[no_db] > ";
		
		System.out.println("Init SGBD done.");
	}

	
	/**
	 * La methode initialise les adaptaters de l'application SGBD.
	 * <br/>
	 * <br/>Les adaptateurs sont des objets qui permettent d'associer
	 * une action concrete dans la SGBD a une expression
	 * reguliere. En cas de match du parser, l'action est automatiquement
	 * triggered par l'adaptater associe.
	 */
	private void initAdaptater()
	{
		this.adaptaters = new Adaptater[] {
				new Adaptater(new Commande() { public void action(Matcher m){help();} }, "(?i)^\\s*HELP\\s*;"),
				new Adaptater(new Commande() { public void action(Matcher m){exit();} }, "(?i)^\\s*EXIT\\s*;"),
				new Adaptater(new Commande() { public void action(Matcher m){showDatabases();} }, "(?i)^\\s*SHOW DATABASES\\s*;"),
				new Adaptater(new Commande() { public void action(Matcher m){showTables();} }, "(?i)^\\s*SHOW TABLES\\s*;"),
				new Adaptater(new Commande() { public void action(Matcher m){useDatabase(m);} }, "(?i)^\\s*USE DATABASE (\\w+)\\s*;"),
				new Adaptater(new Commande() { public void action(Matcher m){selectByColumn(m);} }, "(?i)^\\s*SELECT ((?:(?:\\d+\\s*,\\s*)*)\\d+|[*]) FROM (\\w+)\\s*(?:;|WHERE (\\d+)\\s*(=|>=|<=)\\s*(\\d+)\\s*;)"),
				new Adaptater(new Commande() { public void action(Matcher m){selectByRow(m);} }, "(?i)^\\s*SELECT ((?:(?:\\d+\\s*,\\s*)*)\\d+|[*]) FROM (\\w+)\\s*(?:;|WHERE ROW\\s*(=|>=|<=)\\s*(\\d+)\\s*;)"),
				new Adaptater(new Commande() { public void action(Matcher m){createTableInt(m);} }, "(?i)^\\s*CREATE TABLEINT (\\w+) (\\d+)\\s*;"),
				new Adaptater(new Commande() { public void action(Matcher m){insertTableInt(m);} }, "(?i)^\\s*INSERT INTO (\\w+) VALUES\\s*[(]\\s*((?:\\d+\\s*,\\s*)*\\d+)\\s*[)]\\s*;"),
				new Adaptater(new Commande() { public void action(Matcher m){deleteByColumn(m);} }, "(?i)^\\s*DELETE FROM (\\w+) WHERE (\\d+)\\s*=\\s*(\\d+)\\s*;"),
				new Adaptater(new Commande() { public void action(Matcher m){deleteByRow(m);} }, "(?i)^\\s*DELETE FROM (\\w+) WHERE ROW\\s*=\\s*(\\d+)\\s*;"),
				new Adaptater(new Commande() { public void action(Matcher m){updateByColumn(m);} }, "(?i)^\\s*UPDATE (\\w+) SET (\\d+)\\s*=\\s*(\\d+) WHERE (\\d+)\\s*(=|<=|>=)\\s*(\\d+)\\s*;"),
				new Adaptater(new Commande() { public void action(Matcher m){updateByRow(m);} }, "(?i)^\\s*UPDATE (\\w+) SET (\\d+)\\s*=\\s*(\\w+) WHERE ROW\\s*(=|<=|>=)\\s*(\\w+)\\s*;")
				//new Adaptater(COMMANDE, REGEX),
		};
	}
	
	
	/**
	 * Initialise les differents dossiers pour l'utilisation du SGBD.
	 * <br/>
	 * <br/>En l'etat des choses, les fichiers des BDD sont stockes dans le dossier 
	 * "./databases/", donc dans le meme dossier que les sources.
	 * <br/>Les fichiers des tables eux, sont stockes dans "./tables/". 
	 * <b>Toutes les tables sont stockees dans le meme dossier</b>.
	 * Cela implique donc que deux tables de meme nom mais de database differents vont 
	 * <b>entrer en conflit</b>.
	 */
	private void initFolders()
	{
		this.dbFolder    = "./databases/";
		this.tableFolder = "./tables/";
		
		File dirDb    = new File(this.dbFolder);
		File dirTable = new File(this.tableFolder);
		
		if(!dirDb.isDirectory())
		{
			dirDb.mkdir();
		}
		if(!dirTable.isDirectory())
		{
			dirTable.mkdir();
		}
	}


	/**
	 * Methode qui permet de parser l'input entree par l'utilisateur afin de traiter sa requete.
	 * <br>
	 * <br>Liste des requetes actuellement reconnues :
	 * <br>| SELECT <b>[colonnes,]*colonne</b> | <b>*</b> FROM <b>table</b> [WHERE <b>colonne</b> = <b>condition</b>]
	 * <br>| SELECT <b>[colonnes,]*colonne</b> | <b>*</b> FROM <b>table</b> [WHERE ROW = <b>ligne</b>]
	 * <br>| UPDATE <b>table</b> SET <b>colonne1</b> = <b>newValue</b> WHERE <b>colonne2</b> = <b>condition2</b>
	 * <br>| UPDATE <b>table</b> SET <b>colonne</b> = <b>value</b> WHERE ROW = <b>condition</b>
	 * <br>| INSERT INTO <b>table</b> VALUES (<b>values, ..., values</b>)
	 * <br>| DELETE FROM <b>table</b> WHERE <b>colonne</b> = <b>condition</b>
	 * <br>| DELETE FROM <b>table</b> WHERE ROW = <b>condition</b>
	 * <br>| CREATE TABLEINT <b>nom</b> <b>taille</b>
	 * <br>| USE DATABASE <b>database</b>
	 * <br>| SHOW DATABASES
	 * <br>| SHOW TABLES
	 * <br>| EXIT
	 * <br>| HELP
	 *  
	 * @param input String entree par l'utilisateur a parser
	 */
	public boolean parse(String input)
	{
		// On parcourt tous les adaptaters
		for(int i=0; i<this.adaptaters.length; i++)
		{
			// Si l'un trouve un match
			this.m = this.adaptaters[i].pattern.matcher(input);
			if(this.m.matches())
			{
				// On trigger la commande
				this.adaptaters[i].commande.action(this.m);
				return true;
			}
		}
		return false;
	}
	
	/*
	 * Permet de tester l'affichage du matcher sur l'expression reguliere.
	 * 
	 * @param m2 Objet Matcher
	 */
	private void doCommandTest(Matcher m2)
	{
		System.out.println(m.groupCount());
		for(int i=m.start(); i<=m.groupCount(); i++)
		{
			System.out.println("Group "+i+" : "+m.group(i));
		}
	}
	
	/**
	 * Permet a l'utilisateur de quitter le programme sur confirmation.
	 * <br>
	 * <br>La methode demande validation pour proceder a l'arret du programme.
	 * <br>En cas de reponse non comprise, le programme <b>ne</b> s'arrete <b>pas</b>
	 * <br>
	 * <br>R.E. "(?i)^\\s*EXIT\\s*$"
	 */
	protected void exit()
	{
		int reponse = 'n';

		System.out.print("Prenez soin d'enregistrer votre travail. Quitter ? [y/n] : ");
		try {
			reponse = System.in.read(); // lecture du prochain character
			if(reponse=='y')
			{
				System.out.println("Arret du programme");
				this.status = false;
			}
			else
			{
				System.out.println("Arret du programme annulee");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void dummy()
	{
		System.out.println("Test");
	}
	
	/**
	 * Met a jour une colonne d'un Nuplet si son index remplie la condition
	 * <br/>
	 * <br/> A noter qu'en utilisant 'WHERE ROW >= 0', on obtient le meme effet que
	 * 'UPDATE table SET col=value"  
	 * <br/>
	 * <br/>"(?i)^\\s*UPDATE (\\w+) SET (\\d+)\\s*=\\s*(\\d+) WHERE ROW\\s*(=|<=|>=)\\s*(\\d+)\\s*$"
	 * <br/>
	 * @param m2 Objet Matcher
	 */
	protected void updateByRow(Matcher m2) 
	{		
		// Verifications
		if(this.db==null)
		{
			System.out.println("[Erreur] Aucune base de donnees selectionee");
			return;
		}
		
		if(!this.db.containsKey(m2.group(1)))
		{
			System.out.println("[Erreur] La table <"+m2.group(1)+"> n'existe pas");
			return;
		}	
		
		if(this.db.get(m2.group(1)).getNupletSize()<=Integer.parseInt(m2.group(2)))
		{
			System.out.println("[Erreur] Colonne <"+m2.group(2)+"> hors portee");
			return;
		}
		
		// Traitement
		TableInt table = this.db.get(m2.group(1));
		if(m2.group(4).equals("="))
		{
			if(table.size() <= Integer.parseInt(m2.group(5)))
			{
				System.out.println("[Erreur] Ligne <"+m2.group(5)+"> hors portee");
				return;
			}
			
			// Si egal, on met a jour la ligne concernee
			table.updateInRow(Integer.parseInt(m2.group(5)), Integer.parseInt(m2.group(2)), Byte.parseByte(m2.group(3)));		
		}
		else if(m2.group(4).equals("<="))
		{
			int limite;
			limite = table.size()<=Integer.parseInt(m2.group(5)) ? table.size() : Integer.parseInt(m2.group(5));
			
			// On itere sur toute la table et on modifie les lignes dont l'index est <=
			for(int i=0; i<limite; i++)
			{
				table.updateInRow(i, Integer.parseInt(m2.group(2)), Byte.parseByte(m2.group(3)));
			}
		}
		else //">="
		{
			int limite;
			limite = table.size()<=Integer.parseInt(m2.group(5)) ? -1 : table.size();
			
			if(limite != -1)
			{
				// On itere sur toute la table et on modifie les lignes dont l'index est >=
				for(int i=Integer.parseInt(m2.group(5)); i<limite; i++)
				{
					table.updateInRow(i, Integer.parseInt(m2.group(2)), Byte.parseByte(m2.group(3)));
				}
			}
		}
		
		System.out.println("Update effectuee avec succes");
	}

	/**
	 * Met a jour une colonne d'un Nuplet si une colonne remplie la condition.
	 * <br/>
	 * <br/>"(?i)^\\s*UPDATE (\\w+) SET (\\d+)\\s*=\\s*(\\d+) WHERE (\\d+)\\s*(=|<=|>=)\\s*(\\d+)\\s*$"
	 * <br/>
	 * @param m2 Objet Matcher
	 */
	protected void updateByColumn(Matcher m2) 
	{
		// Verifications
		if(this.db==null)
		{
			System.out.println("[Erreur] Aucune base de donnees selectionee");
			return;
		}
		
		if(!this.db.containsKey(m2.group(1)))
		{
			System.out.println("[Erreur] La table <"+m2.group(1)+"> n'existe pas");
			return;
		}	
		
		if(this.db.get(m2.group(1)).getNupletSize()<=Integer.parseInt(m2.group(2)))
		{
			System.out.println("[Erreur] Colonne <"+m2.group(2)+"> hors portee");
			return;
		}
		
		if(this.db.get(m2.group(1)).getNupletSize()<=Integer.parseInt(m2.group(4)))
		{
			System.out.println("[Erreur] Colonne <"+m2.group(4)+"> hors portee");
			return;
		}
		
		// Traitement
		TableInt table = this.db.get(m2.group(1));
		int setColumn  = Integer.parseInt(m2.group(2));
		int ifColumn   = Integer.parseInt(m2.group(4));
		byte newValue  = Byte.parseByte(m2.group(3));
		byte ifValue   = Byte.parseByte(m2.group(6));
		
		if(m2.group(5).equals("="))
		{
			// On itere sur la table et on modifie la ligne si la colonne repond aux conditions
			for(int i=0; i<table.size(); i++) 
			{
				if((byte)table.get(i).getAtt(ifColumn) == ifValue)
				{
					table.updateInRow(i, setColumn, newValue);
				}
			}
		}
		else if(m2.group(5).equals("<="))
		{
			for(int i=0; i<table.size(); i++) 
			{
				if((byte)table.get(i).getAtt(ifColumn) <= ifValue)
				{
					table.updateInRow(i, setColumn, newValue);
				}
			}
		}
		else //">="
		{
			for(int i=0; i<table.size(); i++) 
			{
				if((byte)table.get(i).getAtt(ifColumn) >= ifValue)
				{
					table.updateInRow(i, setColumn, newValue);
				}
			}
		}
		
		System.out.println("Update effectuee avec succes");
	}

	/**
	 * Supprime un Nuplet de la table a l'index indiquee par l'utilisateur. Decremente l'ID des Nuplets sucesseurs.
	 * <br/>
	 * <br/>"(?i)^\\s*DELETE FROM (\\w+) WHERE ROW\\s*=\\s*(\\d+)\\s*$"
	 * <br/>
	 * @param m2 Objet Matcher
	 */
	protected void deleteByRow(Matcher m2) 
	{
		// Verifications
		if(this.db==null)
		{
			System.out.println("[Erreur] Aucune base de donnees selectionee");
			return;
		}
		
		if(!this.db.containsKey(m2.group(1)))
		{
			System.out.println("[Erreur] La table <"+m2.group(1)+"> n'existe pas");
			return;
		}
		
		// Traitement
		TableInt table = this.db.get(m2.group(1));
		if(table.size() <= Integer.parseInt(m2.group(2)))
		{
			System.out.println("[Erreur] Ligne <"+m2.group(2)+"> hors portee");
			return;
		}

		// On supprime la ligne demandee
		table.deleteRow(Integer.parseInt(m2.group(2)));
		
		// Enregistrement immediat dans la BDD
		this.updateDb(table, m2.group(1));
				
		System.out.println("Effacement effectuee avec succes");
	}

	/**
	 * Supprime un Nuplet de la table si une colonne remplie la condition demandee. Decremente l'ID des Nuplets predecesseurs.
	 * <br/>
	 * <br/>(?i)^\\s*DELETE FROM (\\w+) WHERE (\\d+)\\s*=\\s*(\\d+)\\s*$"
	 * <br/>
	 * @param m2 Objet Matcher
	 */
	protected void deleteByColumn(Matcher m2) 
	{
		// Verifications
		if(this.db==null)
		{
			System.out.println("[Erreur] Aucune base de donnees selectionee");
			return;
		}
			
		if(!this.db.containsKey(m2.group(1)))
		{
			System.out.println("[Erreur] La table <"+m2.group(1)+"> n'existe pas");
			return;
		}
		
		// Traitement
		TableInt table = this.db.get(m2.group(1));		
		if(table.getNupletSize()<=Integer.parseInt(m2.group(2)))
		{
			System.out.println("[Erreur] Colonne <"+m2.group(2)+"> hors portee");
			return;
		}
		
		// On supprime toutes les lignes dont la colonne respecte la condition
		table.delete(Integer.parseInt(m2.group(2)), Byte.parseByte(m2.group(3)));
		
		// Enregistrement immediat dans la BDD
		this.updateDb(table, m2.group(1));
		
		System.out.println("Effacement effectuee avec succes");
	}

	/**
	 * Permet d'insérer une ligne dans une table. <b>Le nom de la table est case-sensitive</b>.
	 * <br/>
	 * <br/>"(?i)^\\s*INSERT INTO (\\w+) VALUES\\s*[(]\\s*((?:\\d+\\s*,\\s*)*\\d+[)]\\s*$)"
	 * <br/>
	 * @param m2 Objet Matcher
	 */
	protected void insertTableInt(Matcher m2) 
	{
		// Verifications
		if(this.db==null)
		{
			System.out.println("[Erreur] Aucune base de donnees selectionee");
			return;
		}
					
		if(!this.db.containsKey(m2.group(1)))
		{
			System.out.println("[Erreur] La table <"+m2.group(1)+"> n'existe pas");
			return;
		}
		
		// Traitement
		TableInt table  = this.db.get(m2.group(1));
		String[] values = m2.group(2).replaceAll("\\s+", "").split(",");
		if(values.length!=table.getNupletSize())
		{
			System.out.println("[Erreur] Nombre d'arguments invalide");
			return;
		}		
		
		Nuplet nouveau = new NupletInt(table.getNupletSize());
		for(int i=0; i<table.getNupletSize(); i++)
		{
			nouveau.putAtt(i, Byte.parseByte(values[i]));
		}
		
		// Ajout du nouvel nuplet
		table.put(nouveau);
		
		// Enregistrement immediat dans la BDD
		this.updateDb(table, m2.group(1));
		
		nouveau.print();
		System.out.println("Insertion dans <"+m2.group(1)+"> avec succes");
		
	}

	/**
	 * Permet de creer une table contenant des int. <b>Le nom de la table est case-sensitive</b>.
	 * 
	 * "(?i)^\\s*CREATE TABLEINT (\\w+) ([1-9]+)\\s*$"
	 * @param m2
	 */
	protected void createTableInt(Matcher m2) 
	{
		// Verifications
		if(this.db==null)
		{
			System.out.println("[Erreur] Aucune base de donnees selectionee");
			return;
		}
				
		if(this.db.containsKey(m2.group(1)))
		{
			System.out.println("[Erreur] La table <"+m2.group(1)+"> existe deja");
			return;
		}
		
		// Traitement
		PrintWriter fp = null;
		try {
			// Ouverture puis ecriture dans le fichier database
			fp = new PrintWriter(new FileWriter(this.dbFd, true));
			fp.println(m2.group(1)+":"+m2.group(2)+":0");
			
			// Ajout de la table dans la variable Map
			this.db.put(m2.group(1), new TableInt(this.tableFolder+m2.group(1), Integer.parseInt(m2.group(2))));
			
			// Creation d'un fichier vide qui contiendra les donnees de la table
			File f = new File(this.tableFolder+m2.group(1));
			f.createNewFile();
			System.out.println("La table <"+m2.group(1)+"> a ete cree");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(fp!=null)
			{
				fp.close();
			}
		}
	}

	/**
	 * Selectionne des elements d'une table, avec ou sans conditions sur une colonne.
	 * <br/>
	 * <br/>R.E. "(?i)^SELECT ((?:(?:\\d+\\s*,\\s*)*)\\d+|[*]) FROM (\\w+)\\s*($|WHERE (\\d+)\\s*(=|<=|>=\\s*(\\d+)\\s*$)"
	 * @param m2
	 */
	protected void selectByColumn(Matcher m2) 
	{
		// Verification
		if(this.db==null)
		{
			System.out.println("[Erreur] Aucune base de donnees selectionee");
			return;
		}
		
		if(!this.db.containsKey(m2.group(2)))
		{
			System.out.println("[Erreur] La table <"+m2.group(2)+"> n'existe pas");
			return;
		}	
		
		if(m2.group(3) != null)
		{
			if(this.db.get(m2.group(2)).size()<=Integer.parseInt(m2.group(3)))
			{
				System.out.println("[Erreur] La colonne <"+m2.group(3)+"> est hors portee");
				return;
			}
		}
		
		// Traitement
		Nuplet[] res = null;
		TableInt table = this.db.get(m2.group(2));
		if(m2.group(3) != null) // Si WHERE est present
		{
			if(m2.group(4).equals("="))
			{
				res = table.getByAtt(Integer.parseInt(m2.group(3)), Byte.parseByte(m2.group(5)));
			}
			else if(m2.group(4).equals("<="))
			{
				int limite = table.size() <= Integer.parseInt(m2.group(3)) ? table.size()-1 : Integer.parseInt(m2.group(3));
				res = this.restrictor.inferieur(table.fullScan(), limite, Byte.parseByte(m2.group(5)));
				
			}
			else // >=
			{
				int limite = table.size() <= Integer.parseInt(m2.group(3)) ? -1 : Integer.parseInt(m2.group(3));
				if(limite == -1)
				{
					// On aura rien a afficher, donc on quitte tout de suite
					System.out.println();
					return;
				}
				res = this.restrictor.superieur(table.fullScan(), Integer.parseInt(m2.group(3)), Byte.parseByte(m2.group(5)));
			}

		}
		else // WHERE est absent
		{
			res = table.fullScan();
		}

		// Si des colonnes particulieres sont a afficher, on va faire une projection
		if(!m2.group(1).equals("*"))
		{
			String[] colonnes   = m2.group(1).split(",");
			int[]    atts       = new int[colonnes.length];
			int      nupletSize = table.getNupletSize();
			
			for(int i=0; i<colonnes.length; i++)
			{
				atts[i] = Integer.parseInt(colonnes[i]);
				if(atts[i]>nupletSize)
				{
					System.out.println("[Erreur] Index <"+atts[i]+"> hors portee");
					return;
				}
			}
			
			res = this.projector.project(res, atts);
		}

		if(res == null)
		{
			// Dans le doute ou 'res' n'aura pas ete modifie, on affiche rien
			System.out.println("");
		}
		else
		{
			this.printNuplets(res);
		}
	}
	
	/**
	 * Selectionne des elements d'une table, avec ou sans conditions sur l'index.
	 * <br/>
	 * <br/>R.E."(?i)^SELECT ((?:(?:\\d+\\s*,\\s*)*)\\d+|[*]) FROM (\\w+)\\s*(?:$|WHERE ROW\\s*(=|>=|<=)\\s*(\\d+)\\s*$)"
	 * @param m2
	 */
	protected void selectByRow(Matcher m2) 
	{
		// Verification 
		if(this.db==null)
		{
			System.out.println("[Erreur] Aucune base de donnees selectionee");
			return;
		}
		
		if(!this.db.containsKey(m2.group(2)))
		{
			System.out.println("[Erreur] La table <"+m2.group(2)+"> n'existe pas");
			return;
		}
		
		// Traitement
		Nuplet[] res ;
		if(m2.group(4) != null)
		{
			int      ligne = Integer.parseInt(m2.group(4));
			TableInt table = this.db.get(m2.group(2));
			
			if(m2.group(3).equals("="))
			{
				if(table.size()<=ligne)
				{
					System.out.println("[Erreur] Ligne <"+ligne+"> hors de portee");
					return;
				}
				res = new Nuplet[] {table.get(ligne)};
			}
			else if(m2.group(3).equals("<="))
			{
				ligne = table.size()<=ligne ? table.size()-1 : ligne;
				res   = this.restrictor.inferieurId(table.fullScan(), ligne);
			}
			else
			{
				if(ligne<table.size())
				{
					res = this.restrictor.superieurId(table.fullScan(), ligne);
				}
				else
				{
					// On aura rien a afficher, donc on quitte tout de suite
					System.out.println("");
					return;
				}
			}
		}
		else
		{
			res = this.db.get(m2.group(2)).fullScan();
		}

		// Dans le cas ou il faut selectionner des colonnes : projection
		if(!m2.group(1).equals("*"))
		{
			String[] colonnes   = m2.group(1).split(",");
			int[]    atts       = new int[colonnes.length];
			int      nupletSize = this.db.get(m2.group(2)).getNupletSize();
			
			for(int i=0; i<colonnes.length; i++)
			{
				atts[i] = Integer.parseInt(colonnes[i]);
				if(atts[i]>nupletSize)
				{
					System.out.println("[Erreur] Index <"+atts[i]+"> hors portee");
					return;
				}
			}
			
			res = this.projector.project(res, atts);
		}

		if(res == null)
		{
			// Au cas ou la variable est toujours null
			System.out.println("");
		}
		else
		{
			this.printNuplets(res);
		}
		
	}

	/**
	 * Utilise la database designee par l'utilisateur si elle existe.
	 * <br>
	 * <br>Usage : USE DATABASE db
	 * <br>E.R. "(?i)^\\s*USE DATABASE (\\w+)\\s*$"
	 * 
	 * @param m2 Input parse de l'utilisateur
	 */
	protected void useDatabase(Matcher m2) 
	{
		// Normalisation vers le dossier et l'extension adequate
		this.dbFd = new File(this.dbFolder+m2.group(1)+".sgbd");
		if(dbFd.isFile())
		{
			this.loadDb(dbFd);
			this.prompt = "SGBD["+m2.group(1)+"] > ";
			System.out.println("Database changed to '"+m2.group(1)+"'");
		}
		else
		{
			this.dbFd = null;
			System.out.println("[Error] La base de donnees <"+m2.group(1)+"> n'existe pas");
		}
	}

	/**
	 * Affiche les bases de donnees actuellement presentes sur le disque.
	 * <br/>
	 * <br/> Une base de donnnees est sauvegardee sous la forme :
	 * <br/> nom_table:taille_nuplet:taille_table
	 * <br/>
	 * <br/>"(?i)^\\s*SHOW DATABASES\\s*$"
	 */
	protected void showDatabases() 
	{
		System.out.println("=========== DATABASES ============");
		File dir = new File(this.dbFolder);
		for (File file : dir.listFiles()) {
		    if (file.isFile()) {
		        System.out.println("|| "+file.getName().split("[.]")[0]);
		    }
		}
		System.out.println("==================================");
		
	}
	
	/**
	 * Affiche les tables disponibles sur le disques
	 * <br/>
	 * <br/> Une table peut entrer en conflit avec une autre table de nom identique.
	 * "(?i)^\\s*SHOW TABLES\\s*$"
	 */
	protected void showTables() 
	{
		System.out.println("============= TABLES =============");
		for(Map.Entry<String, TableInt> entry : this.db.entrySet())
		{
			System.out.println("|| "+entry.getValue().getName());
		}
		System.out.println("==================================");		
	}

	/**
	 * "(?i)^\\s*HELP\\s*$"
	 */
	protected void help() {
		System.out.println("Liste des requetes actuellement reconnues :");
		System.out.println("SELECT [colonnes,]*colonne | * FROM table [WHERE colonne = condition] ;");
		System.out.println("SELECT [colonnes,]*colonne | * FROM table [WHERE ROW = ligne] ;");
		System.out.println("UPDATE table SET colonne1 = newValue WHERE colonne2 = condition2 ;");
		System.out.println("UPDATE table SET colonne = value WHERE ROW = condition ;");
		System.out.println("INSERT INTO table VALUES (values, ..., values) ;");
		System.out.println("DELETE FROM table WHERE colonne = condition ;");
		System.out.println("DELETE FROM table WHERE ROW = condition ;");
		System.out.println("CREATE TABLEINT nom taille ;");
		System.out.println("USE DATABASE database ;");
		System.out.println("SHOW DATABASES ;");
		System.out.println("SHOW TABLES ;");
		System.out.println("EXIT ;");		
	}
	
	/**
	 * Permet de charger une base de donnee depuis son fichier descriptif.
	 * 
	 * @param f Fichier contenant les informations 
	 */
	protected void loadDb(File f)
	{
		// On instancie un nouvel HashMap pour la db
		this.db = new HashMap<String, TableInt>();
		
		// Recuperation et lecture du fichier descriptif de la base
		try {
			Scanner sc = new Scanner(f);
			String[] ligne;
			while(sc.hasNext())
			{
				ligne = sc.next().split(":");
				if(ligne.length == 3)
				{
					this.db.put(ligne[0], this.loadTable(this.tableFolder+ligne[0], Integer.parseInt(ligne[1]), Integer.parseInt(ligne[2])));
				}
			}
			sc.close();
		} catch (FileNotFoundException | ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
			System.out.println("[Erreur] La BDD n'a pas pu etre chargee");
		}
	}
	
	/**
	 * Charge une table en mémoire et l'ajoute dans la variable Map<String, TableInt> du SGBD
	 */
	protected TableInt loadTable(String filePath, int nupletSize, int tableSize)
	{
		TableInt result = new TableInt(filePath, nupletSize, tableSize);
		return result;
	}
	
	
	/**
	 * Affiche sous une forme lisible tous les nuplets du tableau de nuplet n.
	 * 
	 * @param n Nuplet[] Le tableau dont on doit afficher les nuplets.
	 */
	protected void printNuplets(Nuplet[] n)
	{
		for(Nuplet u : n)
		{
			u.print();
		}
	}
	
	// GETTERS / SETTERS
	
	public String getPrompt() {
		return prompt;
	}
	
	/**
	 * Renvoie true sur le parser est en etat de fonctionnement.
	 * 
	 * @return status boolean
	 */
	public boolean isRunning()
	{
		return this.status;
	}

	
	/**
	 * Met le fichier de la database a jour en y modifiant l'etat de la table table referencee par nom.
	 * 
	 * @param table TableInt La table à mettre à jour dans le fichier
	 * @param nom String Le string qui permet de le referencer dans la variable Map.
	 */
	protected void updateDb(TableInt table, String nom)
	{
		try {
			// On recupere tout le contenu du fichier
			List<String> fileDb = new ArrayList<>(Files.readAllLines(this.dbFd.toPath(), StandardCharsets.UTF_8));

			// On lit le fichier ligne par ligne
			for (int i=0; i<fileDb.size(); i++) 
			{
				// Des que la ligne a remplacer apparait, on break
			    if(fileDb.get(i).contains(nom+":"+table.getNupletSize()+":")) 
			    {
			    	fileDb.set(i, nom+":"+table.getNupletSize()+":"+table.size());
			        break;
			    }
			}
			
			// Et l'on rentre la nouvelle valeur
			Files.write(this.dbFd.toPath(), fileDb, StandardCharsets.UTF_8);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
