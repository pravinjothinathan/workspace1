package SecFeatures;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;


public class Sha512 {
	
	private MessageDigest mda;
	
	public Sha512() {
		Security.addProvider(new BouncyCastleProvider());
		try {
			mda = MessageDigest.getInstance("SHA-512", "BC");
		} catch (Exception e) {
			System.out.println("Exception!!! SHA 512 Obj creation!!");
		} 
		
	}
	
	
	public String GetDigest(String msg)
	{
		if(mda!=null)
		{
			byte[] digesta = null;
			try {
				digesta = mda.digest(msg.getBytes("ISO-8859-1"));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("Exception in GetDigest!!!!");
				e.printStackTrace();
			}
			
			return String.format("%0128x", new BigInteger(1, digesta));
		}
		else
			return null;
	}
	
	public byte[] GetDigestinByte(String msg)
	{
		if(mda!=null)
		{
			return mda.digest(msg.getBytes());
		}
		else
			return null;

	}


}
