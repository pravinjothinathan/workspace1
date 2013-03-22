package SecFeatures;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;


public class RSA {
	Serializer s;
	
	public RSA()
	{
        s = new Serializer();
	}
	
	public void GenerateKeyPair()
	{
		try {
			KeyPairGenerator kpGen = KeyPairGenerator.getInstance("RSA");
			kpGen.initialize(2048);
			KeyPair kp = kpGen.generateKeyPair();
			
			PrivateKey pk = kp.getPrivate();
	        PublicKey pubKey = kp.getPublic();
	        
	        s.SerializeData(pk, "privatekey.txt");
	        s.SerializeData(pubKey, "publickey.txt");
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception in creating Private and Public Key pair !!!");
		}
	}
	
	public byte[] EncryptMessage(String Message)
	{
		try{
		 PublicKey pk = (PublicKey)s.DeSerializeData("publickey.txt");  
		 Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
	     cipher.init(Cipher.ENCRYPT_MODE, pk);
	     
	     return cipher.doFinal(Message.getBytes());
		}catch(Exception e) {
			System.out.println("Exception in Encrypting Message !!!");
			e.printStackTrace();
		}
		
		return null;
	}
	
	public String DecryptMessage(byte[] ciphertxt)
	{
		try {
			PrivateKey pk = (PrivateKey)s.DeSerializeData("privatekey.txt");
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.DECRYPT_MODE, pk);
	        byte[] newPlainText = cipher.doFinal(ciphertxt);
	        String originalString = new String(newPlainText);
		    return originalString;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
		

}
