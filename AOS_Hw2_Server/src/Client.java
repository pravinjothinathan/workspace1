import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;


public class Client {
	
	DataInputStream disDataIn;
	PrintStream psDataOut;
	
	public Client(Socket tempsock)
	{
		try {
			disDataIn = new DataInputStream(tempsock.getInputStream());
			psDataOut = new PrintStream(tempsock.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error in client Data input and output stream creations");
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("deprecation")
	public Message ReadStream()
	{
		String line;
		try {
			line = disDataIn.readLine();
			if(line != null)
				return new Message(line);
			else
				return null;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error in Message read!!");
			e.printStackTrace();
		}
		return null;
	}
}
