
public class Producteur extends Thread
{
	private int[] t;
	
	public Producteur(int[] t)
	{
		this.t = t;
	}
	
	public void run()
	{
		while(true)
		{
			synchronized(this.t)
			{
				if(this.t[0] == 0)
				{
					this.remplir(this.t);
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
	
	public void remplir(int[] t)
	{
		synchronized(t)
		{
			t[0] = 1;
			System.out.println("Buffer rempli");
			t.notify();
		}
	}
}
