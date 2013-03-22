import java.io.DataInputStream;
import java.net.Socket;


public class InputStream extends Thread{
	
	DataInputStream disDataIn;

	public InputStream(Socket sock) {
		// TODO Auto-generated constructor stub
		try {
			disDataIn = new DataInputStream(sock.getInputStream());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
