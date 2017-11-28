
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
	
	private String element;
	private MessageDigest md;
	private byte[] digest;
	
	final int MINIMUM_BYTE = 128;
	
	
	public MD5() throws NoSuchAlgorithmException
	{
		this.md = MessageDigest.getInstance("MD5");
	}
	
	public void update(String element)
	{
		this.element = element;
		this.md.update(this.element.getBytes());
	}
	
	public void digest()
	{
		this.md.update(this.element.getBytes());
		this.digest = this.md.digest();
	}
	
	public int hacheK(int k)
	{
		return this.digest[k] + this.MINIMUM_BYTE;
	}

}
