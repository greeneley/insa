package sgbd.console;

import java.util.regex.Pattern;

/**
 * Cette classe a pour but de lier une commande dans le SGBD avec sa representation en expression reguliere.
 */
public class Adaptater {
	
	public Commande commande;
	public Pattern  pattern;
	
	
	public Adaptater(Commande com, String re)
	{
		this.commande = com;
		this.pattern  = Pattern.compile(re);
	}

}
