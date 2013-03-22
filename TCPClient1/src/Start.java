
public class Start {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//CheckForTCPServer Server = new CheckForTCPServer();
		
		//String host ="localhost";
		//String host  = "www.wikipedia.org";
		
		//Server.check(host);
		
		CheckDataInputStreaminaSocket Sock = new CheckDataInputStreaminaSocket();
		
		String s = Sock.getInputStream(7);
		System.out.println(s);
	}

}
