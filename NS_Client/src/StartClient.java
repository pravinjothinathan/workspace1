import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;


public class StartClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			Socket sock = new Socket("localhost",Integer.parseInt(args[0]));
			sock.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
