
public class check {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		SecAlgosWrapper saw = new SecAlgosWrapper();
		
		String s = saw.LoginEncrypt("Pravin gmail hello", "sasubi bib bibke", 1);
		
		System.out.println(s+"length "+s.length());
		
		String d = saw.LoginDecrypt(s);
		
		//System.out.println(d);
		
		
		
		//String k = d.split(":")[2];
		
	//	String em = saw.EncryptMessage(k, "i want os encrypt this message!!!");
		
		//System.out.println(em);
		
	//	String dm = saw.DecryptMessage(k, em);
		
		//System.out.println(dm);
		
	}

}
