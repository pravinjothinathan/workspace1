package SecFeatures;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;



public class SecAlgosWrapper {
	
	public PortKey RegisterDecrypt(String encMsg) throws Exception
	{
		RSA rsa = new RSA();
		String Msg ="";
		try {
//only the third portion of hte message is taken first two are for reister and username which are sent as clear text and hence am not worried about them
			System.out.println("incoming message - > "+encMsg);
		 Msg = rsa.DecryptMessage(asBytes(encMsg.split(":")[2]));
		}catch(Exception e)
		{
			System.out.println("Exception in RegisterDecrypt error in string to byte[] conversion");
		}
		String extUsername = encMsg.split(":")[1];
		String intUsername = Msg.split(":")[0];
		
		String HexKey = Msg.split(":")[1];
		String key = new String(asBytes(HexKey),"ISO-8859-1");
		int Portno = Integer.parseInt(Msg.split(":")[2]);
		
		if(intUsername.equals(extUsername))
			return new PortKey(key,HexKey,Portno);
		else
			return null;
	}
	
	//similar funcitona
	public String RegisterEncrypt(String username,String password,int portno)
	{
		
		String strUnamePwd = String.format("%s:%s", username,password);
		Sha512 sha = new Sha512();
		byte[] hash = sha.GetDigestinByte(strUnamePwd);
		
		String Msg ="";
		//String MsgwithIntegrity ="";
		//byte[] IntegrityHash = null;
		try {
			Msg = String.format("%s:%s:%d",username,asHex(hash),portno);
			System.out.println("hash:"+asHex(hash));
			//System.out.println("Msg to hash for integrity : "+asHex(hash));
			//IntegrityHash = sha.GetDigestinByte(Msg);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		RSA rsa = new RSA();
		byte[] encMsg = rsa.EncryptMessage(Msg);
		String str = asHex(encMsg);//+":"+asHex(IntegrityHash);
		
		return str;

	}
	
	//operation will have 2 values
	// 1-> Registration
	//  Login otherwise
	//based on that it will encrypt and return a string
	public String LoginEncrypt(String username,String password,int Portno)
	{
		//next 3 lines username and password are joined to form a string which is then converted to their hash!!
		String strUnamePwd = String.format("%s:%s", username,password);
		Sha512 sha = new Sha512();
		byte[] hash = sha.GetDigestinByte(strUnamePwd);
		
		String Msg ="";
		String MsgwithIntegrity ="";
		try {
			//Hash is then atached with the use name to from a message string
			Msg = String.format("%s:%s:%d",username,asHex(hash),Portno);
			System.out.println("Msg ->"+Msg);
			//Hash of the messages string is taken and attached to the message for integrity check!
			String IntegrityHash = sha.GetDigest(Msg);
			MsgwithIntegrity = String.format("%s::%s",Msg,IntegrityHash);
			
		} catch (Exception e1) {
			System.out.println("Exception in LoginEncrypt !!!");
		}
		
		//AES Encryption is performed on the final message 
		AES_256 aes = new AES_256();
		Helper h = new Helper();
		byte[] encMsg = aes.EncryptMessage(h.ReduceKeySizetoHalf(hash),MsgwithIntegrity);
		
		//Finally the message is converted to hex so that it can be sent in out of the socket 
		String str = asHex(encMsg);
		
		return str;
	}
	
	public PortKey LoginDecrypt(String Hexkey,String encMsg) throws Exception
	{
		String key = new String(asBytes(Hexkey),"ISO-8859-1");
		AES_256 aes = new AES_256();
		String Msg ="";
		System.out.println("encMsg->"+encMsg);
		try {
//only the third portion of hte message is taken first two are for reister and username which are sent as clear text and hence am not worried about them
			Helper h = new Helper();
			Msg = aes.DecryptMessage(h.ReduceKeySizetoHalf(key.getBytes("ISO-8859-1")),asBytes(encMsg.split(":")[2]));
		}catch(Exception e)
		{
			System.out.println("Exception in RegisterDecrypt error in string to byte[] conversion");
		}
		
		String[] vals = Msg.split("::");
		//integrity check
		Sha512 sha = new Sha512();
		String msghash = sha.GetDigest(vals[0]);
		if(msghash.equals(vals[1]))
		{
			String intUsername = vals[0].split(":")[0];
			String extUsername = encMsg.split(":")[1];
			if(intUsername.equals(extUsername))
			{
				String strHexKey = vals[0].split(":")[1];
				int PortNo = Integer.parseInt(vals[0].split(":")[2]);
				String strkey = new String(asBytes(strHexKey),"ISO-8859-1");
				return new PortKey(strkey,strHexKey,PortNo);
			}
		}
		return null;
	}
	
	public boolean ValidateKey(String storedKey,String IncomingKey)
	{
		if(storedKey.equals(IncomingKey))
			return true;
		else
			return false;
	}
	
	public String EncryptMessage(String Hexkey, String Msg)
	{
		
		Helper h = new Helper();
		byte[] newkey = null;
		try {
			String key = new String(asBytes(Hexkey),"ISO-8859-1");
			newkey = h.ReduceKeySizetoHalf(key.getBytes("ISO-8859-1"));
		} catch (Exception e) {
			System.out.println("Exception EncryptMessage Error in type conversion!!");
			e.printStackTrace();
		}
		Sha512 sha = new Sha512();
		String IntegrityHash = sha.GetDigest(Msg);
		String MsgwithIntegrity = String.format("%s::%s",Msg,IntegrityHash);
		AES_256 aes = new AES_256();
		byte[] encMsg = aes.EncryptMessage(newkey, MsgwithIntegrity);
		String retval = null;
		try {
			retval = asHex(encMsg);//new String(encMsg,"ISO-8859-1");
			System.out.println("retval ->"+retval);
		} catch (Exception e) {
			System.out.println("Exception in EncryptMessage");
			e.printStackTrace();
		}
		return retval;
	}

	public String DecryptMessage(String Hexkey, String n1) {
		try {
			String deckey = new String(asBytes(Hexkey),"ISO-8859-1");
			Helper h = new Helper();
			byte[] newkey = h.ReduceKeySizetoHalf(deckey.getBytes("ISO-8859-1"));
			AES_256 aes = new AES_256();
			byte[] newn1 = asBytes(n1);
			String decMsg = aes.DecryptMessage(newkey, newn1);
			System.out.println("decMsg -> "+decMsg);
			String[] vals = decMsg.split("::");
			System.out.println("vals[0] -> "+vals[0]);
			System.out.println("vals[1] -> "+vals[1]);
			Sha512 sha = new Sha512();
			String digest = sha.GetDigest(vals[0]);
			if(digest.equals(vals[1]))
				return vals[0];
			else
				return null;
		} catch (Exception e) {
			System.out.println("Exception in DecryptMessage");
			e.printStackTrace();
		}
		return null;
	}
	
	public String Get_CliSvr_Key(String username,String password)
	{
		//String key =null;
		
		String strUnamePwd = String.format("%s:%s", username,password);
		Sha512 sha = new Sha512();
		byte[] hash = sha.GetDigestinByte(strUnamePwd);
		
		//try {
			//key = new String(hash,"ISO-8859-1");
		//} catch (Exception e) {
			///System.out.println("Exception in encoding Get_CliSvr_Key!!!");
		//} 
		return asHex(hash);
	}
	
	public Key KeyGen32()
	{
		AES_256 aes = new AES_256();
		Key key = new Key();
		
		try {
			byte[] bgenkey = aes.GenerateKey();
			key.strKey = new String(bgenkey,"ISO-8859-1");
			key.strHexKey = asHex(bgenkey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return key;
		
	}
	
	public Key KeyGen64()
	{
		AES_256 aes = new AES_256();
		Key key = new Key();
		
		try {
			byte[] bgenkey1 = aes.GenerateKey();
			byte[] bgenkey2 = aes.GenerateKey();
			key.strKey = new String(bgenkey1,"ISO-8859-1") + new String(bgenkey2,"ISO-8859-1");
			key.strHexKey = asHex(bgenkey1) + asHex(bgenkey2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return key;
		
	}
	
	public String HexFormat(String input)
	{
		try {
			return String.format("%0128x", new BigInteger(1, input.getBytes("ISO-8859-1")));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public String asHex (byte buf[]) {
        StringBuffer strbuf = new StringBuffer(buf.length * 2);
        int i;

        for (i = 0; i < buf.length; i++) {
         if (((int) buf[i] & 0xff) < 0x10)
  	    strbuf.append("0");

         strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
        }

        return strbuf.toString();
       }
	
	public byte[] asBytes (String s) {
        String s2;
        byte[] b = new byte[s.length() / 2];
        int i;
        for (i = 0; i < s.length() / 2; i++) {
            s2 = s.substring(i * 2, i * 2 + 2);
            b[i] = (byte)(Integer.parseInt(s2, 16) & 0xff);
        }
        return b;
    }

}
