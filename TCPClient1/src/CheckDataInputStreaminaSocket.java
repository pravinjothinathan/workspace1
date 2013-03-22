import java.io.DataInputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;


public class CheckDataInputStreaminaSocket {

	@SuppressWarnings("deprecation")
	public String getInputStream(int i) {
		// TODO Auto-generated method stub
		Socket sock;
		DataInputStream dataIS;
		
		try
		{
			InetAddress addr = InetAddress.getLocalHost();
			sock = new Socket(addr,i);
			dataIS = new DataInputStream(sock.getInputStream());
			return dataIS.toString();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

}
