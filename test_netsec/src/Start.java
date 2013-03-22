import SecFeatures.Key;
import SecFeatures.PortKey;
import SecFeatures.SecAlgosWrapper;



public class Start {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		SecAlgosWrapper wrapper = new SecAlgosWrapper();
		
		String regmsg = wrapper.RegisterEncrypt("sasuke", "sasuke", 5000);
		
		regmsg = "register:sasuke:" + regmsg;
		
		PortKey decregmsg = null;
		try {
			decregmsg = wrapper.RegisterDecrypt(regmsg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(decregmsg!=null)
		{
			
			System.out.println("key ->" + decregmsg.key.strKey);
			System.out.println("KKK ->"+wrapper.Get_CliSvr_Key("sasuke", "sasuke"));
			System.out.println("key ->" + decregmsg.key.strHexKey);
			System.out.println("key ->" + wrapper.asHex(decregmsg.key.strKey.getBytes("ISO-8859-1")));
			System.out.println("key length ->" + decregmsg.key.strKey.length());
			System.out.println("Port ->"+decregmsg.PortNo);
			
		}
		else
			System.out.println("Incorrect data decregmsg!!!");
		
		String logmsg = wrapper.LoginEncrypt("sasuke", "sasuke",6000);
		
		logmsg = "login:sasuke:" + logmsg;
		
		PortKey declogmsg = wrapper.LoginDecrypt(decregmsg.key.strHexKey, logmsg);
		
		if(declogmsg!=null)
		{
			System.out.println("key ->" + declogmsg.key.strKey);
			System.out.println("hex key ->" + declogmsg.key.strHexKey);
			System.out.println("key ->" + wrapper.asHex(declogmsg.key.strKey.getBytes("ISO-8859-1")));
			System.out.println("key length ->" + declogmsg.key.strKey.length());
			System.out.println("Port ->"+declogmsg.PortNo);
			
		}
		else
			System.out.println("Incorrect data declogmsg !!!");
		
		//System.out.println("key ->" + declogmsg.strKey);
		//declogmsgSystem.out.println("key ->" + declogmsg.strHexKey);
		
		
		String encMsg = wrapper.EncryptMessage(decregmsg.key.strHexKey, "I want to encrypt this message using the key generated earlier and decrypt it with teh same key and check both of them return the same string !!!!");
		
		String decMsg = wrapper.DecryptMessage(decregmsg.key.strHexKey, encMsg);
		
		System.out.println("decMsg ->"+decMsg);
		
		Key k = wrapper.KeyGen64();
		
		System.out.println("k.strKey.length() ->"+k.strKey.length());
		System.out.println("k.strHexKey.length() ->"+k.strHexKey.length());
		
		Key k1 = wrapper.KeyGen32();
		
		System.out.println("k.strKey.length() ->"+k1.strKey.length());
		System.out.println("k.strHexKey.length() ->"+k1.strHexKey.length());
	}
	
    public static String asHex (byte buf[]) {
        StringBuffer strbuf = new StringBuffer(buf.length * 2);
        int i;

        for (i = 0; i < buf.length; i++) {
         if (((int) buf[i] & 0xff) < 0x10)
  	    strbuf.append("0");

         strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
        }

        return strbuf.toString();
       }


}
