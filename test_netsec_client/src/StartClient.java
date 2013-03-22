import java.net.Socket;


public class StartClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int port = Integer.parseInt(args[0]);
	
		try {
			Socket sock = new Socket("localhost",port);
			InputStream is = new InputStream(sock);
			OutputStream os = new OutputStream(sock);
			os.start();
			is.start();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

}
