package SecFeatures;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;



public class AES_256 {
	
	private KeyGenerator kgen;
	
	public AES_256()
	{
		try {
			kgen = KeyGenerator.getInstance("AES");
		    kgen.init(256);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception in Initializing AES Key to 256 !!!");
		}
		
	}
	
	public byte[] GenerateKey()
	{
			SecretKey skey = kgen.generateKey();
			return skey.getEncoded();
			
	}
	
	private SecretKeySpec GetSecretSpec(byte[] key)
	{
		return new SecretKeySpec(key, "AES");
	}
	
	public byte[] EncryptMessage(byte[] key,String Message)
	{
		try {
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, GetSecretSpec(key));
			byte[] encrypted = cipher.doFinal(Message.getBytes("ISO-8859-1"));
			return encrypted;
		} catch (Exception e) {
			System.out.println("Exception in AES Encryption of the message!!!");
			e.printStackTrace();
		}
		return null;
	}
	
	public String DecryptMessage(byte[] key,byte[] Ciphertxt)
	{
		try {
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, GetSecretSpec(key));
		       byte[] original = cipher.doFinal(Ciphertxt);
		       String originalString = new String(original,"ISO-8859-1");
		       return originalString;
		} catch (Exception e) {
		}
		return null;
	}
}
