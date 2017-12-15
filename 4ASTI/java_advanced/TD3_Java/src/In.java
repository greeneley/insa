import java.io.*;


public class In 
{
	private static int c;
	
	// ====== CONSTRUCTORS ======
	public In()
	{
		this.init();
	}
	
	// ====== METHODS ======
	private static boolean isWhite()
	{
		return Character.isWhitespace((char) c);
	}
	
	private static void readC()
	{
		try
		{
			c = System.in.read();
		}
		catch(IOException e)
		{
			c = -1;
		}
	}
	
	public boolean isEmpty()
	{
		return(this.c != -1);
	}
	
	/* ========== TODO ========== */
	public void init()
	{
		this.c = -1;
	}
	
	public String getString()
	{
		return "tmp";
	}
	
	public double getDouble()
	{
		return 0.0;
	}
	
	public int getInt()
	{
		return 0;
	}
	
}
