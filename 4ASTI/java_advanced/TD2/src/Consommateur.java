
public class Consommateur extends Thread
{
	private int[] t;

	public Consommateur(int[] t)
	{
		this.t = t;
	}
	
	public void run()
	{
		synchronized(this.t)
		{
			while(true)
			{
				if (this.t[0] == 1)
				{
					this.consommer(this.t, this.t.length);
				}
				try {
					this.t.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public void consommer(int[] t, int taille)
	{
		synchronized(t)
		{
			t[0] = 0;
			System.out.println(this.getName()+" : Buffer consomme");
			this.t.notify();
		}
	}
}
